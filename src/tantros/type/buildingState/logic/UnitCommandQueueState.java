package tantros.type.buildingState.logic;

import arc.math.geom.Position;
import arc.math.geom.Rect;
import arc.math.geom.Vec2;
import arc.struct.Seq;
import mindustry.Vars;
import mindustry.game.Teams;
import mindustry.gen.*;
import mindustry.world.Tile;
import tantros.type.buildConfig.ClearQueueConfig;
import tantros.type.buildConfig.ClearUnitsConfig;
import tantros.type.buildingState.BuildingState;
import tantros.util.io.ReadContext;
import tantros.util.io.WriteContext;
import tantros.world.blocks.BlockExtended;

import static mindustry.Vars.player;
import static mindustry.Vars.state;

public class UnitCommandQueueState implements BuildingState {

    public static final int maxSize = 50;

    public static final Rect rect = new Rect();
    final static Seq<Unit> tmpUnits = new Seq<>(false);

    public Seq<Position> commandQueue = new Seq<>(5);

    @Override
    public void initState(BlockExtended ownerType, BlockExtended.BuildExtended owner) {

    }

    @Override
    public void update(BlockExtended ownerType, BlockExtended.BuildExtended owner) {
        if(commandQueue.any()){
            commandQueue.removeAll(e -> e instanceof Healthc h && !h.isValid());
        }
    }

    @Override
    public void onProximity(BlockExtended ownerType, BlockExtended.BuildExtended owner) {

    }

    @Override
    public void reset() {
        commandQueue.clear();
    }

    @Override
    public <E> void onConfig(BlockExtended.BuildExtended owner, E config) {
        if(config instanceof Integer pos){
            Tile tile = Vars.world.tile(pos);
            Position target = tile.build;
            tile.getBounds(rect);

            if(target == null) {
                Seq<Teams.TeamData> data = state.teams.present;
                for (int i = 0; i < data.size; i++) {
                    if (data.items[i].team != player.team()) {
                        data.items[i].tree().intersect(rect, tmpUnits);
                    }
                }

                target = tmpUnits.min(u -> !u.inFogTo(player.team()), u -> u.dst(owner) - u.hitSize / 2f);
            }
            if(target == null){
                target = new Vec2().set(tile);
            }
            if(commandQueue.size < maxSize && !commandQueue.contains(target)) {
                commandQueue.add(target);
            }
        }
        if(config instanceof ClearQueueConfig){
            this.commandQueue.clear();
        }
    }

    @Override
    public void write(WriteContext write) {

        write.b(commandQueue.size);
        for(var pos : commandQueue){
            if(pos instanceof Building b){
                write.b(0);
                write.i(b.pos());
            }else if(pos instanceof Unit u){
                write.b(1);
                write.i(u.id);
            }else if(pos instanceof Vec2 v){
                write.b(2);
                write.f(v.x);
                write.f(v.y);
            }else{
                //who put garbage in the command queue??
                write.b(3);
            }
        }
    }

    @Override
    public void read(ReadContext read) {
        commandQueue.clear();
        int length = read.ub();
        for(int i = 0; i < length; i++){
            int commandType = read.b();
            switch(commandType){
                case 0 -> {
                    var build = Vars.world.build(read.i());
                    if(build != null) commandQueue.add(build);
                }
                case 1 -> {
                    var unit = Groups.unit.getByID(read.i());
                    if(unit != null) commandQueue.add(unit);
                }
                case 2 -> {
                    commandQueue.add(new Vec2(read.f(), read.f()));
                }
                //otherwise disregard
            }
        }
    }
}
