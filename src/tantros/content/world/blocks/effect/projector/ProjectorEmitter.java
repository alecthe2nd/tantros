package tantros.content.world.blocks.effect.projector;

import arc.math.geom.Position;
import mindustry.world.blocks.ExplosionShield;
import tantros.content.world.blocks.effect.GenericProjector.GenericProjectorBuild;
import tantros.content.world.blocks.effect.projector.draw.DrawEmitter;

public abstract class ProjectorEmitter<E extends ProjectorEmitter<E>> implements ExplosionShield {

    public float range = 0f;

    public DrawEmitter<E> drawer;

    abstract void effect(GenericProjectorBuild projector);

    public float range(GenericProjectorBuild projector){
        return range * projector.efficiency;
    };

    public boolean inRange(GenericProjectorBuild projector, Position pos){
        return projector.dst(pos) <= this.range(projector);
    }

    public void drawSelect(GenericProjectorBuild projector){
        drawer.drawSelect(self(), projector);
    }

    public void draw(GenericProjectorBuild projector){
        drawer.draw(self(), projector);
    }


    public abstract E self();

    @Override
    public boolean absorbExplosion(float x, float y, float damage) {
        return false;
    }
}
