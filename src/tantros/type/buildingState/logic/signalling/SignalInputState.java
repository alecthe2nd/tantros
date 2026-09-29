package tantros.type.buildingState.logic.signalling;

import arc.math.geom.Geometry;
import arc.math.geom.Vec2;
import arc.struct.IntMap;
import arc.struct.ObjectMap;
import arc.struct.ObjectSet;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.io.Reads;
import arc.util.io.Writes;
import arc.util.pooling.Pools;
import mindustry.Vars;
import mindustry.gen.Building;
import mindustry.world.Build;
import mindustry.world.Tile;
import tantros.type.buildConfig.UpdateSignal;
import tantros.type.buildingState.BuildingState;
import tantros.world.blocks.BlockExtended;

public class SignalInputState implements BuildingState {

    public Vec2 tmpPos1 = new Vec2();

    public IntMap<ObjectSet<Signal>> signals = new IntMap<>();
    public ObjectMap<Class<?>, Seq<Signal>> signalDataIndex = new ObjectMap<>();
    public Seq<Signal> flatSignals = new Seq<>();

    public boolean dirty = false;

    @Override
    public void onRemove(BlockExtended.BuildExtended build) {
        //build index
        for(IntMap.Entry<ObjectSet<Signal>> entry: signals){
            for(Signal signal: entry.value){
                Pools.free(signal);
            }
            entry.value.clear();
        }
    }

    @Override
    public <E> void onConfig(BlockExtended.BuildExtended owner, E config) {
        if(config instanceof UpdateSignal updateSignal){
            ObjectSet<Signal> set = signals.get(updateSignal.source.pos(),ObjectSet::new);


            //remove all in the indices (not source of truth, so don't free)
            for(ObjectMap.Entry<Class<?>, Seq<Signal>> entry: signalDataIndex){
                entry.value.clear();
            }
            flatSignals.clear();

            //remove previous signals received from this source building
            clear(set);

            //add incoming signals from source building
            for(Signal signal: updateSignal.signals){
                set.add(signal);
            }

            //build indices
            for(IntMap.Entry<ObjectSet<Signal>> entry: signals){
                for(Signal signal: entry.value){
                    addIndexed(signal);
                }
            }

            //update signal config has completed its task
            Pools.free(updateSignal);
            dirty = true;
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
            int sourcePos = read.i();
            ObjectSet<Signal> signals = new ObjectSet<>();
            int size = read.i();
            for(int j = 0; j < size; j++){
                signals.add(Signal.readSignal(read));
            }
            this.signals.put(sourcePos, signals);
        }
        dirty = read.bool();
    }

    @Override
    public void reset() {

    }

    /** Fetches a signal arbituarily.*/
    public Signal get(){
        if(flatSignals.isEmpty()) return null;
        return flatSignals.first();
    }


    /** Fetches a signal arbituarily.*/
    public Signal get(int index){
        if(index >= flatSignals.size) return null;
        return flatSignals.get(index);
    }

    /** Fetches a signal arbituarily from a building in the given direction.*/
    public Signal get(BlockExtended.BuildExtended build, int dir){
        for(IntMap.Entry<ObjectSet<Signal>> entry: signals){
            if(entry.value.isEmpty()) continue;
            Building other = Vars.world.build(entry.key);
            if(other == null) continue;
            if(build.relativeTo(other) == dir){
                return entry.value.first();
            }
        }
        return null;
    }

    public <T> T find(Class<T> type){
        Signal signal = signalDataIndex.get(type).firstOpt();
        if(type.isInstance(signal.getData())){
            return type.cast(signal.getData());
        }
        return null;
    }

    public void addIndexed(Signal signal){
        if(signal == null || signal.getData() == null) return;
        Seq<Signal> signals = signalDataIndex.get(signal.getData().getClass(), Seq::new);
        signals.add(signal);
        flatSignals.add(signal);
    }

    public void clear(ObjectSet<Signal> signals){
        for(Signal signal: signals){
            Pools.free(signal);
        }
        signals.clear();
    }
}
