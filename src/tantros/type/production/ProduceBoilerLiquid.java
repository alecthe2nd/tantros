package tantros.type.production;

import mindustry.content.StatusEffects;
import mindustry.type.LiquidStack;
import mindustry.type.StatusEffect;
import tantros.type.blockConfig.BoilerConfig;
import tantros.type.buildingState.BoilerPressureBuildup;
import tantros.world.blocks.production.ProductionBlock;

public class ProduceBoilerLiquid extends ProduceLiquid{

    public BoilerConfig config = new BoilerConfig();

    public ProduceBoilerLiquid(LiquidStack output) {
        super(output);
    }

    @Override
    public void apply(ProductionBlock block) {
        super.apply(block);
        block.putBlockConfig(config);
        block.stateSources.add(BoilerPressureBuildup::new);
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
