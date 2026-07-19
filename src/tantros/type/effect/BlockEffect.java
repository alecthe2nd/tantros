package tantros.type.effect;

import mindustry.game.Team;
import mindustry.world.Tile;
import mindustry.world.meta.BlockStatus;
import tantros.type.blockUtil.OnDestroyExplosionContext;
import tantros.world.blocks.BlockExtended;

public interface BlockEffect {

    OnDestroyExplosionContext defaultExplosionContext = new OnDestroyExplosionContext();

    void update(BlockExtended.BuildExtended build);

    default void apply(BlockExtended block){

    }

    default boolean canBeAppliedTo(BlockExtended block){
        return true;
    }

    /**Ideal timing for setting stats*/
    default void setStats(BlockExtended block){

    }

    default void onDestroyedExplosion(BlockExtended.BuildExtended build, OnDestroyExplosionContext explosionContext){

    }

    default boolean placementAllowed(BlockExtended block, Tile tile, Team team, int rotation){
        return true;
    }

    default float efficiency(){
        return 1f;
    }

    default BlockStatus status(){
        return BlockStatus.active;
    }

}
