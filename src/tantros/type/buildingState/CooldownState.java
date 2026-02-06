package tantros.type.buildingState;

import arc.math.Mathf;
import arc.util.Log;
import tantros.util.io.ReadContext;
import tantros.util.io.WriteContext;
import tantros.world.blocks.BlockExtended;

public class CooldownState implements BuildingState{

    public float cooldownDuration = 5;
    public float cooldown = 0;

    public CooldownState(){

    }

    public CooldownState(float duration){
        this.cooldownDuration = duration;
    }

    @Override
    public void initState(BlockExtended ownerType, BlockExtended.BuildExtended owner) {

    }

    @Override
    public void update(BlockExtended ownerType, BlockExtended.BuildExtended owner) {

        if(owner.efficiency > 1.0E-7F){
            cooldown = Mathf.approachDelta(cooldown, 1f, 1/cooldownDuration);
        } else {
            cooldown = Mathf.approachDelta(cooldown, 0f, 1/cooldownDuration);
        }
    }

    @Override
    public void onProximity(BlockExtended ownerType, BlockExtended.BuildExtended owner) {

    }

    @Override
    public void write(WriteContext write) {
        Log.info("Wrote cooldown");
        write.f(cooldown);
    }

    @Override
    public void read(ReadContext read) {
        Log.info("Read cooldown");
        cooldown = read.f();
        Log.info(cooldown);
    }

    @Override
    public boolean isTransient() {
        return false;
    }

    @Override
    public void reset() {
        cooldown = 0;
    }
}
