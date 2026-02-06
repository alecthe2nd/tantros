package tantros.type.buildingState.logic;

import arc.struct.Seq;
import mindustry.gen.Building;
import mindustry.world.blocks.ConstructBlock;
import tantros.world.blocks.BlockExtended;

import static mindustry.Vars.tilesize;

public interface Links {

    Seq<Link> getLinks();

    boolean validLink(BlockExtended.BuildExtended owner, Building other);

}
