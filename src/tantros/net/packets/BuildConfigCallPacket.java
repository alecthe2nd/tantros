package tantros.net.packets;

import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.Vars;
import mindustry.gen.Building;
import mindustry.gen.Player;
import mindustry.io.TypeIO;
import mindustry.net.NetConnection;
import mindustry.net.Packet;
import tantros.net.TantrosCalls;
import tantros.type.buildConfig.BuildConfigurationUnit;

public class BuildConfigCallPacket extends Packet {
    private byte[] DATA;
    public Player player;
    public Building build;
    public BuildConfigurationUnit value;

    public BuildConfigCallPacket() {
        this.DATA = NODATA;
    }

    public void write(Writes WRITE) {
        if (Vars.net.server()) {
            TypeIO.writeEntity(WRITE, this.player);
        }

        TypeIO.writeBuilding(WRITE, this.build);
        WRITE.i(value.getNameHash());
        value.write(WRITE);
    }

    public void read(Reads READ, int LENGTH) {
        this.DATA = READ.b(LENGTH);
    }

    public void handled() {
        BAIS.setBytes(this.DATA);
        if (Vars.net.client()) {
            this.player = (Player)TypeIO.readEntity(READ);
        }

        this.build = TypeIO.readBuilding(READ);
        int hash = READ.i();
        this.value = BuildConfigurationUnit.get(hash);
    }

    public void handleServer(NetConnection con) {
        if (con.player != null && !con.kicked) {
            Player player = con.player;
            TantrosCalls.inputTileConfig(player, this.build, this.value);
            TantrosCalls.tileConfig__forward(con, player, this.build, this.value);
        }
    }

    public void handleClient() {
        TantrosCalls.inputTileConfig(this.player, this.build, this.value);
    }
}
