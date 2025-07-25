package tantros.content.world.draw.wallDrill;

import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.graphics.g2d.TextureRegion;
import arc.math.Angles;
import arc.math.Mathf;
import arc.math.geom.Geometry;
import arc.math.geom.Point2;
import arc.util.Time;
import arc.util.Tmp;
import mindustry.gen.Building;
import mindustry.graphics.Drawf;
import mindustry.graphics.Layer;
import mindustry.world.Block;
import mindustry.world.Tile;
import mindustry.world.blocks.production.BeamDrill;
import mindustry.world.draw.DrawBlock;

import static mindustry.Vars.tilesize;

public class DrawDrillBit extends DrawBlock {

    public TextureRegion bit;

    public String suffix = "-bit";

    public DrawDrillBit(){

    }

    public DrawDrillBit(String suffix){
        this.suffix = suffix;
    }

    @Override
    public void draw(Building build) {
        super.draw(build);

        if (!(build instanceof BeamDrill.BeamDrillBuild drill && build.block instanceof BeamDrill block)) return;
        if(build.isPayload()) return;


        var dir = Geometry.d4(drill.rotation);
        int ddx = Geometry.d4x(drill.rotation + 1), ddy = Geometry.d4y(drill.rotation + 1);

        for(int i = 0; i < block.size; i++){
            Tile face = drill.facing[i];
            if(face != null){
                Point2 p = drill.lasers[i];
                float lx = face.worldx() - (dir.x/2f)*tilesize, ly = face.worldy() - (dir.y/2f)*tilesize;
                float lsx = (p.x - dir.x/2f) * tilesize, lsy = (p.y - dir.y/2f) * tilesize;

                float offset = ((drill.rotation > 1) ? -1f:  1f )* Mathf.absin(Time.time + i*5 + (drill.id % 9)*9, 6f, block.range);

                Draw.z(Layer.blockUnder);
                Draw.rect(bit, ((drill.rotation % 2 == 0 )? offset : 0f) + lsx,((drill.rotation % 2 == 1 )? offset : 0f) + lsy, drill.rotdeg());
                Draw.color();
                Draw.mixcol();

                Draw.z(Layer.effect);
                Lines.stroke(drill.warmup);
                rand.setState(i, drill.id);
                Color col = face.wallDrop().color;
                Color spark = Tmp.c3.set(block.sparkColor).lerp(block.boostHeatColor, drill.boostWarmup);

                for(int j = 0; j < block.sparks; j++){
                    float fin = (Time.time / block.sparkLife + rand.random(block.sparkRecurrence + 1f)) % block.sparkRecurrence;
                    float or = rand.range(2f);
                    Tmp.v1.set(block.sparkRange * fin, 0).rotate(drill.rotdeg() + rand.range(block.sparkSpread));

                    Draw.color(spark, col, fin);
                    float px = Tmp.v1.x, py = Tmp.v1.y;
                    if(fin <= 1f) Lines.lineAngle(lx + px + or * ddx, ly + py + or * ddy, Angles.angle(px, py), Mathf.slope(fin) * block.sparkSize);
                }

                Draw.blend();
                Draw.reset();
                Draw.z(Layer.block);
            }
        }
    }

    @Override
    public void load(Block block) {
        super.load(block);
        bit = Core.atlas.find(block.name + suffix);
    }
}
