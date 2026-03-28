package tantros.type.effect;

import arc.struct.Seq;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.world.Tile;
import tantros.type.blockConfig.AttributeConfig;
import tantros.world.blocks.BlockExtended;

public class AttributePlacementRestriction implements BlockEffect{

    public AttributeConfig config;

    protected static final Seq<Tile> tempTiles = new Seq<>();
    protected static final Seq<Building> tempBuilds = new Seq<>();
    @Override
    public void update(BlockExtended.BuildExtended build) {

    }

    @Override
    public void apply(BlockExtended block) {
        if(block.blockConfigs.containsKey(AttributeConfig.class)){
            this.config = block.getBlockConfig(AttributeConfig.class);
        } else {
            this.config = new AttributeConfig();
            this.config.minEfficiency = block.size * block.size - 0.0001f;
            block.putBlockConfig(this.config);
        }
    }

    @Override
    public boolean placementAllowed(BlockExtended block, Tile tile, Team team, int rotation) {
        return tile.getLinkedTilesAs(block, tempTiles).sumf(other -> other.floor().attributes.get(config.attribute)) > config.minEfficiency;
    }
}
