package tantros.graphics.overlays;

import arc.Core;
import arc.Events;
import arc.func.Boolf;
import arc.func.Cons;
import arc.func.Cons2;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.math.Mathf;
import arc.math.geom.Position;
import arc.math.geom.QuadTree;
import arc.math.geom.Rect;
import arc.math.geom.Vec2;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.Time;
import arc.util.Tmp;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.graphics.Layer;
import mindustry.world.Tile;
import tantros.world.BuildingIndexer;
import tantros.world.blocks.effect.GroundPenetratingRadar;
import tantros.world.blocks.effect.SonarWrapper;
import tantros.world.blocks.environment.DeepOreBlock;

import static arc.Core.camera;

public class SonarTracking extends BuildingIndexer<Building> {

    public static final Seq<SonarSource> temp = new Seq<>();

    public static final Rect tempRect = new Rect();
    public static final Vec2 tempPos = new Vec2();

    public QuadTree<SonarSource> sourceTree;

    public SonarTracking(){
        super(Building.class);
        Events.on(EventType.ResetEvent.class, this::onResetEvent);
        Events.run(EventType.Trigger.update, this::update);
        Events.run(EventType.Trigger.draw, this::draw);
        this.sourceTree = new QuadTree<>(new Rect(0,0,0,0));
    }

    @Override
    public void onWorldLoadEvent(EventType.WorldLoadEvent event){
        this.clear();
        Vars.world.getQuadBounds(sourceTree.bounds);
        super.onWorldLoadEvent(event);
    }

    public void onResetEvent(EventType.ResetEvent event){
        this.clear();
    }

    public void clear(){
        sourceTree.clear();
    }

    public boolean get(Team team, float x, float y) {
        return sourceTree.intersect(x, y, 1, 1, (s)->{
            s.hitbox(tempRect);
            tempRect.getCenter(tempPos);
            return team == s.team() && tempPos.within(x, y, s.range());
        });
    }

    public boolean get(Team team, Vec2 pos) {
        return get(team, pos.x, pos.y);
    }

    @Override
    protected void add(Building building) {
        if(building instanceof GroundPenetratingRadar.GroundPenetratingRadarBuild b) {
            Log.info("Radar Added " + b.wrapper);
            this.add(b.wrapper);
        }
    }

    public void add(SonarSource source){
        sourceTree.insert(source);
    }

    @Override
    protected void remove(Building building) {
        if(building instanceof GroundPenetratingRadar.GroundPenetratingRadarBuild b) {
            this.remove(b.wrapper);
            b.wrapper.applyInRange(this::recacheTile, b.wrapper.maxRange(), b.wrapper);
        }
    }

    public void remove(SonarSource source){
        sourceTree.remove(source);
    }

    public void update(){
        temp.clear();
        sourceTree.getObjects(temp);
        sourceTree.clear();
        temp.each(sourceTree::insert);
        for(SonarSource source : temp){
            if(source.dirty()) {
                source.applyInRange(this::recacheTile, source.maxRange(), source);
            }
        }
    }

    public void recacheTile(Tile tile, SonarSource source){
        tile.recache();
        source.dirty(false);
    }

    public void draw(){
        if(Core.settings.getBool("debug-sonar-renderer")) {
            Draw.z(Layer.overlayUI);
            Lines.stroke(1f);

            sourceTree.intersect(camera.bounds(Tmp.r1), s -> {
                s.hitbox(Tmp.r2);
                Draw.color((s.dirty()?Color.red: Color.green));
                Lines.rect(Tmp.r2);
            });

            Draw.reset();
        }
    }

    public interface SonarSource extends QuadTree.QuadTreeObject{

        Rect tempRect = new Rect();
        Vec2 tempPos = new Vec2();

        float range();
        Team team();
        boolean dirty();
        void dirty(boolean dirty);

        default float maxRange(){
            return this.range();
        }

        default void applyInRange(Cons2<Tile, SonarSource> effect, float radius, SonarSource source){
            int range = Mathf.ceil(radius / Vars.tilesize);
            this.hitbox(tempRect);
            tempRect.getCenter(tempPos);
            for (int i = -range; i < range + 1; i++) {
                for (int j = -range; j < range + 1; j++) {
                    Tile tile = Vars.world.tileWorld(tempPos.x + (i * Vars.tilesize), tempPos.y + (j * Vars.tilesize));
                    if (tile != null) {
                        if ( tile.within(tempPos, radius) && tile.overlay() != null && tile.overlay() instanceof DeepOreBlock ore) {
                            effect.get(tile, source);
                        }
                    }
                }
            }
        }
    }
}
