package tantros.world.blocks.defense;

import arc.struct.Seq;
import mindustry.world.Tile;
import mindustry.world.blocks.defense.AutoDoor;

public interface Door {
    default void open() {
        setOpen(true);
    }

    default void close() {
        setOpen(true);
    }
    boolean isOpen();
    void setOpen(boolean open);

    static void doorToggle(Tile tile, boolean open){
        if(tile == null || !(tile.build instanceof Door door)) return;
        door.setOpen(open);
    }
    Tile tile();
    void updateChained();
    Seq<Door> chained();
    void chained(Seq<Door> chained);
}
