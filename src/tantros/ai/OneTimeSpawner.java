package tantros.ai;

import arc.Events;
import arc.util.Log;
import mindustry.game.EventType;
import mindustry.game.Team;
import mindustry.gen.Call;
import mindustry.world.Block;
import mindustry.world.Tile;
import tantros.ai.spawn.SpawnType;
import tantros.world.blocks.environment.AmbientSpawn;
import tantros.world.blocks.environment.GenericSpawnBlock;

public class OneTimeSpawner extends UnitSpawner{

    @Override
    public boolean trySpawn() {
        boolean spawnSucceeded = super.trySpawn();
        enabled = false;
        return spawnSucceeded;
    }

    @Override
    public void spawn(Tile tile) {
        if(tile.overlay() instanceof GenericSpawnBlock spawn){
            spawn.group.createUnit(Team.sharded, tile.worldx(), tile.worldy(), 0, 1, (unit)->{
                unit.unloaded();
                Events.fire(new EventType.UnitSpawnEvent(unit));
            });
        }else{
            Log.warn("[" + tile + "] has attempted to spawn a unit, but does not have a spawn.");
        }
    }
}
