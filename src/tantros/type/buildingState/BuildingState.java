package tantros.type.buildingState;

import arc.scene.ui.layout.Table;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.ui.Bar;
import tantros.util.io.ReadContext;
import tantros.util.io.WriteContext;
import tantros.world.blocks.BlockExtended;

/** A state-holding device for buildings. Allows building types to store additional state as needed.
 * Do not overuse. If there is a possibility of doing something statelessly, do it that way.*/
public interface BuildingState {

    WriteContext writeContext = new WriteContext();
    ReadContext readContext = new ReadContext();
    void initState(BlockExtended ownerType, BlockExtended.BuildExtended owner);
    void update(BlockExtended ownerType, BlockExtended.BuildExtended owner);
    void onProximity(BlockExtended ownerType, BlockExtended.BuildExtended owner);

    default <E> void onConfig(BlockExtended.BuildExtended owner, E config){

    }

    default void displayBars(BlockExtended.BuildExtended build, Table table){

    }

    default boolean isTransient(){
        return true;
    }

    default void write(Writes write){
        writeContext.init(write);
        this.write(writeContext);
        writeContext.flush();
    }

    default void write(WriteContext write){

    }

    default void read(Reads read){
        readContext.init(read);
        this.read(readContext);
        readContext.flush(this::reset);
    }

    default void read(ReadContext read){

    }

    default void onOverflow(Object resource){

    }

    void reset();

    static void addBar(Table table, Bar bar){
        if(bar == null) return;
        table.add(bar).growX();
        table.row();
    }

}
