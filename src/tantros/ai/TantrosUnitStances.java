package tantros.ai;

import mindustry.ai.UnitCommand;
import mindustry.ai.UnitStance;

public class TantrosUnitStances {

    public static UnitStance repairSentry, rebuildSentry;

    public static void load(){

        repairSentry = new UnitStance("repairSentry", "logic", null, true);

        rebuildSentry = new UnitStance("rebuildSentry", "logic", null, true);

    }

}
