package tantros.content.planets;

import mindustry.Vars;
import mindustry.content.SectorPresets;
import mindustry.mod.Mods;
import mindustry.type.SectorPreset;
import tantros.TantrosVars;

import static mindustry.content.Planets.tantros;

public class TantrosSectorPresets {
    public static SectorPreset embark,
    shallows,
    polarEdge,
    patrolOutpost,
    deepChannels;

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
            captureWave = 10;
            difficulty = 1;
            overrideLaunchDefaults = true;
        }};
        patrolOutpost = new SectorPreset("patrol-outpost", tantros, 49){{
            difficulty = 5;
            overrideLaunchDefaults = true;
        }};
        deepChannels = new SectorPreset("deep-channels", tantros, 3){{
            difficulty = 3;
            overrideLaunchDefaults = true;
            captureWave = 20;
        }};

        Vars.maps.all().each((m)->m.mod == TantrosVars.modWrapper && Vars.content.planet("tantros").sectors.contains(
                (s)->{
            return s.preset != null && s.preset.generator != null && s.preset.generator.map != null && s.preset.generator.map.name().equals(m.name());
        }), (m)->m.mod = null);
    }
}
