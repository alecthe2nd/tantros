package tantros.content.world.blocks.effect.projector.draw;

import tantros.content.world.blocks.effect.GenericProjector.GenericProjectorBuild;
import tantros.content.world.blocks.effect.projector.ProjectorEmitter;

public interface DrawEmitter<E extends ProjectorEmitter<E>> {

    void drawSelect(E emitter, GenericProjectorBuild build);

    void draw(E emitter, GenericProjectorBuild build);
}
