package tantros.type.buildingState.drills;

import arc.func.Boolf;
import arc.func.Cons;
import arc.struct.ObjectMap;
import arc.util.io.Reads;
import arc.util.io.Writes;
import arc.util.pooling.Pools;
import mindustry.Vars;
import mindustry.type.Item;
import tantros.type.buildConfig.SetItemConfig;
import tantros.type.buildingState.BuildingState;
import tantros.world.blocks.BlockExtended;

public class ItemSelectionState implements BuildingState {

    public ObjectMap<Item,Boolean> toggles = new ObjectMap<>();

    public Cons<BlockExtended.BuildExtended> trigger = (b)->{};

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
        if(config instanceof SetItemConfig itemConfig){
            this.toggles.put( itemConfig.item,!this.toggles.get(itemConfig.item, false));
            Pools.free(itemConfig);
        }
    }

    @Override
    public boolean isTransient() {
        return false;
    }

    @Override
    public String getName() {
        return "ItemSelectionState";
    }

    @Override
    public int getVersion() {
        return 0;
    }

    @Override
    public void write(Writes write) {

        write.i(toggles.size);
        for(var entry : toggles.entries()){
            write.i(entry.key.id);
            write.bool(entry.value);
        }
    }

    @Override
    public void read(Reads read) {
        int size = read.i();
        for(int i = 0; i < size; i++){
            int id = read.i();
            boolean toggled = read.bool();
            Item item = Vars.content.item(id);
            if(item != null) toggles.put(item, toggled);
        }
    }

    @Override
    public void reset() {
        toggles.clear();
    }

    public Boolf<Item> isSelected = this::isSelected;

    public boolean isSelected(Item item){
        if(item == null) return false;
        return toggles.get(item, false);
    }
}
