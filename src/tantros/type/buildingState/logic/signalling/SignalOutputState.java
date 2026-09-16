package tantros.type.buildingState.logic.signalling;

import arc.struct.ObjectSet;
import arc.struct.Seq;
import arc.util.io.Reads;
import arc.util.io.Writes;
import arc.util.pooling.Pools;
import mindustry.io.TypeIO;
import tantros.type.buildingState.BuildingState;
import tantros.world.blocks.BlockExtended;

public class SignalOutputState implements BuildingState {

    public ObjectSet<Signal> signals = new ObjectSet<>();

    public boolean dirty;

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
    public boolean isTransient() {
        return false;
    }

    @Override
    public String getName() {
        return "SignalOutputState";
    }

    @Override
    public int getVersion() {
        return 0;
    }

    @Override
    public void write(Writes write) {
        write.i(signals.size);
        for(Signal signal: signals){
            Signal.writeSignal(write, signal);
        }
        write.bool(dirty);
    }

    @Override
    public void read(Reads read) {
        int size = read.i();
        for(int i = 0; i < size; i++){
            signals.add(Signal.readSignal(read));
        }
        dirty = read.bool();
    }

    @Override
    public void reset() {

    }

    /**Switches the output signal to exactly one signal by
     * clearing the list and then adding the one signal.*/
    public void toggleSignal(BlockExtended.BuildExtended build, Object data){
        for(Signal oldSignals: this.signals){
            Pools.free(oldSignals);
        }
        this.signals.clear();
        Signal signal = Signal.newSignal(build, data);
        this.signals.add(signal);
        this.dirty = true;
    }
}
