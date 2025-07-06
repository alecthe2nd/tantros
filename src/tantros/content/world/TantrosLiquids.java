package tantros.content.world;

import arc.graphics.Color;
import mindustry.content.StatusEffects;
import mindustry.type.Liquid;

public class TantrosLiquids {
    public static Liquid steam;

    public static void load(){

        steam = new Liquid("steam", Color.valueOf("e6f6ff")){{
            gas = true;
            temperature = 0.65f;
            heatCapacity = 0;
            viscosity = 0.1f;
            flammability = 0f;
            explosiveness = 0.3f;
            effect = StatusEffects.wet;
        }};
    }
}
