package tantros.world.consumers;

import mindustry.gen.Building;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatUnit;
import mindustry.world.meta.Stats;
import tantros.type.blockConfig.HeatConsumptionConfig;
import tantros.type.buildingState.InputHeatState;
import tantros.world.blocks.BlockExtended;

public class ConsumeHeat extends ExtendedConsume {

    public HeatConsumptionConfig config;

    public ConsumeHeat(HeatConsumptionConfig config) {
        this.config = config;
    }

    @Override
    public void apply(BlockExtended block) {
        block.stateSources.add(InputHeatState::new);
        block.putBlockConfig(config);
    }

    @Override
    public void display(Stats stats) {
        stats.add(Stat.input, config.heatPerEfficiency, StatUnit.heatUnits);
        stats.add(Stat.maxEfficiency, (int)(config.computeEfficiency(config.maximumHeat) * 100f), StatUnit.percent);
    }

    @Override
    public float efficiency(BlockExtended.BuildExtended build) {
        InputHeatState input = build.getState(InputHeatState.class);
        return config.computeEfficiency(input.heat);
    }

    @Override
    public float efficiencyMultiplier(BlockExtended.BuildExtended build) {
        InputHeatState input = build.getState(InputHeatState.class);
        return config.computeEfficiency(input.heat);
    }
}
