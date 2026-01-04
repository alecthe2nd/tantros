package tantros.world;

import arc.func.Boolf;
import arc.math.geom.Position;
import arc.struct.Seq;
import arc.util.Log;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.gen.Building;
import mindustry.world.Tile;
import tantros.world.blocks.effect.GenericProjector;
import tantros.world.blocks.effect.projector.EnvEmitter;
import tantros.world.blocks.effect.projector.ProjectorEmitter;
import tantros.world.environment.LocalEnv;

public class BuildingIndexer<E extends Building> extends TileIndexer {
    
    
    private final Class<E> buildType;

    public BuildingIndexer(Class<E> buildType){
        super();
        this.buildType = buildType;
    }

    public Seq<E> index = new Seq<>();

    public Boolf<E> buildCondition = (b)-> true;

    public Boolf<Tile> tileCondition = (b)-> true;

    public boolean checkBuildCondition(Tile tile){
        E subject = unpack(tile);
        return subject != null && buildCondition.get(subject);
    }

    public boolean checkTileCondition(Tile tile){
        return checkBuildCondition(tile) && tile.isCenter() && tileCondition.get(tile);
    }

    public E unpack(Tile tile){
        E subject = null;
        if(buildType.isInstance(tile.build)){
            subject = buildType.cast(tile.build);
        }
        return subject;
    }

    @Override
    public void onTilePreChange(EventType.TilePreChangeEvent event) {
        if(checkTileCondition(event.tile)){
            index.remove(unpack(event.tile));
        }
    }

    @Override
    public void onTileChange(EventType.TileChangeEvent event) {
        if(checkTileCondition(event.tile)){
            index.add(unpack(event.tile));
        }
    }

    @Override
    public void onWorldLoadEvent(EventType.WorldLoadEvent event) {
        index.clear();
        Log.log(Log.LogLevel.info, "Loaded '" , buildType,"' building index");
        for (Tile tile : Vars.world.tiles){
            if (checkTileCondition(tile)){
                index.add(unpack(tile));
            }
        }
    }



    public E get(Boolf<E> filter){
        for(E build: index) {
            if(filter.get(build)){
                return build;
            }
        }
        return null;
    }
}


