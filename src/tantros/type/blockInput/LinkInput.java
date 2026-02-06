package tantros.type.blockInput;

import arc.util.Log;
import mindustry.gen.Building;
import tantros.type.buildingState.BuildingState;
import tantros.type.buildingState.logic.Links;
import tantros.world.blocks.BlockExtended;

public class LinkInput<E extends BuildingState & Links> implements BlockInput{

    public Class<E> linksType;

    public LinkInput(Class<E> linksType){
        this.linksType = linksType;
    }

    @Override
    public void apply(BlockExtended block) {
        block.configurable = true;
    }

    @Override
    public boolean onConfigureBuildTapped(BlockExtended.BuildExtended build, Building other) {
        Links links = build.getState(linksType);

        if(build == other){
            build.deselect();
            return false;
        }
        if(links == null) return true;

        if(links.validLink(build, other)){
            build.configure(other.pos());
            Log.info("Run");
            return false;
        }
        return true;
    }
}
