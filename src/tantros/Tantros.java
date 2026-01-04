package tantros;

import mindustry.Vars;
import mindustry.mod.*;
import tantros.ai.TantrosUnitCommands;
import tantros.content.blocks.*;
import tantros.content.planets.*;
import tantros.content.world.*;
import tantros.gen.*;
import tantros.content.recipes.TantrosRecipes;
import tantros.graphics.TantrosShaders;

public class Tantros extends Mod{

    public static float waterToSteamConversion = 2f;

    @Override
    public void loadContent(){
        EntityRegistry.register();

        TantrosVars.load();
        TantrosShaders.load();
        TantrosLiquids.load();
        TantrosUnitCommands.load();
        TantrosUnitTypes.load();
        TantrosRecipes.load();
        TantrosBlocks.load();
        TantrosOverride.override();
    }

    @Override
    public void init() {
        super.init();
        Vars.ui.settings.graphics.checkPref("fast-parallax", Vars.mobile);
    }
}
