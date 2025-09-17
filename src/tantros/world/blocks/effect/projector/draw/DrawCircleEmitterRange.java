package tantros.world.blocks.effect.projector.draw;

import arc.graphics.Color;
import mindustry.graphics.Drawf;
import tantros.world.blocks.effect.GenericProjector.GenericProjectorBuild;
import tantros.world.blocks.effect.projector.ProjectorEmitter;

public class DrawCircleEmitterRange<E extends ProjectorEmitter<E>> implements DrawEmitter<E> {

    public Color baseColor = Color.white;

    @Override
    public void drawSelect(E emitter, GenericProjectorBuild build) {
        float realRange = emitter.range(build);

        Drawf.dashCircle(build.x, build.y, realRange, baseColor);
    }

    @Override
    public void draw(E emitter, GenericProjectorBuild build) {

    }
}
