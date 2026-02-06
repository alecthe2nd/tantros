package tantros.type.effect;

import tantros.world.blocks.BlockExtended;

public class IsWalkable implements BlockEffect{

    @Override
    public void update(BlockExtended.BuildExtended build) {

    }

    @Override
    public void apply(BlockExtended block) {
        block.underBullets = true;
        block.solid = false;
    }
}
