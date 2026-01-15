package tantros.world.blocks.defense;

import arc.Core;
import arc.Graphics;
import arc.audio.Sound;
import arc.func.Boolf;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.math.geom.Rect;
import arc.scene.ui.ButtonGroup;
import arc.scene.ui.ImageButton;
import arc.scene.ui.layout.Table;
import arc.struct.Queue;
import arc.struct.Seq;
import arc.util.Eachable;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.content.Fx;
import mindustry.entities.Effect;
import mindustry.entities.Units;
import mindustry.entities.units.BuildPlan;
import mindustry.gen.*;
import mindustry.logic.LAccess;
import mindustry.ui.Styles;
import mindustry.world.Tile;
import mindustry.world.blocks.defense.AutoDoor;
import tantros.net.TantrosCalls;

import static mindustry.Vars.*;

public class DoorExtended extends WallExtended{
    protected final static Rect rect = new Rect();
    protected final static Seq<Unit> units = new Seq<>();
    protected final static Boolf<Unit> groundCheck = u -> u.isGrounded() && !u.type.allowLegStep;
    protected final static Queue<Door> doorQueue = new Queue<>();

    public final int timerToggle = timers++;

    public float checkInterval = 20f;
    public Effect openfx = Fx.dooropen;
    public Effect closefx = Fx.doorclose;
    public Sound doorSound = Sounds.door;
    public boolean chainEffect = false;
    public TextureRegion openRegion;
    public float triggerMargin = 12f;


    public DoorExtended(String name) {
        super(name);
        solid = false;
        solidifes = true;
        update = true;
        teamPassable = true;

        noUpdateDisabled = true;
        drawDisabled = true;

        configurable = true;

        config(Boolean.class, (DoorExtendedBuild base, Boolean open) -> {

            doorQueue.clear();
            doorQueue.add(base);

            for(Door door : base.chained.isEmpty() ? doorQueue : base.chained){
                //skip doors with things in them
                if((Units.anyEntities(door.tile()) && !open) || door.isOpen() == open){
                    continue;
                }

                door.setOpen(open);
            }
        });

        config(DoorMode.class, (DoorExtendedBuild base, DoorMode mode)->{
            for(Door door: base.chained) {
                if(door instanceof DoorExtendedBuild build) {
                    build.mode = mode;
                }
            }
        });
    }

    @Override
    public TextureRegion getPlanRegion(BuildPlan plan, Eachable<BuildPlan> list){
        return plan.config == Boolean.TRUE ? openRegion : region;
    }

    @Override
    public void load() {
        super.load();
        openRegion = Core.atlas.find(name + "-open");
    }

    public class DoorExtendedBuild extends WallExtendedBuild implements Door{
        public boolean open = false;
        public DoorMode mode = DoorMode.AUTO;
        public Seq<Door> chained = new Seq<>();

        public DoorExtendedBuild(){
            //make sure it is staggered
            timer.reset(timerToggle, Mathf.random(checkInterval));
        }

        @Override
        public void onProximityAdded(){
            super.onProximityAdded();
            updateChained();
        }

        @Override
        public void onProximityRemoved(){
            super.onProximityRemoved();

            for(Building b : proximity){
                if(b instanceof mindustry.world.blocks.defense.Door.DoorBuild d){
                    d.updateChained();
                }
            }
        }

        @Override
        public double sense(LAccess sensor){
            if(sensor == LAccess.enabled) return open ? 1 : 0;
            return super.sense(sensor);
        }

        @Override
        public void control(LAccess type, double p1, double p2, double p3, double p4){
            if(type == LAccess.enabled){
                boolean shouldOpen = !Mathf.zero(p1);

                if(net.client() || open == shouldOpen || (Units.anyEntities(tile) && !shouldOpen)){
                    return;
                }

                configureAny(shouldOpen);
            }
            if(type == LAccess.config){
                DoorMode mode = (Mathf.zero(p1))? DoorMode.AUTO:DoorMode.MANUAL;
                configureAny(mode);
            }
        }

        @Override
        public void updateTile(){
            if(timer(timerToggle, checkInterval) && !net.client() && this.mode == DoorMode.AUTO){
                units.clear();
                team.data().tree().intersect(rect.setSize(size * tilesize + triggerMargin * 2f).setCenter(x, y), units);
                boolean shouldOpen = units.contains(groundCheck);

                if(open != shouldOpen){
                    TantrosCalls.doorToggle(tile, shouldOpen);
                }
            }
        }

        @Override
        public void draw(){
            Draw.rect(open ? openRegion : region, x, y);
        }

        @Override
        public boolean checkSolid(){
            return !open;
        }

        @Override
        public void write(Writes write){
            super.write(write);
            write.bool(open);
            write.bool(this.mode == DoorMode.MANUAL);
        }

        @Override
        public void read(Reads read, byte revision){
            super.read(read, revision);
            open = read.bool();
            this.mode = (read.bool())? DoorMode.MANUAL: DoorMode.AUTO;
        }

        @Override
        public boolean isOpen() {
            return open;
        }

        @Override
        public void setOpen(boolean open){
            if( this.timer(timerToggle, 80f)) return;
            this.open = open;
            if(!world.isGenerating()) pathfinder.updateTile(tile);
            if(wasVisible){
                (!open ? closefx : openfx).at(this, size);
                doorSound.at(this);
            }
        }

        @Override
        public Tile tile() {
            return tile;
        }

        @Override
        public void updateChained(){
            chained = new Seq<>();
            doorQueue.clear();
            doorQueue.add(this);

            while(!doorQueue.isEmpty()){
                var next = doorQueue.removeLast();
                chained.add(next);
                if (next instanceof Building build){
                    for(var b : build.proximity){
                        if(b instanceof Door d && d.chained() != chained){
                            d.chained(chained);
                            doorQueue.addFirst(d);
                        }
                    }
                }
            }
        }

        @Override
        public Seq<Door> chained() {
            return chained;
        }

        @Override
        public void chained(Seq<Door> chained) {
            this.chained = chained;
        }

        @Override
        public Graphics.Cursor getCursor(){
            return interactable(player.team()) ? Graphics.Cursor.SystemCursor.hand : Graphics.Cursor.SystemCursor.arrow;
        }

        @Override
        public boolean shouldShowConfigure(Player player){
            return true;
        }

        @Override
        public void buildConfiguration(Table table) {
            var group = new ButtonGroup<ImageButton>();
            group.setMinCheckCount(1);
            ImageButton button1 = table.button(Icon.logic, Styles.clearNoneTogglei, 40f, () -> {
                configure(DoorMode.AUTO);
                deselect();
            }).tooltip(Core.bundle.get("door-mode.auto.name")).group(group).get();
            button1.update(() -> button1.setChecked(this.mode == DoorMode.AUTO));
            ImageButton button2 = table.button(Icon.effect, Styles.clearNoneTogglei, 40f, () -> {
                if(this.mode == DoorMode.MANUAL) configure(!this.open);
                configure(DoorMode.MANUAL);
                deselect();
            }).tooltip(Core.bundle.get("door-mode.manual.name")).group(group).get();
            button2.update(() -> button2.setChecked(this.mode == DoorMode.MANUAL));
        }

        public DoorExtendedBuild origin(){
            return chained.isEmpty() ? this : (chained.first() instanceof DoorExtendedBuild) ? (DoorExtendedBuild)chained.first(): this;
        }
    }

    public enum DoorMode{
        AUTO,
        MANUAL;
    }

}
