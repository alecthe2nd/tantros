package tantros.type.effect;

import arc.struct.Seq;
import mindustry.game.Team;
import mindustry.world.Tile;
import mindustry.world.blocks.environment.Floor;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatUnit;
import mindustry.world.meta.StatValues;
import tantros.type.buildConfig.DrillConfig;
import tantros.type.buildingState.drills.FloorOreState;
import tantros.type.production.ProduceOre;
import tantros.world.blocks.BlockExtended;
import tantros.world.blocks.production.ProductionBlock;

import static mindustry.Vars.indexer;
import static mindustry.Vars.state;

public class DrillsFloorOres implements BlockEffect {

    public DrillConfig drillConfig;

    public static final Seq<Tile> tempTiles = new Seq<>();

    public DrillsFloorOres(DrillConfig drillConfig){
        this.drillConfig = drillConfig;
    }

    @Override
    public void apply(BlockExtended block) {
        if(! (block instanceof ProductionBlock prod) ) throw new IllegalArgumentException("DrillsFloorOres can only set stats to Production Blocks.");
        prod.putBlockConfig(drillConfig);
        prod.hasItems = true;
        prod.produce(new ProduceOre(drillConfig));
        drillConfig.oreStateName = prod.postStateRequest(()->new FloorOreState(drillConfig), "DrillsFloor");
    }

    @Override
    public boolean canBeAppliedTo(BlockExtended block) {
        return BlockEffect.super.canBeAppliedTo(block) && block instanceof ProductionBlock;
    }

    @Override
    public void update(BlockExtended.BuildExtended build) {

    }

    @Override
    public void setStats(BlockExtended block) {
        if(! (block instanceof ProductionBlock prod) ) throw new IllegalArgumentException("DrillsFloorOres can only set stats to Production Blocks.");
        block.stats.add(Stat.drillTier, StatValues.drillables(prod.productionTime, drillConfig.hardnessMultiplier, block.size * block.size, drillConfig.drillMultipliers, b ->
                b instanceof Floor f
                && !f.wallOre
                && f.itemDrop != null
                && drillConfig.drillable(f.itemDrop)
                && (indexer.isBlockPresent(f) || state.isMenu())));

        block.stats.add(Stat.drillSpeed, 60f / prod.productionTime * block.size * block.size, StatUnit.itemsSecond);
    }

    @Override
    public boolean placementAllowed(BlockExtended block, Tile tile, Team team, int rotation) {
        if(block.isMultiblock()){
            for(Tile other : tile.getLinkedTilesAs(block, tempTiles)){
                return drillConfig.canMine(other);
            }
            return false;
        }else{
            return drillConfig.canMine(tile);
        }
    }
}
