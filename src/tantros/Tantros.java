package tantros;

import mindustry.content.Planets;
import mindustry.game.Schematic;
import mindustry.game.Schematics;
import mindustry.mod.*;
import mindustry.type.Planet;
import tantros.content.TantrosBlocks;
import tantros.content.TantrosTechTree;
import tantros.content.world.TantrosLiquids;
import tantros.content.world.TantrosUnitTypes;
import tantros.gen.*;
import tantros.graphics.overlays.SonarTracking;

public class Tantros extends Mod{



    public static Schematic basicShell;

    public static SonarTracking sonarTracking;

    @Override
    public void loadContent(){
        EntityRegistry.register();
        Planets.tantros.accessible = true;
        Planets.tantros.visible = true;
        Planets.tantros.alwaysUnlocked = true;
        TantrosLiquids.load();
        TantrosBlocks.load();
        Planets.tantros.defaultCore = TantrosBlocks.coreShell;
        TantrosTechTree.load();
        TantrosUnitTypes.load();
        basicShell = Schematics.readBase64("bXNjaAF4nGNgYWBhZmDJS8xNZeByzi9KtQrOSM3JYeBOSS1OLsosKMnMz2NgYGDLSUxKzSlmYIqOZWQQKknMKynKL9ZNBqrXLQarZ2BgBCEgAQBy2hSU");
        Planets.tantros.generator.defaultLoadout = basicShell;
        sonarTracking = new SonarTracking();
    }


}
