package tantros.type.buildConfig;

import mindustry.Vars;
import mindustry.gen.Groups;
import mindustry.gen.Unit;
import tantros.util.io.ReadContext;
import tantros.util.io.WriteContext;

public class AddUnitConfig extends BuildConfigurationUnit{

    public Unit unit = null;
    public boolean toggle = false;

    @Override
    public void reset() {
        this.unit = null;
    }

    @Override
    public void read(ReadContext read) {
        int id = read.i();
        if (id != -1) unit = Groups.unit.getByID(id);
        toggle = read.bool();
    }

    @Override
    public void write(WriteContext write) {
        write.i(unit == null? -1 : unit.id);
        write.bool(toggle);
    }
}
