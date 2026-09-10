package tantros.type.buildingState;

import arc.func.Boolf;
import arc.struct.IntSet;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.Tmp;
import mindustry.gen.Building;
import tantros.type.blockConfig.BuildingTargetsConfig;
import tantros.type.effect.projector.range.RangeConfig;
import tantros.type.effect.projector.range.RangeState;
import tantros.world.blocks.BlockExtended;

import static mindustry.Vars.*;

public class BuildingTargetsState implements BuildingState{

    public BuildingTargetsConfig config;

    private static final IntSet taken = new IntSet();

    public Seq<Building> targets = new Seq<>();
    public int lastChange = -2;
    public Boolf<Building> filter = (b)->true;

    public RangeConfig rangeConfig;

    public BuildingTargetsState(BuildingTargetsConfig config, RangeConfig rangeConfig){
        this.rangeConfig = rangeConfig;
        this.config = config;
    }

    @Override
    public void initState(BlockExtended ownerType, BlockExtended.BuildExtended owner) {
    }

    @Override
    public void update(BlockExtended ownerType, BlockExtended.BuildExtended build) {
        if(build.timer(config.refreshTimer, config.refreshTime)) {
            if (lastChange != world.tileChanges) {
                lastChange = world.tileChanges;

                RangeState range = build.getState(RangeState.class, rangeConfig.rangeStateName);
                if (range != null) {
                    targets.clear();
                    taken.clear();
                    indexer.eachBlock(build.team, Tmp.r1.setCentered(build.x, build.y, range.range() * 2), b -> filter.get(b) && range.inRange(build, b), targets::add);
                }
            }
        }
    }

    @Override
    public void onProximity(BlockExtended ownerType, BlockExtended.BuildExtended owner) {

    }

    @Override
    public String getName() {
        return "BuildingTargetsStates";
    }

    @Override
    public int getVersion() {
        return 0;
    }

    @Override
    public void reset() {

    }
}
