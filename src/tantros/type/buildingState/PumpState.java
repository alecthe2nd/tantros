package tantros.type.buildingState;

import mindustry.content.Liquids;
import mindustry.type.Liquid;
import mindustry.type.LiquidStack;
import mindustry.world.Tile;
import tantros.type.Resource;
import tantros.type.blockConfig.PumpConfig;
import tantros.world.blocks.BlockExtended;

public class PumpState implements BuildingState{

    public PumpConfig config;
    protected LiquidStack cachedStack = new LiquidStack(Liquids.water, 0);
    public Resource cachedOutput = new Resource();

    public float amount = 0;
    public Liquid liquidDrop;

    @Override
    public void initState(BlockExtended ownerType, BlockExtended.BuildExtended owner) {
        config = ownerType.getBlockConfig(PumpConfig.class);
        reset();
    }

    @Override
    public void update(BlockExtended ownerType, BlockExtended.BuildExtended owner) {

    }

    @Override
    public void onProximity(BlockExtended ownerType, BlockExtended.BuildExtended owner) {

        amount = 0f;
        liquidDrop = null;

        for(Tile other : owner.tile.getLinkedTiles(PumpConfig.tempTiles)){
            if(config.canPump(other)){
                liquidDrop = other.floor().liquidDrop;
                amount += other.floor().liquidMultiplier;
            }
        }
        cachedStack.set(liquidDrop, amount);
        cachedOutput.clear();
        cachedOutput.withLiquids(cachedStack);
    }

    @Override
    public void reset() {
        amount = 0;
        liquidDrop = null;
    }
}
