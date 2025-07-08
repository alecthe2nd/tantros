package tantros.graphics.overlays;

import arc.Events;
import arc.struct.IntSeq;
import mindustry.game.EventType;

import java.util.HashSet;

public class SonarTracking {


    public HashSet<Integer> ores = new HashSet<>();

    public SonarTracking(){
        Events.run(EventType.Trigger.draw, this.ores::clear);
    }
}
