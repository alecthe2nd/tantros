package tantros.world.blocks.effect.projector.draw;

import arc.struct.Seq;
import tantros.world.blocks.effect.GenericProjector;
import tantros.world.blocks.effect.projector.ProjectorEmitter;

public class DrawMultiEmitter<E extends ProjectorEmitter<E>> implements DrawEmitter<E> {

    public Seq<DrawEmitter<E>> drawers = new Seq<>();

    @Override
    public void drawSelect(E emitter, GenericProjector.GenericProjectorBuild build) {
        for(DrawEmitter<E> drawer: drawers){
            drawer.drawSelect(emitter, build);
        }
    }

    @Override
    public void draw(E emitter, GenericProjector.GenericProjectorBuild build) {
        for(DrawEmitter<E> drawer: drawers){
            drawer.draw(emitter, build);
        }
    }
}
