package tantros.ui;

import arc.scene.style.Drawable;

public class TantrosTex {

    public static Drawable chip;

    public static void load(){
        chip = arc.Core.atlas.drawable("chip");
    }
}
