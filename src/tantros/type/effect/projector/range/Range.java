package tantros.type.effect.projector.range;

import arc.func.Func2;
import arc.math.geom.Position;
import tantros.type.buildingState.BuildingState;
import tantros.world.blocks.BlockExtended;

public class Range implements BuildingState, Func2<BlockExtended.BuildExtended, Position, Boolean> {

    public float scale;

    public RangeShape shape;

    public Range(float scale){
        this(scale, RangeShape.circle);
    }
    public Range(float scale, RangeShape shape){
        this.scale = scale;
        this.shape = shape;
    }

    @Override
    public Boolean get(BlockExtended.BuildExtended param1, Position param2) {
        return (shape.optimizedCheck != null)? shape.optimizedCheck.get(param1, param2, scale): false;
    }


    @Override
    public void initState(BlockExtended ownerType, BlockExtended.BuildExtended owner) {

    }

    @Override
    public void update(BlockExtended ownerType, BlockExtended.BuildExtended owner) {

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
