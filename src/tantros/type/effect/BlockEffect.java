package tantros.type.effect;

import tantros.type.blockUtil.OnDestroyExplosionContext;
import tantros.world.blocks.BlockExtended;

public interface BlockEffect {

    OnDestroyExplosionContext defaultExplosionContext = new OnDestroyExplosionContext();

    void update(BlockExtended.BuildExtended build);

    default void apply(BlockExtended block){

    }

    default void onDestroyedExplosion(BlockExtended.BuildExtended build, OnDestroyExplosionContext explosionContext){

    }

}
