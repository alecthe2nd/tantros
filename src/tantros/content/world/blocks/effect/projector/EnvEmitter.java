package tantros.content.world.blocks.effect.projector;

import arc.graphics.Color;
import tantros.content.world.blocks.effect.GenericProjector.GenericProjectorBuild;
import tantros.world.environment.LocalEnv;

public class EnvEmitter extends FieldEmitter<EnvEmitter> {

    public LocalEnv env;

    public int sides = 4;

    @Override
    public void effect(GenericProjectorBuild projector) {

    }

    @Override
    public EnvEmitter self() {
        return this;
    }

    @Override
    public Color color(GenericProjectorBuild build) {
        if (env != null){
            return env.computeColor();
        }
        return Color.white;
    }

    @Override
    public int sides(GenericProjectorBuild build) {
        return sides;
    }

    @Override
    public float fieldRotation(GenericProjectorBuild build) {
        return 0;
    }

    @Override
    public float flash(GenericProjectorBuild build) {
        return 0;
    }
}
