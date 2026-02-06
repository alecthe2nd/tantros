package tantros.type.effect;

import tantros.world.blocks.BlockExtended;

public class IsBuilding implements BlockEffect{
    @Override
    public void update(BlockExtended.BuildExtended build) {

    }

    @Override
    public void apply(BlockExtended block) {
        block.update = true;
        block.solid = true;
        block.sync = true;
    }
}
