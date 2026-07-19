package tantros.type.production;

import arc.Core;
import arc.math.Mathf;
import arc.struct.ObjectFloatMap;
import arc.struct.ObjectIntMap;
import arc.util.Strings;
import mindustry.graphics.Pal;
import mindustry.type.Item;
import mindustry.type.ItemStack;
import mindustry.ui.Bar;
import mindustry.world.meta.Stats;
import tantros.type.Resource;
import tantros.type.buildConfig.DrillConfig;
import tantros.type.buildingState.drills.FloorOreState;
import tantros.world.blocks.production.ProductionBlock;

public class ProduceOre extends Produce{

    public DrillConfig config;

    public static final Resource tempResource = new Resource();

    /** Multipliers of drill speed for each item. Defaults to 1. */
    public ObjectFloatMap<Item> drillMultipliers = new ObjectFloatMap<>();

    public ProduceOre(DrillConfig config){
        this.config = config;
    }

    @Override
    public boolean outputsItems() {
        return true;
    }

    @Override
    public Resource output(ProductionBlock.ProductionBuild build) {
        tempResource.clear();

        FloorOreState state = build.getState(FloorOreState.class, config.oreStateName);
        if(config.onlyDrillsDominantItems){
            tempResource.withItems(new ItemStack(state.dominantOre, state.totalDominantOres));
        }else{
            for(ObjectIntMap.Entry<Item> entry : state.oreCount.entries()){
                tempResource.withItems(new ItemStack(entry.key, entry.value));
            }
        }
        return tempResource;
    }

    @Override
    public float productionTimeMultiplier(ProductionBlock.ProductionBuild build) {
        Resource out = output(build);
        int itemSum = 0;
        float timeSum = 0;
        for(ItemStack stack: out.items){
            itemSum += stack.amount;
            timeSum += config.hardnessMultiplier * stack.item.hardness * stack.amount / drillMultipliers.get(stack.item, 1f);
        }

        return (1 + (timeSum / itemSum / build.getBlock().productionTime))/itemSum;
    }

    @Override
    public void apply(ProductionBlock block) {
        block.hasItems = true;
    }

    @Override
    public void trigger(ProductionBlock.ProductionBuild build) {
        FloorOreState state = build.getState(FloorOreState.class, config.oreStateName);

        if(config.onlyDrillsDominantItems){
            build.offload(state.dominantOre);
        } else {
            int step = Mathf.random(0,state.totalOres - 1);
            int index = 0;
            for(ObjectIntMap.Entry<Item> entry : state.oreCount.entries()){
                if(step >= index  && step < entry.value + index){
                    build.offload(entry.key);
                    break;
                } else {
                    index += entry.value;
                }
            }
        }
    }

    @Override
    public void update(ProductionBlock.ProductionBuild build) {
    }

    @Override
    public boolean canCraft(ProductionBlock.ProductionBuild build) {
        Resource out = output(build);
        for(var output : out.items){
            if(!dumpExcessItems && build.items.get(output.item) + 1 > build.block.itemCapacity){
                return false;
            }
        }
        return build.enabled;
    }

    @Override
    public void display(Stats stats, ProductionBlock block) {
    }

    @Override
    public void setBars(ProductionBlock block) {
        block.addBar("drillspeed", (ProductionBlock.ProductionBuild e) ->
                new Bar(() -> Core.bundle.format("bar.drillspeed", Strings.fixed(60 / e.currentProductionTime * e.timeScale(), 2)), () -> Pal.ammo, () -> e.warmup));
    }
}
