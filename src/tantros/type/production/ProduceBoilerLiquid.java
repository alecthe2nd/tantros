package tantros.type.production;

import mindustry.type.LiquidStack;
import tantros.type.blockConfig.BoilerConfig;
import tantros.type.buildingState.BoilerPressureBuildup;
import tantros.world.blocks.production.ProductionBlock;

public class ProduceBoilerLiquid extends ProduceLiquid{

    public BoilerConfig config = new BoilerConfig();

    public ProduceBoilerLiquid(LiquidStack output) {
        super(output);
        ignoreLiquidFullness = true;
    }

    @Override
    public void apply(ProductionBlock block) {
        super.apply(block);
        block.putBlockConfig(config);
        block.postStateRequest(BoilerPressureBuildup::new, "ProducedPressureBuildup");
    }

    @Override
    public void update(ProductionBlock.ProductionBuild build) {
        super.update(build);
        BoilerPressureBuildup buildup = build.getState(BoilerPressureBuildup.class);
        if(buildup.pressure >= config.pressureCapacity){
            build.damage(config.pressureDamage);
        }
    }
}
