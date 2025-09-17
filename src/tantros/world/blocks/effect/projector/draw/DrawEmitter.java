package tantros.world.blocks.effect.projector.draw;

import tantros.world.blocks.effect.GenericProjector.GenericProjectorBuild;
import tantros.world.blocks.effect.projector.ProjectorEmitter;

public interface DrawEmitter<E extends ProjectorEmitter<E>> {

    void drawSelect(E emitter, GenericProjectorBuild build);

    void draw(E emitter, GenericProjectorBuild build);
}
