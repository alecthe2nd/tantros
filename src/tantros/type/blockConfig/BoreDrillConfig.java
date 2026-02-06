package tantros.type.blockConfig;

import arc.struct.ObjectFloatMap;
import arc.struct.Seq;
import arc.util.Nullable;
import mindustry.type.Item;
import mindustry.world.Tile;

public class BoreDrillConfig implements BlockConfig {

    public int tier;
    public int range;
    public @Nullable Seq<Item> blockedItems;
    public ObjectFloatMap<Item> drillMultipliers = new ObjectFloatMap<>();

    public BoreDrillConfig(int tier, int range){
        this(tier,range,null);
    }

    public BoreDrillConfig(int tier, int range, Seq<Item> blockedItems){
        this.tier = tier;
        this.range = range;
        this.blockedItems = blockedItems;
    }


    public boolean canMine(Tile tile){
        if(tile == null) return false;
        Item drop = getDrop(tile);
        return drop != null && drop.hardness <= this.tier && (blockedItems == null || !blockedItems.contains(drop));
    }

    public Item getDrop(Tile tile){
        return tile.wallDrop();
    }

}
