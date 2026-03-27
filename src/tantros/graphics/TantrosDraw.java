package tantros.graphics;

import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;

import static arc.graphics.g2d.Draw.rect;
import static arc.math.Angles.randLenVectors;
import static mindustry.Vars.renderer;

public class TantrosDraw {

    /** Draws a sprite that should be light-wise correct, when rotated. Provided sprite must be symmetrical in shape. Extended from Anuken's Drawf class.*/
    public static void spinSprite(TextureRegion region, float x, float y, float r, float symmetryAngle){
        float a = Draw.getColorAlpha();
        r = Mathf.mod(r, symmetryAngle);
        Draw.rect(region, x, y, r);
        Draw.alpha(r / symmetryAngle*a);
        Draw.rect(region, x, y, r - symmetryAngle);
        Draw.alpha(a);
    }

    public static void bubble(float x, float y, float lifetime){
        rect(renderer.bubbles[Math.min((int)(renderer.bubbles.length * Mathf.curveMargin(lifetime, 0.11f, 0.06f)), renderer.bubbles.length - 1)], DrawPsuedoParrallax.xHeight(x, lifetime * 20), DrawPsuedoParrallax.yHeight(y, lifetime * 20));
    }
}
