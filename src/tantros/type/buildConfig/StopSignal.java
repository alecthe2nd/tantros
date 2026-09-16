package tantros.type.buildConfig;

import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.gen.Building;
import mindustry.gen.Groups;

/**
* The signal sent by a logic chip when it is destroyed or otherwise stops "sending".
 * Used to tell other logic chips to expire all active signals associated with it.
* */
public class StopSignal extends BuildConfigurationUnit{

    public Building source = null;

    @Override
    public void read(Reads read) {
        int id = read.i();
        source = (id == -1)? null: Groups.build.getByID(id);
    }

    @Override
    public void write(Writes write) {
        write.i(source == null? -1: source.id);
    }

    @Override
    public void reset() {
        source = null;
    }
}
