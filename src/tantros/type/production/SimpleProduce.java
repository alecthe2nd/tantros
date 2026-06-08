package tantros.type.production;

import arc.scene.ui.layout.Table;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatUnit;
import mindustry.world.meta.Stats;
import tantros.type.Resource;
import tantros.world.blocks.production.ProductionBlock;

public class SimpleProduce extends Produce {

    public Resource output;

    public SimpleProduce(Resource output){
        this.output = output;
    }

    @Override
    public boolean outputsItems() {
        return !output.items.isEmpty();
    }

    @Override
    public Resource output(ProductionBlock.ProductionBuild build) {
        return output;
    }

    @Override
    public void apply(ProductionBlock block) {

        if(!output.items.isEmpty()) {
            block.hasItems = true;
        }

        if(!output.liquids.isEmpty()) {
            block.hasLiquids = true;
            block.outputsLiquid = true;
        }

        if(output.power > 0){
            block.hasPower = true;
            block.outputsPower = true;
            block.powerProduction = this;
        }
    }

    @Override
    public void build(ProductionBlock.ProductionBuild build, Table table) {

    }

    @Override
    public void trigger(ProductionBlock.ProductionBuild build) {
        if(!isActive.get()) return;
        for(var output : output.items){
            for(int i = 0; i < output.amount; i++){
                build.offload(output.item);
            }
        }
    }

    @Override
    public void update(ProductionBlock.ProductionBuild build) {
        if(!isActive.get()) return;
        //continuously output based on efficiency
        float inc = build.getProgressIncrease(1f);
        for(var output : output.liquids){
            build.handleLiquid(build, output.liquid, Math.min(output.amount * inc, build.block.liquidCapacity - build.liquids.get(output.liquid)));
        }
        build.powerProductionEfficiency = build.efficiency;
    }

    @Override
    public boolean canCraft(ProductionBlock.ProductionBuild build) {
        for(var output : output.items){
            if(!dumpExcessItems && build.items.get(output.item) + output.amount > build.block.itemCapacity){
                return false;
            }
        }

        if(!ignoreLiquidFullness){
            boolean allFull = true;
            for(var output : output.liquids){
                if(build.liquids.get(output.liquid) >= build.block.liquidCapacity - 0.001f){
                    if(dumpExcessLiquids){
                        return false;
                    }
                }else{
                    //if there's still space left, it's not full for all liquids
                    allFull = false;
                }
            }

            //if there is no space left for any liquid, it can't reproduce
            //only relevant if liquids are being outputted
            if(allFull && !output.liquids.isEmpty()){
                return false;
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
        float scaling = 1f, max = 1f;
        if(!output.liquids.isEmpty()){
            max = 0f;
            for(var s : output.liquids){
                float value = (build.block.liquidCapacity - build.liquids.get(s.liquid)) / (s.amount * build.edelta());
                scaling = Math.min(scaling, value);
                max = Math.max(max, value);
            }
        }

        //when dumping excess take the maximum value instead of the minimum.
        return (dumpExcessLiquids ? Math.min(max, 1f) : scaling);
    }


    @Override
    public void display(Stats stats, ProductionBlock block) {
        if(!output.items.isEmpty()){
            stats.add(Stat.productionTime, block.productionTime / 60f, StatUnit.seconds);
        }
    }
}
