package tantros.net.packets;

import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.io.TypeIO;
import mindustry.net.Packet;
import mindustry.world.Tile;
import mindustry.world.blocks.defense.AutoDoor;
import tantros.world.blocks.defense.Door;

public class DoorTogglePacket extends Packet {
    private byte[] DATA;
    public Tile tile;
    public boolean open;

    public DoorTogglePacket() {
        this.DATA = NODATA;
    }

    public void write(Writes WRITE) {
        TypeIO.writeTile(WRITE, this.tile);
        WRITE.bool(this.open);
    }

    public void read(Reads READ, int LENGTH) {
        this.DATA = READ.b(LENGTH);
    }

    public void handled() {
        BAIS.setBytes(this.DATA);
        this.tile = TypeIO.readTile(READ);
        this.open = READ.bool();
    }

    public void handleClient() {
        Door.doorToggle(this.tile, this.open);
    }
}
