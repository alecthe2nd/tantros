package tantros.type.production;

import arc.Core;
import arc.math.Mathf;
import arc.math.Rand;
import arc.scene.ui.layout.Table;
import arc.struct.ObjectFloatMap;
import arc.struct.ObjectIntMap;
import arc.struct.Seq;
import arc.util.Nullable;
import arc.util.Strings;
import arc.util.Time;
import mindustry.graphics.Pal;
import mindustry.type.Item;
import mindustry.type.ItemStack;
import mindustry.ui.Bar;
import mindustry.world.Tile;
import mindustry.world.blocks.environment.Floor;
import mindustry.world.blocks.production.Drill;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatUnit;
import mindustry.world.meta.StatValues;
import mindustry.world.meta.Stats;
import tantros.type.Resource;
import tantros.world.blocks.production.Boiler;
import tantros.world.blocks.production.ProductionBlock;

import static mindustry.Vars.indexer;
import static mindustry.Vars.state;

public class ProduceOre extends Produce{

    /** Special exemption items that this drill can't mine. */
    public @Nullable Seq<Item> blockedItems;

    public int tier = 0;

    public float hardnessMultiplier = 50f;

    public static final Seq<Tile> tempTiles = new Seq<>();

    public static final ObjectIntMap<Item> tempOreCount = new ObjectIntMap<>();
    public static final Resource tempResource = new Resource();

    public static final Rand random = new Rand();

    /** Multipliers of drill speed for each item. Defaults to 1. */
    public ObjectFloatMap<Item> drillMultipliers = new ObjectFloatMap<>();

    public boolean canMine(Tile tile){
        if(tile == null || tile.block().isStatic()) return false;
        Item drops = tile.drop();
        return drops != null && drops.hardness <= tier && (blockedItems == null || !blockedItems.contains(drops));
    }

    public Item getDrop(Tile tile){
        return tile.drop();
    }

    @Override
    public boolean outputsItems() {
        return true;
    }

    @Override
    public Resource output(ProductionBlock.ProductionBuild build) {
        Tile tile = build.tile;

        tempOreCount.clear();
        tempResource.clear();

        for(Tile other : tile.getLinkedTilesAs(build.block, tempTiles)){
            if(canMine(other)){
                tempOreCount.increment(getDrop(other), 0, 1);
            }
        }

        for(ObjectIntMap.Entry<Item> entry: tempOreCount){
            tempResource.withItems(new ItemStack(entry.key, entry.value));
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
            timeSum += hardnessMultiplier * stack.item.hardness * stack.amount / drillMultipliers.get(stack.item, 1f);
        }

        return (1 + (timeSum / itemSum / build.getBlock().productionTime))/itemSum;
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
        Resource out = output(build);
        int itemSum = 0;
        for(ItemStack stack: out.items){
            itemSum += stack.amount;
        }
        random.setSeed(build.id);
        int step = (Mathf.floor(Time.time / build.currentProductionTime) + random.random(0,itemSum)) % itemSum;

        int index = 0;
        for(var output : out.items){
            if(index <= step && output.amount + index > step){
                build.offload(output.item);
                break;
            } else {
                index += output.amount;
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
    public float progressLimit(ProductionBlock.ProductionBuild build) {
        return 1;
    }

    @Override
    public void display(Stats stats, ProductionBlock block) {
        stats.add(Stat.drillTier, StatValues.drillables(block.productionTime, hardnessMultiplier, block.size * block.size, drillMultipliers, b -> b instanceof Floor f && !f.wallOre && f.itemDrop != null &&
                f.itemDrop.hardness <= tier && (blockedItems == null || !blockedItems.contains(f.itemDrop)) && (indexer.isBlockPresent(f) || state.isMenu())));

        stats.add(Stat.drillSpeed, 60f / block.productionTime * block.size * block.size, StatUnit.itemsSecond);
    }

    @Override
    public void setBars(ProductionBlock block) {
        block.addBar("drillspeed", (ProductionBlock.ProductionBuild e) ->
                new Bar(() -> Core.bundle.format("bar.drillspeed", Strings.fixed(60 / e.currentProductionTime * e.timeScale(), 2)), () -> Pal.ammo, () -> e.warmup));
    }
}
