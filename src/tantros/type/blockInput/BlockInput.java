package tantros.type.blockInput;

import arc.scene.ui.layout.Table;
import mindustry.gen.Building;
import tantros.world.blocks.BlockExtended;

public interface BlockInput {

    default void apply(BlockExtended block){

    }

    default void post(BlockExtended.BuildExtended build){

    }

    default void remove(BlockExtended.BuildExtended build){

    }

    default boolean onConfigureBuildTapped(BlockExtended.BuildExtended build, Building other){
        return true;
    }

    default void buildConfiguration(BlockExtended.BuildExtended build, Table table){

    }

}
