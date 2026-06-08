package tantros.type.effect;

import tantros.type.blockUtil.OnDestroyExplosionContext;
import tantros.type.buildingState.BoilerPressureBuildup;
import tantros.world.blocks.BlockExtended;

public class PressureExplosionOnDestruction implements BlockEffect{

    public float pressureExplosiveness = 1;

    public PressureExplosionOnDestruction(float explosiveness){
        this.pressureExplosiveness = explosiveness;
    }

    @Override
    public void update(BlockExtended.BuildExtended build) {

    }

    public void onDestroyedExplosion(BlockExtended.BuildExtended build, OnDestroyExplosionContext explosionContext){
        BoilerPressureBuildup buildup = build.getState(BoilerPressureBuildup.class);
        if(buildup == null) return;
        explosionContext.explosiveness *= 1 + (pressureExplosiveness * (buildup.pressureFrac()));
        explosionContext.radius *= (buildup.pressureFrac());
    }
}
