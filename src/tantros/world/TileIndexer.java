package tantros.world;

import arc.Events;
import arc.util.Log;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.world.Tile;
import tantros.world.environment.LocalEnv;

public abstract class TileIndexer {

    public TileIndexer(){
        Events.on(EventType.TilePreChangeEvent.class, this::onTilePreChange);

        Events.on(EventType.TileChangeEvent.class, this::onTileChange);

        Events.on(EventType.WorldLoadEvent.class, this::onWorldLoadEvent);
    }

    public abstract void onTilePreChange(EventType.TilePreChangeEvent event);

    public abstract void onTileChange(EventType.TileChangeEvent event);

    public abstract void onWorldLoadEvent(EventType.WorldLoadEvent event);
}
