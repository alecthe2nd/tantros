package tantros.net;

import mindustry.Vars;
import mindustry.gen.AutoDoorToggleCallPacket;
import mindustry.net.Net;
import mindustry.world.Tile;
import mindustry.world.blocks.defense.AutoDoor;
import tantros.net.packets.DoorTogglePacket;
import tantros.world.blocks.defense.Door;

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
