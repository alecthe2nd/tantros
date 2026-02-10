package tantros.type.effect;

import tantros.type.blockConfig.BoilerConfig;
import tantros.type.blockUtil.OnDestroyExplosionContext;
import tantros.type.buildingState.BoilerPressureBuildup;
import tantros.world.blocks.BlockExtended;

public class Boiler implements BlockEffect{

    public BoilerConfig config = new BoilerConfig();

    @Override
    public void update(BlockExtended.BuildExtended build) {
        BoilerPressureBuildup buildup = build.getState(BoilerPressureBuildup.class);
        if(buildup.pressure >= config.pressureCapacity){
            build.damage(config.pressureDamage);
        }
    }

    @Override
    public void apply(BlockExtended block) {
        BlockEffect.super.apply(block);
        block.putBlockConfig(config);
        block.stateSources.add(BoilerPressureBuildup::new);
    }

    public void onDestroyedExplosion(BlockExtended.BuildExtended build, OnDestroyExplosionContext explosionContext){
        BoilerPressureBuildup buildup = build.getState(BoilerPressureBuildup.class);
        explosionContext.explosiveness *= 1 + (config.pressureExplosiveness * (buildup.pressure / config.pressureCapacity));
        explosionContext.radius *= (buildup.pressure / config.pressureCapacity);
    }
}
