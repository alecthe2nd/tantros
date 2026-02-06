package tantros.net;

import arc.Events;
import mindustry.Vars;
import mindustry.entities.Units;
import mindustry.game.EventType;
import mindustry.gen.Building;
import mindustry.gen.Player;
import mindustry.gen.TileConfigCallPacket;
import mindustry.net.Administration;
import mindustry.net.Net;
import mindustry.net.NetConnection;
import mindustry.net.ValidateException;
import mindustry.world.Tile;
import tantros.net.packets.BuildConfigCallPacket;
import tantros.net.packets.DoorTogglePacket;
import tantros.type.buildConfig.BuildConfigurationUnit;
import tantros.world.blocks.defense.Door;

import static mindustry.Vars.net;
import static mindustry.Vars.netServer;

public class TantrosCalls {

    public static void doorToggle(Tile tile, boolean open) {
        if (Vars.net.server() || !Vars.net.active()) {
            Door.doorToggle(tile, open);
        }

        if (Vars.net.server()) {
            DoorTogglePacket packet = new DoorTogglePacket();
            packet.tile = tile;
            packet.open = open;
            Vars.net.send(packet, true);
        }
    }

    public static void tileConfig(Player player, Building build, BuildConfigurationUnit value){
        inputTileConfig(player, build, value);
        if (Vars.net.server() || Vars.net.client()) {
            BuildConfigCallPacket packet = new BuildConfigCallPacket();
            if (Vars.net.server()) {
                packet.player = player;
            }

            packet.build = build;
            packet.value = value;
            Vars.net.send(packet, true);
        }
    }

    public static void tileConfig__forward(NetConnection exceptConnection, Player player, Building build, BuildConfigurationUnit value) {
        if (Vars.net.server() || Vars.net.client()) {
            BuildConfigCallPacket packet = new BuildConfigCallPacket();
            if (Vars.net.server()) {
                packet.player = player;
            }

            packet.build = build;
            packet.value = value;
            Vars.net.sendExcept(exceptConnection, packet, true);
        }
    }

    public static void inputTileConfig(Player player, Building build, BuildConfigurationUnit value){
        if(build == null && net.server()) throw new ValidateException(player, "building is null");
        if(build == null) return;

        if(net.server() && (!Units.canInteract(player, build) ||
                !netServer.admins.allowAction(player, Administration.ActionType.configure, build.tile, action -> action.config = value))){

            if(player.con != null){
                Object old = build.config();
                if(old instanceof BuildConfigurationUnit buildConfig) {
                    BuildConfigCallPacket packet;
                    packet = new BuildConfigCallPacket(); //undo the config on the client
                    packet.player = player;
                    packet.build = build;
                    packet.value = buildConfig;
                    player.con.send(packet, true);
                } else {
                    TileConfigCallPacket packet;
                    packet = new TileConfigCallPacket(); //undo the config on the client
                    packet.player = player;
                    packet.build = build;
                    packet.value = old;
                    player.con.send(packet, true);
                }
            }

            if(!player.isLocal()){
                throw new ValidateException(player, "Player cannot configure a tile.");
            }else{
                return;
            }
        }
        if(player != null) build.updateLastAccess(player);
        build.configured(player == null || player.dead() ? null : player.unit(), value);
        Events.fire(new EventType.ConfigEvent(build, player, value));
    }

    public static void initPackets(){
        Net.registerPacket(DoorTogglePacket::new);
    }
}
