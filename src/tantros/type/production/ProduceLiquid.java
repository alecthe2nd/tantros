package tantros.type.production;

import arc.math.Mathf;
import arc.scene.ui.layout.Table;
import mindustry.type.LiquidStack;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatValues;
import mindustry.world.meta.Stats;
import tantros.type.Resource;
import tantros.type.blockConfig.BoilerConfig;
import tantros.type.buildingState.BoilerPressureBuildup;
import tantros.world.blocks.production.ProductionBlock;

public class ProduceLiquid extends Produce{

    public LiquidStack output;

    protected Resource cachedOutput = new Resource();

    public ProduceLiquid(LiquidStack output){
        this.output = output;
        cachedOutput.withLiquids(output);
    }

    @Override
    public Resource output(ProductionBlock.ProductionBuild build) {
        return this.cachedOutput;
    }

    @Override
    public void apply(ProductionBlock block) {
        block.hasLiquids = true;
        block.outputsLiquid = true;
    }

    @Override
    public void build(ProductionBlock.ProductionBuild build, Table table) {

    }

    @Override
    public void trigger(ProductionBlock.ProductionBuild build) {

    }

    @Override
    public void update(ProductionBlock.ProductionBuild build) {
        if(!isActive.get()) return;
        //continuously output based on efficiency
        float inc = build.getProgressIncrease(1f);

        build.handleLiquid(build, output.liquid,output.amount * inc);
    }

    @Override
    public boolean canCraft(ProductionBlock.ProductionBuild build) {

        if(!ignoreLiquidFullness){
            if(build.liquids.get(output.liquid) >= build.block.liquidCapacity - 0.001f){
                if(!dumpExcessLiquids){
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public float progressLimit(ProductionBlock.ProductionBuild build) {
        if(ignoreLiquidFullness){
            return 1f;
        }

        //limit progress increase by maximum amount of liquid it can produce
        if(Mathf.equal(build.efficiency, 0)){
            return 0;
        }
        float scaling = 1f;
        float value = (build.block.liquidCapacity - build.liquids.get(output.liquid)) / (output.amount * build.edelta());
        scaling = Math.min(scaling, value);

        //when dumping excess take the maximum value instead of the minimum.
        return (dumpExcessLiquids ? 1 : scaling);
    }

    @Override
    public void display(Stats stats, ProductionBlock block) {
        stats.add(Stat.output, StatValues.liquids(1f, output));
    }

    @Override
    public void setBars(ProductionBlock block) {
        super.setBars(block);
        block.addLiquidBar(output.liquid);
    }
}
