package tantros.type.blockConfig;

import arc.func.Boolf;
import arc.func.Prov;
import mindustry.gen.Building;
import tantros.world.blocks.BlockExtended;

public class BuildingTargetsConfig implements BlockConfig {

    public int refreshTime = 10;

    /**
    * Whether to refresh targets every tick.
    * If true, {@link #refreshTime} will be used as the duration (in ticks) between refreshes.
    * If false, targets will be refreshed only when the world changes. {@link #refreshTime} will be used as a cooldown duration in ticks to reduce checks when world updates occur too frequently.
    * */
    public boolean refreshEachTick = false;

    public Boolf<Building> filter = (b)->true;
}
