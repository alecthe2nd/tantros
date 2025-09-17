package tantros.world.blocks.effect.projector;

import arc.graphics.Color;
import arc.math.geom.Intersector;
import arc.math.geom.Position;
import arc.util.Nullable;
import tantros.world.blocks.effect.GenericProjector.GenericProjectorBuild;
import tantros.world.environment.LocalEnv;

public class EnvEmitter extends FieldEmitter<EnvEmitter> {

    public LocalEnv env;

    public boolean init;

    private LocalEnv tmpEnv = new LocalEnv();

    public int sides = 4;

    public float fieldRotation = 0f;

    @Override
    public void effect(GenericProjectorBuild projector) {

    }

    public @Nullable LocalEnv env(GenericProjectorBuild build){
        if(env != null){
            tmpEnv.replaceWith(env);
        } else if (build.block.hasLiquids) {
            tmpEnv.liquids.clear();
            build.liquids.each((liquid, amount) -> {
                tmpEnv.liquids.addUnique(liquid);
            });
        } else{
            tmpEnv = null;
        }
        return tmpEnv;
    }

    @Override
    public EnvEmitter self() {
        return this;
    }

    @Override
    public float maxRange() {
        return range;
    }

    @Override
    public Color color(GenericProjectorBuild build) {
        LocalEnv emittedEnv = env(build);
        if (emittedEnv != null){
            return emittedEnv.computeColor();
        }
        return Color.white;
    }

    @Override
    public int sides(GenericProjectorBuild build) {
        return sides;
    }

    @Override
    public float fieldRotation(GenericProjectorBuild build) {
        return fieldRotation;
    }

    @Override
    public float flash(GenericProjectorBuild build) {
        return 1 - build.efficiency;
    }

    @Override
    public boolean inRange(GenericProjectorBuild projector, Position pos) {
        return Intersector.isInRegularPolygon(sides(projector), projector.x, projector.y, range(projector), fieldRotation(projector), pos.getX(), pos.getY());
    }

    @Override
    public float range(GenericProjectorBuild projector) {
        return super.range(projector) * projector.warmup();
    }
}
