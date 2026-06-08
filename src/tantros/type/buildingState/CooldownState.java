package tantros.type.buildingState;

import arc.math.Mathf;
import arc.util.Log;
import arc.util.io.Reads;
import arc.util.io.Writes;
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
    public void write(Writes write) {
        write.f(cooldown);
    }

    @Override
    public void read(Reads read) {
        cooldown = read.f();
    }

    @Override
    public boolean isTransient() {
        return false;
    }

    @Override
    public String getName() {
        return "CooldownState";
    }

    @Override
    public int getVersion() {
        return 0;
    }

    @Override
    public void reset() {
        cooldown = 0;
        Log.warn("Resetting cooldown");
    }

    public boolean elapsed(){
        return Mathf.equal(this.cooldown, 1);
    }
}
