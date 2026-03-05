package tantros.world.blocks.environment;

import arc.Core;
import arc.graphics.g2d.Draw;
import arc.math.Angles;
import arc.math.Mathf;
import arc.math.Rand;
import arc.math.geom.Vec2;
import arc.util.Time;
import mindustry.Vars;
import mindustry.graphics.Layer;
import mindustry.world.Tile;
import mindustry.world.blocks.environment.SeaBush;
import tantros.graphics.DrawPsuedoParrallax;

public class TallSeaBush extends SeaBush {

    static Rand rand = new Rand();

    static Vec2 tempVec2 = new Vec2();

    private int levels = 3;

    public float spacing = Vars.tilesize;

    public float parallaxRange = 20;

    public int levelsMin = 3,
            aboveBridges = 1,
            tallestLevels = 5,
            levelsMax = 7;

    public TallSeaBush(String name) {
        super(name);
        layer = Layer.blockProp + 1;
    }

    public void drawLobes(Tile tile, int level){
        rand.setSeed(tile.pos() + level);

        float offset = rand.random(180f);
        float height = levels * this.spacing;
        float newDist = (Mathf.pow(Mathf.clamp(tempVec2.set(Core.camera.position).dst(tile)/(height * parallaxRange)) - 1,3) + 1) * spacing * level;
        tempVec2.set(Core.camera.position).sub(tile).nor().scl(-newDist);
        float stemX = DrawPsuedoParrallax.xHeight(tile.getX(), level * Vars.tilesize);
        float stemY = DrawPsuedoParrallax.yHeight(tile.getY(),level * Vars.tilesize);



        int lobes = rand.random(lobesMin, lobesMax);
        for(int i = 0; i < lobes; i++){
            float ba =  i / (float)lobes * 360f + offset + rand.range(spread), angle = ba + Mathf.sin(Time.time + rand.random(0, timeRange), rand.random(sclMin, sclMax), rand.random(magMin, magMax));
            float w = region.width * region.scl(), h = region.height * region.scl();
            var region = Angles.angleDist(ba, 225f) <= botAngle ? botRegion : this.region;

            Draw.rect(region,
                    stemX,
                    stemY,
                    w, h,
                    origin*4f, h/2f,
                    angle
            );
        }

        if(centerRegion.found()){
            Draw.rect(centerRegion, tile.worldx(), tile.worldy());
        }
    }

    @Override
    public void drawBase(Tile tile){
        float lastLayer = Draw.z();
        rand.setSeed(tile.pos());
        levels = rand.random(levelsMin, levelsMax);

        for(int i = 0; i < levels; i++){
            if(i >= tallestLevels){
                Draw.z(Layer.flyingUnit + 1f);
            } else if(i >= aboveBridges) {
                Draw.z(Layer.power + 0.1f);
            } else {
                Draw.z(layer);
            }
            drawLobes(tile, i);
        }
        Draw.z(layer);

        if(centerRegion.found()){
            Draw.rect(centerRegion, tile.worldx(), tile.worldy());
        }
        Draw.z(lastLayer);
    }
}
