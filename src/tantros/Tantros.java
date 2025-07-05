package tantros;

import arc.graphics.Color;
import mindustry.content.Planets;
import mindustry.graphics.g3d.HexMesh;
import mindustry.maps.planet.TantrosPlanetGenerator;
import mindustry.mod.*;
import mindustry.type.Planet;
import mindustry.world.meta.Env;
import tantros.content.TantrosBlocks;
import tantros.content.TantrosTechTree;
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
        TantrosBlocks.load();
        TantrosTechTree.load();
        TantrosUnitTypes.load();
    }
}
