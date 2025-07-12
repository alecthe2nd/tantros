package tantros.content.world.blocks.production;

import arc.struct.Seq;
import mindustry.type.Item;
import mindustry.world.Tile;
import mindustry.world.blocks.Attributes;
import mindustry.world.blocks.environment.Floor;
import mindustry.world.blocks.production.GenericCrafter;
import mindustry.world.meta.Attribute;

public class FloorDrillCrafter extends GenericCrafter {

    /** Any Tile attributes that are required for functioning*/
    public Seq<Attribute> anyAttributes;

    /** Any Tile Drops that are required for functioning*/
    public Seq<Item> anyItems;



    public FloorDrillCrafter(String name) {
        super(name);
    }

    public boolean tileMatches(Tile tile){
        boolean validTile = false;
        for(Attribute attr: anyAttributes) {
            validTile |= tileMatchesAttr(tile, attr);
        }
        for(Item item: anyItems) {
            validTile |= tileMatchesItem(tile, item);
        }
        return validTile;
    }

    public boolean tileMatchesAttr(Tile tile, Attribute attr){
        return tile.floor().attributes.get(attr) > 0f
                || tile.overlay().attributes.get(attr) > 0f;
    }

    public boolean tileMatchesItem(Tile tile, Item item){
        return tile.floor().itemDrop == item
                || tile.overlay().itemDrop == item;
    }

    public class FloorDrillCrafterBuild extends GenericCrafterBuild{
        @Override
        public boolean shouldConsume() {
            boolean validTile = false;
            for(Tile tile: this.tile.getLinkedTilesAs(this.block, tempTiles)){
                validTile |= ((FloorDrillCrafter)block).tileMatches(tile);
            }
            return validTile && super.shouldConsume();
        }
    }
}
