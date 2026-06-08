package tantros.type.buildingState;

import arc.struct.IntSet;
import arc.struct.Seq;
import arc.util.Tmp;
import mindustry.gen.Building;
import tantros.type.effect.projector.range.RangeConfig;
import tantros.world.blocks.BlockExtended;

import static mindustry.Vars.*;

public class BuildingTargetsState implements BuildingState{

    private static final IntSet taken = new IntSet();

    public Seq<Building> targets = new Seq<>();
    public int lastChange = -2;

    public RangeConfig range;

    @Override
    public void initState(BlockExtended ownerType, BlockExtended.BuildExtended owner) {
        range = ownerType.getBlockConfig(RangeConfig.class);
        if (range == null) range = new RangeConfig(80);
    }

    @Override
    public void update(BlockExtended ownerType, BlockExtended.BuildExtended owner) {
        if(lastChange != world.tileChanges){
            lastChange = world.tileChanges;

            targets.clear();
            taken.clear();
            indexer.eachBlock(owner.team, Tmp.r1.setCentered(owner.x, owner.y, range.maxScale * tilesize), b -> true, targets::add);
        }
    }

    @Override
    public void onProximity(BlockExtended ownerType, BlockExtended.BuildExtended owner) {

    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public int getVersion() {
        return 0;
    }

    @Override
    public void reset() {

    }
}
