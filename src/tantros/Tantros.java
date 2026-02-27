package tantros;

import arc.Core;
import arc.files.Fi;
import arc.scene.ui.TextButton;
import arc.scene.ui.layout.Table;
import arc.struct.ObjectMap;
import arc.util.Log;
import arc.util.Strings;
import mindustry.Vars;
import mindustry.Vars.*;
import mindustry.content.Planets;
import mindustry.content.TechTree;
import mindustry.core.GameState;
import mindustry.ctype.UnlockableContent;
import mindustry.game.Saves;
import mindustry.gen.Icon;
import mindustry.gen.Tex;
import mindustry.mod.*;
import mindustry.type.Planet;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.BaseDialog;
import mindustry.ui.dialogs.SettingsMenuDialog;
import tantros.ai.TantrosUnitCommands;
import tantros.content.blocks.*;
import tantros.content.planets.*;
import tantros.content.world.*;
import tantros.gen.*;
import tantros.content.recipes.TantrosRecipes;
import tantros.graphics.TantrosShaders;
import tantros.mod.ScriptInjector;
import tantros.net.TantrosCalls;
import tantros.ui.ClearPlanetsDialog;

import java.lang.reflect.Field;

import static arc.Core.app;
import static arc.Core.settings;
import static mindustry.Vars.*;

public class Tantros extends Mod{

    public static float waterToSteamConversion = 2f;

    public static ClearPlanetsDialog clearPlanetsDialog;

    @Override
    public void loadContent(){
        EntityRegistry.register();

        TantrosVars.load();
        TantrosItems.load();
        TantrosLiquids.load();
        TantrosUnitCommands.load();
        TantrosUnitTypes.load();
        TantrosRecipes.load();
        TantrosBlocks.load();
        TantrosOverride.override();
        TantrosCalls.initPackets();
        ScriptInjector.load(Vars.mods.getMod(this.getClass()), "packages.js");
    }

    @Override
    public void init() {
        super.init();
        ui.settings.graphics.checkPref("fast-parallax", Vars.mobile);
        ui.settings.graphics.checkPref("drill-assist-indicators", false);

        TantrosShaders.init();

        Log.info("Attempting to access data settings ui.");
        clearPlanetsDialog = new ClearPlanetsDialog();
        try {
            Field field = SettingsMenuDialog.class.getDeclaredField("dataDialog");
            field.setAccessible(true);
            BaseDialog dataDialog = (BaseDialog) field.get(ui.settings);
            dataDialog.cont.row();
            dataDialog.cont.table(Tex.button, t -> {
                t.defaults().size(280f, 60f).left();
                t.button(
                        "@setting.clear-specific-planet",
                        ()->{
                            clearPlanetsDialog.show();
                        }
                ).marginLeft(4);
            });
        } catch (Exception e){
            Log.err(e);
        }
    }
}
