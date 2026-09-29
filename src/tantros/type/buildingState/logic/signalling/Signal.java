package tantros.type.buildingState.logic.signalling;

import arc.Core;
import arc.util.Log;
import arc.util.io.Reads;
import arc.util.io.Writes;
import arc.util.pooling.Pool;
import arc.util.pooling.Pools;
import mindustry.gen.Building;
import mindustry.io.TypeIO;
import tantros.io.TantrosTypeIO;

public class Signal implements Pool.Poolable {

    private Building source = null;

    private Object data = null;

    private TypeIO.Boxed<Building> boxedSource;

    private boolean boxed = false;

    public Signal(){

    }

    public Building getSource() {
        if(boxed) unbox();
        return source;
    }

    public Object getData() {
        if(boxed) unbox();
        return data;
    }

    public void unbox(){
        source = boxedSource.unbox();
        if(data instanceof TypeIO.Boxed<?> box){
            data = box.unbox();
        }
        boxed = false;
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
        this.data = TypeIO.readObjectBoxed(read, true);
        this.boxedSource = TantrosTypeIO.readBoxedBuilding(read);
        this.boxed = true;
    }

    private void write(Writes write) {
        TypeIO.writeObject(write, this.data);
        TypeIO.writeBuilding(write, this.source);
    }

    @Override
    public void reset() {
        source = null;
        data = null;
        boxed = false;
        boxedSource = null;
    }

    @Override
    public String toString() {
        if(boxed){
            return Core.bundle.format(
                    "block-ability.displays-signals.signal.boxed",
                    String.valueOf(this.boxedSource),
                    String.valueOf(this.data)
            );
        }else {
            return Core.bundle.format(
                    "block-ability.displays-signals.signal",
                    String.valueOf(getSource()),
                    String.valueOf(getData())
            );
        }
    }
}
