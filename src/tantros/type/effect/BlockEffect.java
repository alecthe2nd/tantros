package tantros.type.effect;

import tantros.world.blocks.BlockExtended;

public interface BlockEffect {

    void update(BlockExtended.BuildExtended build);

    default void apply(BlockExtended block){

    }

}
