package tantros.ai;

import arc.Events;
import arc.func.Boolf;
import arc.func.Cons;
import arc.struct.Seq;
import mindustry.Vars;
import mindustry.content.Blocks;
import mindustry.game.*;
import mindustry.world.Tile;
import tantros.ai.spawn.SpawnType;
import tantros.world.blocks.environment.GenericSpawnBlock;

import static mindustry.Vars.world;

public abstract class UnitSpawner {

    protected final Seq<Tile> spawns = new Seq<>(false);

    public SpawnType type = SpawnType.ambient;

    public boolean enabled = false;

    public UnitSpawner(){
        Events.on(EventType.WorldLoadEndEvent.class, e -> reset());
    }

    public boolean shouldSpawn(){
        return enabled && Vars.state.isPlaying();
    };

    public void reset(){
        enabled = world.isGenerating();
        spawns.clear();

        for(Tile tile : world.tiles){
            if(tile.overlay() instanceof GenericSpawnBlock spawn && spawn.type == this.type){
                spawns.add(tile);
            }
        }
    }

    public abstract void spawn(Tile tile);

    public boolean canSpawn(Tile tile){
        return true;
    }

    public boolean trySpawn(){
        boolean shouldSpawn = shouldSpawn();
        if(shouldSpawn){
            doSpawn();
        }
        return shouldSpawn;
    }

    public void doSpawn(){
        eachSpawn(this::spawn);
    }

    public void eachSpawn(Cons<Tile> action){
        this.eachSpawn(action, this::canSpawn);
    }

    public void eachSpawn(Cons<Tile> action, Boolf<Tile> condition){
        for(Tile spawn: spawns){
            if(condition.get(spawn)){
                action.get(spawn);
            }
        }
    }
}
