package tantros.content.planets;

import mindustry.type.SectorPreset;

import static mindustry.content.Planets.tantros;

public class TantrosSectorPresets {
    public static SectorPreset embark,
    shallows,
    polarEdge;

    public static void load(){
        embark = new SectorPreset("embark", tantros, 10){{
            alwaysUnlocked = true;
            addStartingItems = true;
            captureWave = 10;
            difficulty = 1;
            overrideLaunchDefaults = true;
        }};
        shallows = new SectorPreset("shallows", tantros, 83){{
            captureWave = 10;
            difficulty = 1;
            overrideLaunchDefaults = true;
        }};
        polarEdge = new SectorPreset("polar-edge", tantros, 19){{
            captureWave = 21;
            difficulty = 1;
            overrideLaunchDefaults = true;
        }};
    }
}
