package tantros.type.effect;

import arc.graphics.Color;
import arc.math.Angles;
import arc.math.Mathf;
import mindustry.Vars;
import mindustry.content.Fx;
import tantros.world.blocks.BlockExtended;

public class RotoSweepFXEffect implements BlockEffect {

    public int tines = 4;
    public int tineLength = 8;
    public int particleRad = 1;
    public float rotationScl = 12;
    public float rotationOffset = 45;

    @Override
    public void update(BlockExtended.BuildExtended build) {
        float warmup = build.warmup();
        int particles = tineLength / (particleRad * 2);
        float time = build.totalProgress();

        if(warmup > 0 && !Vars.state.isPaused()){
            for(int tine = 0; tine < tines; tine++){
                if(Mathf.randomBoolean(0.02f)) {
                    float angle = (time * rotationScl + (360f / tines) * tine + rotationOffset) % 360;

                    for (int particle = 0; particle < particles; particle++) {
                        float len = particle * (particleRad) * 2 + particleRad;
                        Fx.mine.at(build.x + Angles.trnsx(angle, len), build.y + Angles.trnsy(angle, len), angle + 90, Color.blue);

                    }
                }

            }
        }
    }
}
