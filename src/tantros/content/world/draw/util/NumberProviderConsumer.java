package tantros.content.world.draw.util;

import arc.func.Func;
import mindustry.gen.Building;

public interface NumberProviderConsumer {

    default float getValue(String name, Building building){
        if(building instanceof NumberProviderc.NumberProviderBuildc provider){
            return provider.get(name).get(building);
        }

        return 0f;
    }
}
