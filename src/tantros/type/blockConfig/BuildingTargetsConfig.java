package tantros.type.blockConfig;

import tantros.world.blocks.BlockExtended;

public class BuildingTargetsConfig implements BlockConfig {

    public int refreshTimer;

    public int refreshTime = 10;

    /**
    * Whether to refresh targets every tick.
    * If true, {@link #refreshTimer} will be used as the timer and {@link #refreshTime} will be used as the duration (in ticks) between refreshes.
    * If false, targets will be refreshed only when the world changes. {@link #refreshTimer} will be used as a cooldown timer of duration {@link #refreshTime} in ticks.
    * */
    public boolean refreshEachTick = false;

    @Override
    public void apply(BlockExtended block) {
        refreshTimer = block.timers++;
    }
}
