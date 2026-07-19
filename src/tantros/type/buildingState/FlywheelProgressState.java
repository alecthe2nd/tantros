package tantros.type.buildingState;

import arc.math.Mathf;
import arc.util.Time;
import arc.util.io.Reads;
import arc.util.io.Writes;
import tantros.type.buildConfig.FlywheelConfig;
import tantros.world.blocks.BlockExtended;

public class FlywheelProgressState implements BuildingState {

    public FlywheelConfig config;

    public float progress = 0f;
    public float flywheelSpeed = 0f;

    public FlywheelProgressState(){

    }

    public FlywheelProgressState(FlywheelConfig config){
        this.config = config;
    }

    @Override
    public void initState(BlockExtended ownerType, BlockExtended.BuildExtended owner) {
        if(config == null) config = ownerType.getBlockConfig(FlywheelConfig.class);
    }

    @Override
    public void update(BlockExtended ownerType, BlockExtended.BuildExtended owner) {
        flywheelSpeed = Mathf.approachDelta(flywheelSpeed, owner.efficiency, 1f/config.inertia);
        progress += flywheelSpeed * config.maxSpeed * Time.delta;
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
        return "FlywheelProgress";
    }

    @Override
    public int getVersion() {
        return 0;
    }

    @Override
    public void write(Writes write) {
        write.f(flywheelSpeed);
    }

    @Override
    public void read(Reads read) {
        flywheelSpeed = read.f();
    }

    @Override
    public void reset() {

    }
}
