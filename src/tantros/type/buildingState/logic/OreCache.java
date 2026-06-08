package tantros.type.buildingState.logic;

import arc.func.Boolf2;
import arc.struct.Seq;
import mindustry.world.Tile;
import tantros.type.effect.projector.range.RangeConfig;
import tantros.type.buildingState.BuildingState;
import tantros.world.blocks.BlockExtended;

public class OreCache implements BuildingState {

    public Seq<Tile> ores = new Seq<>();
    public Boolf2<BlockExtended.BuildExtended, Tile> condition;

    public OreCache(){
        this.condition = (build, tile)->true;
    }

    public OreCache(Boolf2<BlockExtended.BuildExtended, Tile> condition){
        this.condition = condition;
    }


    @Override
    public void initState(BlockExtended ownerType, BlockExtended.BuildExtended owner) {

    }

    @Override
    public void update(BlockExtended ownerType, BlockExtended.BuildExtended owner) {
        RangeConfig rangeConfig = ownerType.getBlockConfig(RangeConfig.class);
        float range = -1;
        if(rangeConfig != null) {
            range = rangeConfig.maxScale;
        }

    }

    @Override
    public void onProximity(BlockExtended ownerType, BlockExtended.BuildExtended owner) {

    }

    @Override
    public String getName() {
        return "OreCache";
    }

    @Override
    public int getVersion() {
        return 0;
    }

    @Override
    public void reset() {

    }
}
