package tantros.world.draw.util;

import arc.func.Cons;
import arc.graphics.g2d.TextureRegion;

public class DrawUtil {

    /**
     * Flips region, runs flippedRegionDraw passing in the flipped region,
     * the unflips region.
     * @param region
     * The region to flip
     * @param flippedRegionDraw
     * Drawing to do with the flipped region. The passed in region will be the
     * same region object as region, so avoid using that region instance in here.
     *
     */
    public static void drawFlipped(TextureRegion region, Cons<TextureRegion> flippedRegionDraw, boolean x, boolean y){
        region.flip(x && !region.isFlipX(),y && !region.isFlipY());
        flippedRegionDraw.get(region);
        region.flip(x && region.isFlipX(),y && region.isFlipY());
    }
}
