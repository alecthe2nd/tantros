package tantros.content.world.blocks.effect.projector.draw;

import arc.graphics.g2d.Draw;
import mindustry.type.Liquid;
import tantros.content.world.blocks.effect.GenericProjector;
import tantros.content.world.blocks.effect.projector.EnvEmitter;

public class DrawEnvIconEmitter implements DrawEmitter<EnvEmitter> {
    @Override
    public void drawSelect(EnvEmitter emitter, GenericProjector.GenericProjectorBuild build) {
        if(emitter.env == null) return;
        Liquid liq = emitter.env.liquids.first();
        if (liq == null) return;
        Draw.rect(liq.fullIcon, build.x, build.y);
    }

    @Override
    public void draw(EnvEmitter emitter, GenericProjector.GenericProjectorBuild build) {

    }
}
