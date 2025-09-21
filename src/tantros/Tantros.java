package tantros;

import mindustry.mod.*;
import tantros.ai.TantrosUnitCommands;
import tantros.content.blocks.*;
import tantros.content.planets.*;
import tantros.content.world.*;
import tantros.gen.*;
import tantros.world.meta.TantrosRecipes;

public class Tantros extends Mod{

    public static float waterToSteamConversion = 2f;

    @Override
    public void loadContent(){
        EntityRegistry.register();

        TantrosVars.load();
        TantrosLiquids.load();
        TantrosUnitCommands.load();
        TantrosUnitTypes.load();
        TantrosRecipes.load();
        TantrosBlocks.load();
        TantrosOverride.override();
    }


}
