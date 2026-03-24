package tantros.content.world;

import arc.graphics.Color;
import mindustry.content.Items;
import mindustry.type.Item;

public class TantrosItems {
    public static Item
    bluecyst,
    redcyst
    ;

    public static void load(){
        bluecyst = new Item("bluecyst", Color.valueOf("4d426a")){{
            flammability = 1.15f;
            buildable = false;
        }};

        redcyst = new Item("redcyst", Color.valueOf("7e564a")){{
            flammability = 1f;
            explosiveness = 0.5f;
            buildable = false;
        }};
    }
}
