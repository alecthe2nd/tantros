package tantros.type.effect.projector.range;

import arc.math.geom.Position;
import tantros.type.buildingState.BuildingState;
import tantros.world.blocks.BlockExtended;

public class RangeState implements BuildingState {

    public RangeConfig config = null;

    public float fracOfMax;

    public boolean wasUpdated = false;

    @Override
    public void initState(BlockExtended ownerType, BlockExtended.BuildExtended owner) {
        if (config == null) config = ownerType.getBlockConfig(RangeConfig.class);
    }

    @Override
    public void update(BlockExtended ownerType, BlockExtended.BuildExtended owner) {
        if(wasUpdated){
            wasUpdated = false;
        } else {
            fracOfMax = 1;
        }
    }

    @Override
    public void onProximity(BlockExtended ownerType, BlockExtended.BuildExtended owner) {

    }

    @Override
    public String getName() {
        return "RangeState";
    }

    @Override
    public int getVersion() {
        return 0;
    }

    @Override
    public void reset() {

    }

    public float range(){
        return this.fracOfMax * config.maxScale;
    }

    public Boolean inRange(BlockExtended.BuildExtended build, Position posToCheck) {
        return (config.shape.optimizedCheck != null)? config.shape.optimizedCheck.get(build, posToCheck, range()): false;
    }
}
