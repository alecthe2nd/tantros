package tantros.world.draw;

import arc.graphics.Blending;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.math.Angles;
import arc.math.Interp;
import arc.math.Mathf;
import arc.util.Time;
import mindustry.type.Item;
import tantros.type.blockConfig.DrillConfig;
import tantros.type.buildingState.drills.FloorOreState;
import tantros.world.blocks.BlockExtended;
import tantros.world.draw.extended.DrawBlockExtended;

public class DrawSandVortex extends DrawBlockExtended {

    public int sides = 12;
    public float x = 0, y = 0;
    public float alpha = 0.5f;
    public int particles = 30;
    public float particleRotation = 0, particleLife = 70f, particleRad = 7f, particleSize = 3f, fadeMargin = 0.4f, rotateScl = 3f;
    public boolean reverse = false, poly = false;
    public Interp particleSizeInterp = Interp.slope;
    public Blending blending = Blending.normal;

    public DrillConfig config;

    public DrawSandVortex(DrillConfig config){
        this.config = config;
    }

    @Override
    public void draw(BlockExtended.BuildExtended build) {
        FloorOreState oreState = build.getState(FloorOreState.class, config.oreStateName);


        if(build.warmup() > 0f) {
            if(oreState.oreCount.size < 1) return;
            float a = alpha * build.warmup();

            Draw.blend(blending);

            float time = build.totalProgress();

            float base = Time.time / particleLife;
            rand.setSeed(build.id);
            for(int i = 0; i < particles; i++){
                float fin = (rand.random(2f) + base) % 1f;
                if(reverse) fin = 1f - fin;
                float angle = rand.random(360f) + (time / rotateScl) % 360f;
                float len = rand.random(particleRad);
                Item item = oreState.selectRandomOre(rand);
                if(item == null) continue;
                Draw.color(item.color);

                Draw.alpha(a * (1f - Mathf.curve(fin, 1f - fadeMargin)));
                if(poly){
                    Fill.poly(
                            build.x + x + Angles.trnsx(angle, len),
                            build.y + y + Angles.trnsy(angle, len),
                            sides,
                            particleSize * particleSizeInterp.apply(fin) * Mathf.clamp(build.warmup()),
                            particleRotation
                    );
                }else{
                    Fill.circle(
                            build.x + x + Angles.trnsx(angle, len),
                            build.y + y + Angles.trnsy(angle, len),
                            particleSize * particleSizeInterp.apply(fin) * build.warmup()
                    );
                }
            }

            Draw.blend();
            Draw.reset();
        }

    }

}
