package tantros.world.draw.util;

import arc.func.Func;
import mindustry.gen.Building;

import java.util.function.Supplier;

public interface NumberProviderc {

    void setNumberSource(String name, Supplier<Func<Building, Float>> source);

    Supplier<Func<Building, Float>> getSource(String name);

    public interface NumberProviderBuildc {
        Func<Building, Float> get(String name);
    }
}
