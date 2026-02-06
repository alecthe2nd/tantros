package tantros.type.blockConfig;

import arc.struct.ObjectFloatMap;
import arc.struct.Seq;
import arc.util.Nullable;
import mindustry.type.Item;
import mindustry.world.Tile;
import mindustry.world.blocks.environment.Floor;
import tantros.world.blocks.environment.DeepOreBlock;

public class DeepDrillConfig implements BlockConfig {

    public int tier;
    public @Nullable Seq<Item> blockedItems;
    public ObjectFloatMap<Item> drillMultipliers = new ObjectFloatMap<>();

    public DeepDrillConfig(int tier){
        this(tier,null);
    }

    public DeepDrillConfig(int tier, Seq<Item> blockedItems){
        this.tier = tier;
        this.blockedItems = blockedItems;
    }

    public boolean canMine(Tile tile){
        if(tile == null || tile.block().isStatic()) return false;
        Item drop = getDrop(tile);
        return drop != null && drop.hardness <= this.tier && (blockedItems == null || !blockedItems.contains(drop));
    }

    public Item getDrop(Tile tile){
        Floor overlay = tile.overlay();
        return (overlay instanceof DeepOreBlock deep)? deep.deepDrop : tile.drop();
    }

}
