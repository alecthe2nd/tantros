package tantros.world.draw.util;

import arc.func.Func;
import arc.func.Prov;
import arc.math.Mathf;
import mindustry.gen.Building;

import java.util.function.Function;
import java.util.function.Supplier;

public class WarmupCooldownProvider implements Func<Building,Float>, Function<Building,Float> {

    public float warmup = 0f;

    public float warmupSpeed = 0.1f;

    @Override
    public Float get(Building building) {
        return apply(building);
    }

    @Override
    public Float apply(Building building) {
        if(building.efficiency > 1.0E-7F){
            warmup = Mathf.approachDelta(warmup, 1f, warmupSpeed);
        } else {
            warmup = Mathf.approachDelta(warmup, 0f, warmupSpeed);
        }
        return warmup;
    }
}
