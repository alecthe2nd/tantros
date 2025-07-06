package tantros;

import mindustry.content.Planets;
import mindustry.mod.*;
import mindustry.type.Planet;
import tantros.content.TantrosBlocks;
import tantros.content.TantrosTechTree;
import tantros.content.world.TantrosLiquids;
import tantros.content.world.TantrosUnitTypes;
import tantros.gen.*;

public class Tantros extends Mod{

    public static Planet tantros;

    @Override
    public void loadContent(){
        EntityRegistry.register();
        Planets.tantros.accessible = true;
        Planets.tantros.visible = true;
        Planets.tantros.alwaysUnlocked = true;
        TantrosLiquids.load();
        TantrosBlocks.load();
        TantrosTechTree.load();
        TantrosUnitTypes.load();
    }
}
