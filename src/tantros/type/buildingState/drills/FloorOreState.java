package tantros.type.buildingState.drills;

import arc.struct.ObjectIntMap;
import arc.struct.Seq;
import arc.util.Nullable;
import mindustry.type.Item;
import mindustry.world.Tile;
import tantros.type.buildConfig.DrillConfig;
import tantros.type.buildingState.BuildingState;
import tantros.world.blocks.BlockExtended;

public class FloorOreState implements BuildingState {

    public DrillConfig config;

    public ObjectIntMap<Item> oreCount = new ObjectIntMap<>();

    public @Nullable Item dominantOre;

    public int totalOres = 0;

    public int totalDominantOres = 0;

    public static final Seq<Tile> tempTiles = new Seq<>();
    public static final Seq<Item> tempItems = new Seq<>();

    public FloorOreState(DrillConfig config){
        this.config = config;
    }

    @Override
    public void initState(BlockExtended ownerType, BlockExtended.BuildExtended owner) {
        oreCount.clear();
    }

    @Override
    public void update(BlockExtended ownerType, BlockExtended.BuildExtended owner) {

    }

    @Override
    public void onProximity(BlockExtended ownerType, BlockExtended.BuildExtended owner) {
        dominantOre = null;
        totalDominantOres = 0;
        totalOres = 0;

        oreCount.clear();
        tempItems.clear();

        for(Tile other : owner.tile.getLinkedTilesAs(ownerType, tempTiles)){
            if(config.canMine(other)){
                oreCount.increment(other.drop(), 0, 1);
                totalOres++;
            }
        }

        for(Item item : oreCount.keys()){
            tempItems.add(item);
        }

        tempItems.sort((item1, item2) -> {
            int type = Boolean.compare(!item1.lowPriority, !item2.lowPriority);
            if(type != 0) return type;
            int amounts = Integer.compare(oreCount.get(item1, 0), oreCount.get(item2, 0));
            if(amounts != 0) return amounts;
            return Integer.compare(item1.id, item2.id);
        });

        if(tempItems.size == 0){
            return;
        }

        dominantOre = tempItems.peek();
        totalDominantOres = oreCount.get(tempItems.peek(), 0);
    }

    @Override
    public String getName() {
        return "FloorOreState";
    }

    @Override
    public int getVersion() {
        return 0;
    }

    @Override
    public void reset() {

    }
}
