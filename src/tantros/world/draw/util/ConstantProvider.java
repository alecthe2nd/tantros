package tantros.world.draw.util;

import arc.func.Func;
import mindustry.gen.Building;

import java.util.function.Function;

public class ConstantProvider  implements Func<Building,Float>, Function<Building,Float> {

    public float value = 0f;

    @Override
    public Float get(Building building) {
        return apply(building);
    }

    @Override
    public Float apply(Building building) {
        return value;
    }
}
