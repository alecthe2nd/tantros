package tantros.content.world;

import mindustry.type.Item;

public class TantrosItems {
    public static Item
    bluecyst,
    redcyst,
    waste
    ;

    public static void load(){
        bluecyst = new Item("bluecyst"){{
            flammability = 1.15f;
            buildable = false;
        }};

        redcyst = new Item("redcyst"){{
            flammability = 1f;
            explosiveness = 0.5f;
            buildable = false;
        }};

        waste = new Item("waste"){{
            flammability = 0.4f;
            buildable = false;
        }};
    }
}
