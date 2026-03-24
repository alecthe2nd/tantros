package tantros.type.buildingState;

import arc.scene.ui.layout.Table;
import arc.util.io.Reads;
import arc.util.io.Writes;
import tantros.world.blocks.BlockExtended;

/** A state-holding device for buildings. Allows building types to store additional state as needed.
 * Do not overuse. If there is a possibility of doing something statelessly, do it that way.
 * You can be certain you are overusing if any block has more than 255 non-transient states. If that ever happens, you have gone way too far.
 * */

public interface BuildingState {

    void initState(BlockExtended ownerType, BlockExtended.BuildExtended owner);
    void update(BlockExtended ownerType, BlockExtended.BuildExtended owner);
    void onProximity(BlockExtended ownerType, BlockExtended.BuildExtended owner);

    default <E> void onConfig(BlockExtended.BuildExtended owner, E config){

    }

    default void displayBars(BlockExtended.BuildExtended build, Table table){

    }

    /**
     * Whether this state is transient (that is, not saved to a file.)
     * Should be the same, constant true or false 100% of the time.
     * DO NOT MAKE IT CONDITIONAL ON ANYTHING DURING RUNTIME OR YOU WILL CORRUPT BELOVED SAVES
    * */
    default boolean isTransient(){
        return true;
    }

    /**
     * The name of the state. Meant to be constant during a game, should never change during the lifetime of this state.
     * Safe to change at compile time, saves using the old name will have values set to default.
     * Altering this should be avoided. If you do, take great care to ensure it is not the same name as another state.
     * Example: Preface your chosen name with your mod id so the name is always unique.
     * DO NOT MAKE IT CONDITIONAL ON ANYTHING DURING RUNTIME OR YOU WILL CORRUPT BELOVED SAVES
     * */
    String getName();

    /**
     * The version of the state. Meant to be constant during a game, should never change during the lifetime of this state.
     * Safe to change at compile time, saves using the old version will have values set to default.
     * In order to read data safely, must always be incremented if you make a change to the schema of saved data.
     * Should always be unique for a given name; never reuse a version number for any name.
     * DO NOT MAKE IT CONDITIONAL ON ANYTHING DURING RUNTIME OR YOU WILL CORRUPT BELOVED SAVES
     * */
    int getVersion();

    default void write(Writes write){
    }

    default void read(Reads read){
    }

    default void onOverflow(Object resource){

    }

    void reset();
}
