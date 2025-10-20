package tantros.type.production;

import arc.scene.ui.layout.Table;
import mindustry.type.ItemStack;
import mindustry.world.meta.Stats;
import tantros.type.Resource;
import tantros.world.blocks.production.ProductionBlock;

public class ProduceItem extends Produce {

    public ItemStack output;

    private static final Resource tempResource = new Resource();

    public ProduceItem(ItemStack stack){
        if(stack == null){
            throw new IllegalArgumentException("ProduceItem must be given an item stack to produce.");
        }
        this.output = stack;
    }

    @Override
    public boolean outputsItems() {
        return true;
    }

    @Override
    public Resource output(ProductionBlock.ProductionBuild build) {
        if(!isActive.get()) return Resource.none;
        tempResource.clear();
        tempResource.withItems(output);
        return tempResource;
    }

    @Override
    public void apply(ProductionBlock block) {
        block.hasItems = true;
    }

    @Override
    public void build(ProductionBlock.ProductionBuild build, Table table) {

    }

    @Override
    public void trigger(ProductionBlock.ProductionBuild build) {
        if(!isActive.get()) return;
        for(int i = 0; i < output.amount; i++){
            build.offload(output.item);
        }
    }

    @Override
    public void update(ProductionBlock.ProductionBuild build) {
        if(!isActive.get()) return;
    }

    @Override
    public boolean canCraft(ProductionBlock.ProductionBuild build) {
        if(!isActive.get()) return true;
        if(!dumpExcessItems && build.items.get(output.item) + output.amount > build.block.itemCapacity){
            return false;
        }
        return build.enabled;
    }

    @Override
    public float progressLimit(ProductionBlock.ProductionBuild build) {
        if(!isActive.get()) return 1;

        //limit progress increase by maximum amount of items it can produce
        float value = (build.block.itemCapacity - build.items.get(output.item)) / (output.amount * build.edelta());

        //when dumping excess progress is not limited by this producer.
        return (dumpExcessLiquids ? 1 : Math.max(value, 1f));
    }

    @Override
    public void display(Stats stats, ProductionBlock block) {

    }
}
