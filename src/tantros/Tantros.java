package tantros;

import mindustry.mod.*;
import mindustry.type.*;
import tantros.ai.TantrosUnitCommands;
import tantros.content.blocks.*;
import tantros.content.planets.*;
import tantros.content.world.*;
import tantros.gen.*;

public class Tantros extends Mod{

    public static float waterToSteamConversion = 6f;

    @Override
    public void loadContent(){
        EntityRegistry.register();

        TantrosVars.load();
        TantrosLiquids.load();
        TantrosUnitCommands.load();
        TantrosUnitTypes.load();
        TantrosBlocks.load();
        TantrosOverride.override();
    }


}
