package tantros.world.consumers;

import mindustry.Vars;
import mindustry.gen.Building;
import mindustry.world.blocks.production.GenericCrafter;
import mindustry.world.consumers.Consume;
import mindustry.world.consumers.ConsumeItems;
import tantros.type.effect.projector.range.RangeConfig;
import tantros.type.effect.projector.range.RangeState;
import tantros.world.BuildingIndexer;
import tantros.world.blocks.BlockExtended;

public class ConsumeNearbyDamagedBuilding extends ExtendedConsume {

    @Override
    public float efficiency(BlockExtended.BuildExtended build) {
        RangeConfig range = build.getBlock().getBlockConfig(RangeConfig.class);
        RangeState rangeState = build.getState(RangeState.class);
        if(range == null ) return 1f;
        float r = (rangeState == null)? range.maxScale: rangeState.fracOfMax * range.maxScale;

        Building found = Vars.indexer.findTile(build.team, build.x, build.y, r, (b)->b.damaged() && !b.isHealSuppressed());
        return found != null? 1f: 0f;
    }
}
