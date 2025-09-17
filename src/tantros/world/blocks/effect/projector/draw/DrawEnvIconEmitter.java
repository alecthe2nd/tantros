package tantros.world.blocks.effect.projector.draw;

import arc.graphics.g2d.Draw;
import mindustry.type.Liquid;
import tantros.world.blocks.effect.GenericProjector;
import tantros.world.blocks.effect.projector.EnvEmitter;
import tantros.world.environment.LocalEnv;

public class DrawEnvIconEmitter implements DrawEmitter<EnvEmitter> {
    @Override
    public void drawSelect(EnvEmitter emitter, GenericProjector.GenericProjectorBuild build) {
        LocalEnv emittedEnv = emitter.env(build);
        if(emittedEnv == null || emittedEnv.liquids.isEmpty()) return;
        Liquid liq = emittedEnv.liquids.first();
        Draw.rect(liq.fullIcon, build.x, build.y);
    }

    @Override
    public void draw(EnvEmitter emitter, GenericProjector.GenericProjectorBuild build) {

    }
}
