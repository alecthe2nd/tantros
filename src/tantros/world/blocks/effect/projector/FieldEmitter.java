package tantros.world.blocks.effect.projector;

import arc.graphics.Color;
import tantros.world.blocks.effect.GenericProjector;


public abstract class FieldEmitter<E extends ProjectorEmitter<E>> extends ProjectorEmitter<E> {

    public abstract Color color(GenericProjector.GenericProjectorBuild build);

    public abstract int sides(GenericProjector.GenericProjectorBuild build);

    public abstract float fieldRotation(GenericProjector.GenericProjectorBuild build);

    public abstract float flash(GenericProjector.GenericProjectorBuild build);
}
