package tantros.type.blockConfig;

import arc.func.Boolf2;
import mindustry.gen.Building;
import tantros.world.blocks.BlockExtended;

public class BeamLinkConfig implements BlockConfig{

    public int range = 3;

    public Boolf2<BlockExtended.BuildExtended, Building> condition = (a,b)->true;

}
