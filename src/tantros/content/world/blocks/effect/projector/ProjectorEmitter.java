package tantros.content.world.blocks.effect.projector;

import arc.math.geom.Position;
import mindustry.world.blocks.ExplosionShield;
import mindustry.world.meta.BlockFlag;
import tantros.content.world.blocks.effect.GenericProjector.GenericProjectorBuild;
import tantros.content.world.blocks.effect.projector.draw.DrawEmitter;

import arc.struct.EnumSet;

public abstract class ProjectorEmitter<E extends ProjectorEmitter<E>> implements ExplosionShield {

    public float range = 0f;

    public EnumSet<BlockFlag> flags = EnumSet.of();

    public DrawEmitter<E> drawer;

    public abstract void effect(GenericProjectorBuild projector);

    public float range(GenericProjectorBuild projector){
        return range;
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

    public abstract float maxRange();
}
