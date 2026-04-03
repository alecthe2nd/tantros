package tantros.type.production;

import mindustry.Vars;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatUnit;
import mindustry.world.meta.Stats;
import tantros.type.Resource;
import tantros.type.blockConfig.HeatProductionConfig;
import tantros.type.buildingState.OutputHeatState;
import tantros.world.blocks.production.ProductionBlock;

public class ProduceHeat extends Produce{

    HeatProductionConfig productionConfig;

    Resource cache = new Resource();

    public ProduceHeat(HeatProductionConfig productionConfig){
        this.productionConfig = productionConfig;
    }

    @Override
    public Resource output(ProductionBlock.ProductionBuild build) {
        cache.clear();
        OutputHeatState outputHeat = build.getState(OutputHeatState.class);
        if(outputHeat == null) return cache;
        cache.withHeat(outputHeat.heat);
        return cache;
    }

    @Override
    public void apply(ProductionBlock block) {
        block.putBlockConfig(productionConfig);
        block.stateSources.add(OutputHeatState::new);
    }

    @Override
    public void trigger(ProductionBlock.ProductionBuild build) {

    }

    @Override
    public void update(ProductionBlock.ProductionBuild build) {
        OutputHeatState outputHeat = build.getState(OutputHeatState.class);
        if(outputHeat == null) return;
        outputHeat.heat = productionConfig.heatOutput * build.efficiency;
        for(int i = 0; i < 4; i++){
            outputHeat.sideHeat[i] = productionConfig.sideOutputs[i] * outputHeat.heat;
        }
    }

    @Override
    public boolean canCraft(ProductionBlock.ProductionBuild build) {
        return true;
    }

    @Override
    public void display(Stats stats, ProductionBlock block) {

        stats.add(Stat.output, productionConfig.heatOutput, StatUnit.heatUnits);
    }
}
