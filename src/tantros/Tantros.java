package tantros;

import arc.Events;
import arc.util.Log;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.game.Team;
import mindustry.gen.Tex;
import mindustry.mod.*;
import mindustry.ui.Fonts;
import mindustry.ui.dialogs.BaseDialog;
import mindustry.ui.dialogs.PlanetDialog;
import mindustry.ui.dialogs.SettingsMenuDialog;
import tantros.ai.TantrosUnitCommands;
import tantros.content.TantrosUnitTypes;
import tantros.content.blocks.*;
import tantros.content.planets.*;
import tantros.content.world.*;
import tantros.gen.*;
import tantros.content.recipes.TantrosRecipes;
import tantros.graphics.TantrosShaders;
import tantros.mod.ScriptInjector;
import tantros.net.TantrosCalls;
import tantros.ui.ClearPlanetsDialog;
import tantros.ui.TantrosFonts;

import java.lang.reflect.Field;

import static mindustry.Vars.*;

public class Tantros extends Mod{

    public static float waterToSteamConversion = 2f;

    public static ClearPlanetsDialog clearPlanetsDialog;


    public Tantros(){
        Events.on(EventType.ClientLoadEvent.class, e -> {
            TantrosFonts.loadFonts();
            Team.blue.emoji = Fonts.getUnicodeStr("archae");
        });
    }

    @Override
    public void loadContent(){
        EntityRegistry.register();

        TantrosVars.load(this);
        TantrosItems.load();
        TantrosLiquids.load();
        TantrosUnitCommands.load();
        TantrosUnitTypes.load();
        TantrosRecipes.load();
        TantrosBlocks.load();
        TantrosOverride.override();
        TantrosCalls.initPackets();
        ScriptInjector.load(TantrosVars.modWrapper, "packages.js");
        ScriptInjector.load(TantrosVars.modWrapper, "defaults.js");
    }

    @Override
    public void init() {
        super.init();

        ui.settings.graphics.checkPref("fast-parallax", Vars.mobile);
        ui.settings.graphics.checkPref("drill-assist-indicators", false);
        ui.settings.dev.checkPref("debug-sonar-renderer", false);
        ui.settings.dev.checkPref("planet-dialog.debug-select", PlanetDialog.debugSelect,(b)->{
            PlanetDialog.debugSelect = b;
        });
        ui.settings.dev.checkPref("planet-dialog.debug-sector-attack-edit", PlanetDialog.debugSectorAttackEdit,(b)->{
            PlanetDialog.debugSectorAttackEdit = b;
        });
        ui.settings.dev.checkPref("planet-dialog.show-sector-numbers", PlanetDialog.debugShowNumbers,(b)->{
            PlanetDialog.debugShowNumbers = b;
        });

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
