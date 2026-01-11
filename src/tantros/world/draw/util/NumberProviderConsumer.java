package tantros.world.draw.util;

import mindustry.gen.Building;

public interface NumberProviderConsumer {

    default float getValue(String name, Building building){
        if(building instanceof NumberProviderc.NumberProviderBuildc provider){
            return provider.get(name).get(building);
        }

        return 0f;
    }
}
