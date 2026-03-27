package tantros.type.production;

import mindustry.world.meta.Stats;
import tantros.type.Resource;
import tantros.type.buildConfig.FlywheelConfig;
import tantros.type.buildingState.FlywheelProgressState;
import tantros.world.blocks.production.ProductionBlock;

public class ProducePowerFlywheel extends ProducePower{

    public FlywheelConfig config;

    public String stateName = "FlywheelProgress0";

    public ProducePowerFlywheel(float maxPower) {
        super(maxPower);
    }

    @Override
    public void apply(ProductionBlock block) {
        super.apply(block);
        if(!block.namedSources.containsKey(stateName) || block.namedSources.get(stateName) instanceof FlywheelProgressState) {
            block.namedSources.put(stateName, FlywheelProgressState::new);
        }
        if(block.blockConfigs.containsKey(FlywheelConfig.class)) {
            config = block.getBlockConfig(FlywheelConfig.class);
        } else {
            block.putBlockConfig(config = new FlywheelConfig());
        }
    }

    @Override
    public void updateAlways(ProductionBlock.ProductionBuild build) {
        FlywheelProgressState state = build.getState(FlywheelProgressState.class, stateName);
        super.updateAlways(build);
        build.powerProductionEfficiency = state.flywheelSpeed;
    }
}
