package tantros.type.effect.projector.range;

import tantros.type.effect.BlockEffect;
import tantros.world.blocks.BlockExtended;

public class HasRange implements BlockEffect {

    RangeConfig config;

    public HasRange(RangeConfig config){
        this.config = config;
    }

    @Override
    public void update(BlockExtended.BuildExtended build) {

    }

    @Override
    public void apply(BlockExtended block) {
        block.putBlockConfig(config);
        block.postStateRequest(RangeState::new);
    }
}
