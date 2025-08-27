package tantros.world.blocks.environment;

import mindustry.game.SpawnGroup;
import tantros.ai.spawn.SpawnType;

public class AmbientSpawn extends GenericSpawnBlock {

    public AmbientSpawn(String name, SpawnGroup group) {
        super(name, group, SpawnType.ambient);
    }

}
