package tantros.type.buildingState.logic;

import arc.func.Boolf;
import arc.func.Boolf2;
import arc.struct.IntSeq;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.gen.Groups;
import mindustry.gen.Unit;
import tantros.type.buildConfig.AddUnitConfig;
import tantros.type.buildConfig.ClearUnitsConfig;
import tantros.type.buildingState.BuildingState;
import tantros.util.io.ReadContext;
import tantros.util.io.WriteContext;
import tantros.world.blocks.BlockExtended;

public class UnitLinks implements BuildingState {

    public IntSeq unitIds = new IntSeq();

    public Seq<Unit> unitLinks = new Seq<>();

    public Boolf2<BlockExtended.BuildExtended, Unit> condition = (b,u)->true;

    public UnitLinks(){}

    public UnitLinks(Boolf2<BlockExtended.BuildExtended, Unit> linkCondition){
        this.condition = linkCondition;
    }


    @Override
    public void initState(BlockExtended ownerType, BlockExtended.BuildExtended owner) {

    }

    @Override
    public void update(BlockExtended ownerType, BlockExtended.BuildExtended owner) {
        Log.info("[Unitlinks] clear unit ids");
        if(!unitIds.isEmpty()){
            unitLinks.clear();
            unitIds.each(i -> {
                var unit = Groups.unit.getByID(i);
                if(unit != null){
                    unitLinks.add(unit);
                }
            });
            unitIds.clear();
        }
    }

    @Override
    public <E> void onConfig(BlockExtended.BuildExtended owner, E config) {
        Log.info("[Unitlinks] Received config");
        if(config instanceof AddUnitConfig unitConfig && condition.get(owner, unitConfig.unit)){
            Log.info("[Unitlinks] Received unit "+ unitConfig.unit);

            boolean alreadyLinked = unitLinks.contains((u)->u.id == unitConfig.unit.id);
            if(alreadyLinked && unitConfig.toggle){
                unitLinks.remove((u)->u.id == unitConfig.unit.id);
            } else {
                unitLinks.remove((u)->u.id == unitConfig.unit.id);
                unitLinks.add(unitConfig.unit);
            }
        }
        if(config instanceof ClearUnitsConfig){
            unitLinks.clear();
        }
    }

    @Override
    public boolean isTransient() {
        return false;
    }

    @Override
    public String getName() {
        return "UnitLinks";
    }

    @Override
    public int getVersion() {
        return 0;
    }

    @Override
    public void onProximity(BlockExtended ownerType, BlockExtended.BuildExtended owner) {

    }

    @Override
    public void reset() {
        unitIds.clear();
        unitLinks.clear();

    }

    @Override
    public void write(Writes write) {
        write.i(unitLinks.size);
        for(Unit unit : unitLinks){
            write.i(unit.id);
        }
    }

    @Override
    public void read(Reads read) {
        int size = read.i();
        for(int i = 0 ; i < size; i++){
            this.unitIds.add(read.i());
        }
    }
}
