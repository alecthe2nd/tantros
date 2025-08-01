package tantros;

import arc.Events;
import mindustry.game.EventType;
import mindustry.game.EventType.*;
import tantros.ai.types.BurrowAI;
import tantros.gen.BurrowerUnit;
import tantros.gen.Burrowerc;
import tantros.graphics.overlays.SonarTracking;
import tantros.world.environment.LocalEnvIndexer;

public class TantrosVars {

    public static LocalEnvIndexer envIndexer = new LocalEnvIndexer();

    public static SonarTracking sonarTracking;

    public static void load(){
        sonarTracking = new SonarTracking();

        Events.on(EventType.UnitDamageEvent.class, (event)->{
            if(event.unit instanceof BurrowerUnit burrower){
                BurrowAI.hit(burrower, event.bullet);
            }
        });
    }
}
