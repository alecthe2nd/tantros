package tantros.type.buildingState;

import arc.math.Mathf;
import arc.util.io.Reads;
import arc.util.io.Writes;
import tantros.type.buildConfig.WarmupConfig;
import tantros.world.blocks.BlockExtended;

public class WarmupState implements BuildingState {

    public float warmup = 0;

    public WarmupConfig config;

    @Override
    public void initState(BlockExtended ownerType, BlockExtended.BuildExtended owner) {
        config = ownerType.getBlockConfig(WarmupConfig.class);
        if(config == null){
            config = new WarmupConfig();
        }
    }

    @Override
    public void update(BlockExtended ownerType, BlockExtended.BuildExtended owner) {
        warmup = Mathf.approachDelta(warmup, warmupTarget(owner), config.warmupSpeed);
    }

    public float warmupTarget(BlockExtended.BuildExtended owner){
        if(config.warmupTargetsEfficiency){
            return owner.efficiency;
        } else {
            return (owner.efficiency > 0.001f)? 1f: 0f;
        }
    }

    @Override
    public void onProximity(BlockExtended ownerType, BlockExtended.BuildExtended owner) {

    }

    @Override
    public boolean isTransient() {
        return false;
    }

    @Override
    public String getName() {
        return "WarmupState";
    }

    @Override
    public int getVersion() {
        return 0;
    }

    @Override
    public void write(Writes write) {
        write.f(warmup);
    }

    @Override
    public void read(Reads read) {
        warmup = read.f();
    }

    @Override
    public void reset() {
        warmup = 0;
    }
}
