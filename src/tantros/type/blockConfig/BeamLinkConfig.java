package tantros.type.blockConfig;

import arc.func.Boolf2;
import arc.struct.Bits;
import arc.util.Log;
import mindustry.gen.Building;
import tantros.world.blocks.BlockExtended;

public class BeamLinkConfig implements BlockConfig{

    public int range = 3;

    public Boolf2<BlockExtended.BuildExtended, Building> condition = (a,b)->true;

    public Bits dir = new Bits(4);

    private static final Bits allSet = new Bits(4);

    static{
        allSet.set(0, true);
        allSet.set(1, true);
        allSet.set(2, true);
        allSet.set(3, true);
    }

    public String beamStateName = "";

    @Override
    public void apply(BlockExtended block) {
        if(!dir.containsAll(allSet)){
            block.rotate = true;
        }
    }
}
