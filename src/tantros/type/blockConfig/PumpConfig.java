package tantros.type.blockConfig;

import arc.struct.Seq;
import mindustry.world.Tile;

public class PumpConfig implements BlockConfig{

    public static final Seq<Tile> tempTiles = new Seq<>();

    public boolean canPump(Tile tile){
        return tile != null && tile.floor().liquidDrop != null;
    }

}
