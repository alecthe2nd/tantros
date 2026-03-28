package tantros.type.buildingState;

import arc.scene.ui.layout.Table;
import tantros.type.blockConfig.AttributeConfig;
import tantros.world.blocks.BlockExtended;

public class AttributeState implements BuildingState{

    AttributeConfig config;

    public float floorSum = 0;

    public float sum = 0;

    @Override
    public void initState(BlockExtended ownerType, BlockExtended.BuildExtended owner) {
        if(ownerType.blockConfigs.containsKey(AttributeConfig.class)){
            config = ownerType.getBlockConfig(AttributeConfig.class);
        } else {
            config = new AttributeConfig();
        }
    }

    @Override
    public void update(BlockExtended ownerType, BlockExtended.BuildExtended owner) {
        sum = floorSum + config.attribute.env();
    }

    @Override
    public void onProximity(BlockExtended ownerType, BlockExtended.BuildExtended owner) {
        floorSum = ownerType.sumAttribute(config.attribute, owner.tile.x, owner.tile.y);
    }

    @Override
    public String getName() {
        return "AttributeState";
    }

    @Override
    public int getVersion() {
        return 0;
    }

    @Override
    public void reset() {

    }
}
