package tantros.type.buildingState;

import arc.util.io.Reads;
import arc.util.io.Writes;
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

    void reset();

}
