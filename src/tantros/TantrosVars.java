package tantros;

import arc.Events;
import mindustry.game.EventType;
import mindustry.game.EventType.*;
import tantros.ai.OneTimeSpawner;
import tantros.ai.spawn.SpawnType;
import tantros.ai.types.BurrowAI;
import tantros.gen.BurrowerUnit;
import tantros.gen.Burrowerc;
import tantros.graphics.overlays.SonarTracking;
import tantros.type.buildingState.CooldownState;
import tantros.type.buildingState.InputHeatState;
import tantros.world.BuildingIndexer;
import tantros.world.blocks.BlockExtended;
import tantros.world.blocks.effect.GroundPenetratingRadar;
import tantros.world.environment.LocalEnvIndexer;

public class TantrosVars {

    public static LocalEnvIndexer envIndexer = new LocalEnvIndexer();

    public static SonarTracking sonarTracking;

    public static BuildingIndexer<GroundPenetratingRadar.GroundPenetratingRadarBuild> sonarIndexer = new BuildingIndexer<>(GroundPenetratingRadar.GroundPenetratingRadarBuild.class);

    public static OneTimeSpawner ambientSpawner;

    public static void load(){
        sonarTracking = new SonarTracking();
        ambientSpawner = new OneTimeSpawner(){{
            type = SpawnType.ambient;
            Events.run(Trigger.afterGameUpdate, this::trySpawn);
        }};

        Events.on(EventType.UnitDamageEvent.class, (event)->{
            if(event.unit instanceof BurrowerUnit burrower){
                BurrowAI.hit(burrower, event.bullet);
            }
        });

        BlockExtended.order.add(CooldownState.class);

        BlockExtended.order.add(InputHeatState.class);
    }
}
