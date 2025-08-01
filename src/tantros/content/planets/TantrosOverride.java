package tantros.content.planets;

import mindustry.content.Planets;
import tantros.content.TantrosLoadouts;
import tantros.content.TantrosTechTree;
import tantros.content.blocks.TantrosBlocks;
import tantros.maps.planet.TantrosPlanetGenerator;

public class TantrosOverride {

    public static void override(){
        Planets.tantros.accessible = true;
        Planets.tantros.visible = true;
        Planets.tantros.alwaysUnlocked = true;
        Planets.tantros.defaultCore = TantrosBlocks.coreShell;
        TantrosTechTree.load();
        TantrosLoadouts.load();
        Planets.tantros.generator = new TantrosPlanetGenerator();
        Planets.tantros.generator.defaultLoadout = TantrosLoadouts.basicShell;
    }

}
