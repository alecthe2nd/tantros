package tantros.world.draw;

import mindustry.graphics.Layer;
import mindustry.world.draw.DrawRegion;

public class DrawLayeredRegion extends DrawRegion {

    public DrawLayeredRegion(String suffix){
        this(suffix, Layer.block);
    }

    public DrawLayeredRegion(String suffix, float layer){
        super(suffix);
        this.layer = layer;
    }
}
