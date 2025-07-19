package tantros.world.environment;

import arc.Events;
import arc.func.Cons;
import arc.func.Func;
import arc.math.geom.Position;
import arc.struct.Seq;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.gen.Building;
import mindustry.world.Tile;
import tantros.content.world.blocks.effect.GenericProjector;
import tantros.content.world.blocks.effect.GenericProjector.GenericProjectorBuild;
import tantros.content.world.blocks.effect.projector.EnvEmitter;
import tantros.content.world.blocks.effect.projector.ProjectorEmitter;

public class LocalEnvIndexer {

    public Seq<GenericProjectorBuild> localEnvs = new Seq<>();
    
    public LocalEnv defaultEnv;

    public LocalEnvIndexer(){

        Events.on(EventType.TilePreChangeEvent.class, this::onTilePreChange);

        Events.on(EventType.TileChangeEvent.class, this::onTileChange);

        Events.on(EventType.WorldLoadEvent.class, this::onWorldLoadEvent);
    }

    public void onTilePreChange(EventType.TilePreChangeEvent event){
        if (event.tile.build != null && event.tile.isCenter()) {
            doIfLocalEnvEmitter(event.tile.build, localEnvs::remove);
        }
    }

    public void onTileChange(EventType.TileChangeEvent event){
        if (event.tile.build != null && event.tile.isCenter()) {
            doIfLocalEnvEmitter(event.tile.build, localEnvs::add);
        }
    }

    public void onWorldLoadEvent(EventType.WorldLoadEvent event){
        localEnvs.clear();
        
        defaultEnv = LocalEnv.with(Vars.state.getPlanet().defaultEnv);
        for (Tile tile : Vars.world.tiles){
            if (tile.build != null){
                doIfLocalEnvEmitter(tile.build, localEnvs::add);
            }
        }

    }

    public void doIfLocalEnvEmitter(Building build, Cons<GenericProjectorBuild> action){
        if(build == null) return;
        if(build instanceof GenericProjectorBuild proj){
            if(isLocalEnvEmitter(proj)) {
                action.get(proj);
            }
        }
    }

    public boolean isLocalEnvEmitter(GenericProjectorBuild build){
        if(build.block instanceof GenericProjector proj){
            for(ProjectorEmitter<?> emitter: proj.emitters){
                if(emitter instanceof EnvEmitter){
                    return true;
                }
            }
        }
        return false;
    }

    public LocalEnv getLocalEnv(Position pos){
        LocalEnv env = null;
        for(GenericProjectorBuild build: localEnvs) {
            if (build.block instanceof GenericProjector proj){
                for (ProjectorEmitter<?> emitter : proj.emitters) {
                    if (emitter instanceof EnvEmitter envEmitter && emitter.inRange(build, pos)) {
                        if (env == null) {
                            env = new LocalEnv();
                        }
                        if(envEmitter.env != null) {
                            env.mergeIn(envEmitter.env);
                        }
                    }
                }
            }
        }
        return env;
    }

}
