package tantros.io;

import arc.util.io.Reads;
import mindustry.gen.Building;
import mindustry.io.TypeIO;

public class TantrosTypeIO {

    public static TypeIO.Boxed<Building> readBoxedBuilding(Reads reads){
        return new TypeIO.BuildingBox(reads.i());
    }

}
