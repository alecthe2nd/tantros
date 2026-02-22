package tantros.content.planets;

import mindustry.type.SectorPreset;

import static mindustry.content.Planets.tantros;

public class TantrosSectorPresets {
    public static SectorPreset embark,
    oldReef;

    public static void load(){
        embark = new SectorPreset("embark", tantros, 10){{
            alwaysUnlocked = true;
            addStartingItems = true;
            captureWave = 10;
            difficulty = 1;
            overrideLaunchDefaults = true;
        }};
        /*oldReef = new SectorPreset("old-reef", tantros, 83){{
            captureWave = 10;
            difficulty = 1;
            overrideLaunchDefaults = true;
        }};*/
    }
}
