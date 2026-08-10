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

    public static void initPackets(){
        Net.registerPacket(DoorTogglePacket::new);
    }
}
