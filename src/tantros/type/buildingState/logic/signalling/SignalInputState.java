package tantros.type.buildingState.logic.signalling;

import arc.struct.IntMap;
import arc.struct.ObjectSet;
import arc.util.io.Reads;
import arc.util.io.Writes;
import arc.util.pooling.Pools;
import tantros.type.buildConfig.UpdateSignal;
import tantros.type.buildingState.BuildingState;
import tantros.world.blocks.BlockExtended;

public class SignalInputState implements BuildingState {

    public IntMap<ObjectSet<Signal>> signals = new IntMap<>();

    public boolean dirty = false;

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
    public <E> void onConfig(BlockExtended.BuildExtended owner, E config) {
        if(config instanceof UpdateSignal updateSignal){
            ObjectSet<Signal> set;
            if(signals.containsKey(updateSignal.source.id)){
                set = signals.get(updateSignal.source.id);
            } else {
                set = new ObjectSet<>();
                signals.put(updateSignal.source.id, set);
            }
            for(Signal signal: updateSignal.signals){
                dirty |= set.add(signal);
            }
            Pools.free(updateSignal);
        }
    }

    @Override
    public boolean isTransient() {
        return false;
    }

    @Override
    public String getName() {
        return "SignalInputState";
    }

    @Override
    public int getVersion() {
        return 0;
    }

    @Override
    public void write(Writes write) {
        write.i(signals.size);
        for(IntMap.Entry<ObjectSet<Signal>> entry: signals){
            write.i(entry.key);
            write.i(entry.value.size);
            for(Signal signal: entry.value){
                Signal.writeSignal(write, signal);
            }
        }
        write.bool(dirty);
    }

    @Override
    public void read(Reads read) {
        int map_size = read.i();
        for(int i = 0; i < map_size; i++){
            int sourceId = read.i();
            ObjectSet<Signal> signals = new ObjectSet<>();
            int size = read.i();
            for(int j = 0; j < size; j++){
                signals.add(Signal.readSignal(read));
            }
            this.signals.put(sourceId, signals);
        }
        dirty = read.bool();
    }

    @Override
    public void reset() {

    }
}
