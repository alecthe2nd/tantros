package tantros.type.buildingState.logic.signalling;

import arc.util.io.Reads;
import arc.util.io.Writes;
import arc.util.pooling.Pool;
import arc.util.pooling.Pools;
import mindustry.gen.Building;
import mindustry.io.TypeIO;

public class Signal implements Pool.Poolable {

    private Building source = null;

    private Object data = null;

    public Signal(){

    }

    public Building getSource() {
        return source;
    }

    public Object getData() {
        return data;
    }

    public static Signal newSignal(Building source, Object data){
        Signal signal = Pools.obtain(Signal.class, Signal::new);
        signal.source = source;
        signal.data = data;
        return signal;
    }

    public static Signal readSignal(Reads read){
        Signal signal = Pools.obtain(Signal.class, Signal::new);
        signal.read(read);
        return signal;
    }

    public static void writeSignal(Writes write, Signal signal){
        signal.write(write);
    }

    private void read(Reads read) {
        this.data = TypeIO.readObject(read);
        this.source = TypeIO.readBuilding(read);
    }

    private void write(Writes write) {
        TypeIO.writeObject(write, this.data);
        TypeIO.writeBuilding(write, this.source);
    }

    @Override
    public void reset() {
        source = null;
        data = null;
    }
}
