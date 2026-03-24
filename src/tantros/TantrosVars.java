package tantros;

import arc.Events;
import arc.func.Func;
import arc.graphics.g2d.TextureRegion;
import arc.struct.ObjectMap;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.game.EventType.*;
import mindustry.game.Team;
import mindustry.mod.Mods;
import tantros.ai.OneTimeSpawner;
import tantros.ai.spawn.SpawnType;
import tantros.gen.Burrowerc;
import tantros.graphics.overlays.SonarTracking;
import tantros.type.buildingState.CooldownState;
import tantros.type.buildingState.InputHeatState;
import tantros.world.BuildingIndexer;
import tantros.world.RangeIndexer;
import tantros.world.blocks.BlockExtended;
import tantros.world.blocks.effect.GroundPenetratingRadar;
import tantros.world.environment.LocalEnvIndexer;

public class TantrosVars {

    public static Tantros thisMod;

    public static Mods.LoadedMod modWrapper;

    public static LocalEnvIndexer envIndexer = new LocalEnvIndexer();

    public static SonarTracking sonarTracking;

    public static BuildingIndexer<GroundPenetratingRadar.GroundPenetratingRadarBuild> sonarIndexer = new BuildingIndexer<>(GroundPenetratingRadar.GroundPenetratingRadarBuild.class);

    public static OneTimeSpawner ambientSpawner;

    public static ObjectMap<Team, RangeIndexer> sonarAreas = new ObjectMap<>();
    public static Func<Team, RangeIndexer> fetchSonarIndexer = (team)->{
        if(!sonarAreas.containsKey(team)){
            sonarAreas.put(team, new RangeIndexer());
        }
        return sonarAreas.get(team);
    };

    public static final ObjectMap<String, TextureRegion> teamTex = new ObjectMap<>();

    public static void load(Tantros mod){
        thisMod = mod;
        modWrapper = Vars.mods.getMod(mod.getClass());

        sonarTracking = new SonarTracking();
        ambientSpawner = new OneTimeSpawner(){{
            type = SpawnType.ambient;
            Events.run(Trigger.afterGameUpdate, this::trySpawn);
        }};

        Events.on(EventType.UnitDamageEvent.class, (event)->{
            if(event.unit instanceof Burrowerc burrower){
                burrower.dislodge(event.bullet);
            }
        });
    }
}
