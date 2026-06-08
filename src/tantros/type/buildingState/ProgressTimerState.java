package tantros.type.buildingState;

import arc.func.Func;
import arc.math.Mathf;
import arc.util.Log;
import arc.util.Time;
import arc.util.io.Reads;
import arc.util.io.Writes;
import tantros.type.buildConfig.ProgressTimerConfig;
import tantros.world.blocks.BlockExtended;

public class ProgressTimerState implements BuildingState {

    public ProgressTimerConfig config = null;

    public float progress = 0f;
    public float totalProgress = 0f;
    public float warmup = 0f;

    @Override
    public void initState(BlockExtended ownerType, BlockExtended.BuildExtended owner) {
        if(config == null) config = ownerType.getBlockConfig(ProgressTimerConfig.class);
    }

    @Override
    public void update(BlockExtended ownerType, BlockExtended.BuildExtended owner) {
        if(owner.efficiency > 0){
            progress += owner.getProgressIncrease(config.progressTime);
            warmup = Mathf.approachDelta(warmup, 1, config.warmupSpeed);
            totalProgress += warmup * Time.delta;
        } else {
            warmup = Mathf.approachDelta(warmup, 0, config.warmupSpeed);
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
        return "ProgressTimerState";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void write(Writes write) {
        write.f(warmup);
        write.f(progress);
        write.f(totalProgress);
    }

    @Override
    public void read(Reads read) {
        warmup = read.f();
        progress = read.f();
        totalProgress = read.f();
    }

    @Override
    public void reset() {
        this.progress = 0f;
        this.totalProgress = 0f;
        this.warmup = 0f;
    }
}
