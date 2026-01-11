package tantros.type.production;

import arc.Core;
import arc.math.geom.Geometry;
import arc.scene.ui.layout.Table;
import arc.struct.ObjectIntMap;
import arc.util.Strings;
import arc.util.Tmp;
import mindustry.game.Team;
import mindustry.graphics.Pal;
import mindustry.type.Item;
import mindustry.type.ItemStack;
import mindustry.ui.Bar;
import mindustry.world.Tile;
import mindustry.world.blocks.environment.Floor;
import mindustry.world.blocks.environment.StaticWall;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatUnit;
import mindustry.world.meta.StatValues;
import mindustry.world.meta.Stats;
import tantros.type.Resource;
import tantros.type.blockState.BoreDrillConfig;
import tantros.type.buildingState.LaserState;
import tantros.world.blocks.production.ProductionBlock;
import tantros.world.meta.TantrosStats;

import static mindustry.Vars.*;

public class ProduceWallOre extends Produce{

    public BoreDrillConfig config;

    public static final ObjectIntMap<Item> tempOreCount = new ObjectIntMap<>();
    public static final Resource tempResource = new Resource();


    @Override
    public boolean outputsItems() {
        return true;
    }

    @Override
    public Resource output(ProductionBlock.ProductionBuild build) {
        BoreDrillConfig boreConfig = ((ProductionBlock)build.block).getBlockState(BoreDrillConfig.class);
        LaserState laserState = build.getState(LaserState.class);

        tempOreCount.clear();
        tempResource.clear();

        for(Tile other : laserState.facing){
            if(boreConfig.canMine(other)){
                tempOreCount.increment(boreConfig.getDrop(other), 0, 1);
            }
        }

        for(ObjectIntMap.Entry<Item> entry: tempOreCount){
            tempResource.withItems(new ItemStack(entry.key, entry.value));
        }

        return tempResource;
    }

    public ProduceWallOre(int tier, int range){
        super();
        config = new BoreDrillConfig(tier,range);
    }

    @Override
    public void apply(ProductionBlock block) {
        block.hasItems = true;

        block.rotate = true;
        block.putBlockState(config);
    }

    @Override
    public void applyToBuild(ProductionBlock block, ProductionBlock.ProductionBuild build) {
        super.applyToBuild(block, build);
        BoreDrillConfig boreConfig = block.getBlockState(BoreDrillConfig.class);
        LaserState laserState = new LaserState();
        laserState.range = config.range;
        laserState.oreCondition=boreConfig::canMine;
        build.putState(laserState);
    }

    @Override
    public void build(ProductionBlock.ProductionBuild build, Table table) {

    }

    @Override
    public void trigger(ProductionBlock.ProductionBuild build) {
        LaserState laserState = build.getState(LaserState.class);
        for(Tile tile : laserState.facing){
            Item drop = tile == null ? null : tile.wallDrop();
            if(build.items.total() < build.block.itemCapacity && drop != null){
                build.items.add(drop, 1);
                build.produced(drop);
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
        stats.add(Stat.drillTier, StatValues.drillables(block.productionTime, 0f, block.size, config.drillMultipliers, b ->
                (b instanceof Floor f && f.wallOre && f.itemDrop != null && f.itemDrop.hardness <= config.tier && (config.blockedItems == null || !config.blockedItems.contains(f.itemDrop))) ||
                        (b instanceof StaticWall w && w.itemDrop != null && w.itemDrop.hardness <= config.tier && (config.blockedItems == null || !config.blockedItems.contains(w.itemDrop)))));

        stats.add(Stat.drillSpeed, 60f / block.productionTime * block.size, StatUnit.itemsSecond);
        stats.add(TantrosStats.boreRange, config.range, StatUnit.blocks);
    }

    @Override
    public boolean placementAllowed(ProductionBlock block, Tile tile, Team team, int rotation) {
        BoreDrillConfig boreConfig = block.getBlockState(BoreDrillConfig.class);
        for(int i = 0; i < block.size; i++){
            block.nearbySide(tile.x, tile.y, rotation, i, Tmp.p1);
            for(int j = 0; j < config.range; j++){
                Tile other = world.tile(Tmp.p1.x + Geometry.d4x(rotation)*j, Tmp.p1.y + Geometry.d4y(rotation)*j);
                if(other != null && other.solid()){
                    if(boreConfig.canMine(other)){
                        return true;
                    }
                    break;
                }
            }
        }

        return false;
    }

    @Override
    public void setBars(ProductionBlock block) {
        block.addBar("drillspeed", (ProductionBlock.ProductionBuild e) ->
                new Bar(() -> Core.bundle.format("bar.drillspeed", Strings.fixed(60 / e.currentProductionTime * e.timeScale() * e.block.size, 2)), () -> Pal.ammo, () -> e.warmup));
    }
}
