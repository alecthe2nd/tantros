package tantros.type.buildConfig;

import arc.struct.ObjectSet;
import arc.struct.Seq;
import arc.util.io.Reads;
import arc.util.io.Writes;
import arc.util.pooling.Pools;
import mindustry.gen.Building;
import mindustry.io.TypeIO;
import tantros.type.buildingState.logic.signalling.Signal;

/**
 * The config sent by a logic chip when the list of signals it is relaying changes.
 * Used to tell other logic chips to mark it (or unmark it) as an active signal sender.
 * */
public class UpdateSignal extends BuildConfigurationUnit{

    public Building source = null;

    public Seq<Signal> signals = new Seq<>();

    @Override
    public void read(Reads read) {
        source = TypeIO.readBuilding(read);
        int size = read.i();
        for(int i = 0; i < size; i++){
            signals.add(Signal.readSignal(read));
        }
    }

    @Override
    public void write(Writes write) {
        TypeIO.writeBuilding(write, source);
        write.i(signals.size);
        for(int i = 0; i < signals.size; i++){
            Signal.writeSignal(write, signals.get(i));
        }
    }

    @Override
    public void reset() {
        source = null;
        signals.clear();
    }
}
