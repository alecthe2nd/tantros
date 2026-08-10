package tantros.type.buildConfig;

import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.Vars;
import mindustry.type.Item;

public class SetItemConfig extends BuildConfigurationUnit{

    public Item item = null;

    public SetItemConfig setItem(Item item) {
        this.item = item;
        return this;
    }

    @Override
    public void read(Reads read) {
        int id = read.i();
        item = Vars.content.item(id);
    }

    @Override
    public void write(Writes write) {
        write.i((item != null)? item.id: -1);
    }

    @Override
    public void reset() {
        item = null;
    }
}
