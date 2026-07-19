package tantros.type.production;

import mindustry.world.meta.Stats;
import tantros.type.Resource;
import tantros.type.buildConfig.FlywheelConfig;
import tantros.type.buildingState.FlywheelProgressState;
import tantros.world.blocks.production.ProductionBlock;

public class ProducePowerFlywheel extends ProducePower{

    public FlywheelConfig config;

    public ProducePowerFlywheel(float maxPower) {
        this(maxPower, new FlywheelConfig());
    }

    public ProducePowerFlywheel(float maxPower, FlywheelConfig config){
        super(maxPower);
        this.config = config;
    }

    @Override
    public void apply(ProductionBlock block) {
        super.apply(block);
        block.putBlockConfig(config);
        config.flywheelStateName = block.postStateRequest(()->new FlywheelProgressState(config), "FlywheelPowerProgress");
    }

    @Override
    public void updateAlways(ProductionBlock.ProductionBuild build) {
        FlywheelProgressState state = build.getState(FlywheelProgressState.class, config.flywheelStateName);
        super.updateAlways(build);
        build.powerProductionEfficiency = state.flywheelSpeed;
    }
}
