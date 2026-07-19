package tantros.type.buildConfig;

import arc.struct.ObjectFloatMap;
import arc.struct.Seq;
import arc.util.Nullable;
import mindustry.type.Item;
import mindustry.world.Tile;
import tantros.type.blockConfig.BlockConfig;

public class DrillConfig implements BlockConfig {

    /** Special exemption items that this drill can't mine. */
    public @Nullable Seq<Item> blockedItems;

    /** Special exemption items that ignore drill tier. */
    public @Nullable Seq<Item> allowedItems;

    /** The only items available for this drill to mine. If present, will override all other item selectors. If present but empty, this block cannot drill anything. */
    public @Nullable Seq<Item> whitelist;

    /** The */
    public boolean onlyDrillsDominantItems = true;

    public int tier = 0;

    public float hardnessMultiplier = 50f;

    /** The name of the ore tracking state */
    public String oreStateName;

    /** Multipliers of drill speed for each item. Defaults to 1. */
    public ObjectFloatMap<Item> drillMultipliers = new ObjectFloatMap<>();

    public boolean drillable(Item item){
        return (
                whitelist == null
                && (blockedItems == null || !blockedItems.contains(item))
                && (item.hardness <= tier || (allowedItems != null && allowedItems.contains(item)))
        ) || (whitelist != null && whitelist.contains(item));
    }

    public boolean canMine(Tile tile){
        if(tile == null || tile.block().isStatic()) return false;
        Item drops = tile.drop();
        return drops != null && this.drillable(drops);
    }
}
