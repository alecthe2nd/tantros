package tantros.graphics;

import arc.math.Mathf;
import mindustry.Vars;

import static arc.Core.camera;

public class DrawPsuedoParrallax {

    // Imaginary height the camera is above the ground.
    // Effects how quickly the parallax effects the apparent langth
    public static final float CAMERA_HEIGHT = 500;

    public static float xHeight(float x, float height){
        if(height <= 0) return x;
        return x + xOffset(x, height);
    }

    public static float yHeight(float y, float height){
        if(height <= 0) return y;
        return y + yOffset(y, height);
    }

    public static float xOffset(float x, float height){
        float num = (x - camera.position.x) * height;
        float den = Mathf.sqrt(
                Mathf.pow(CAMERA_HEIGHT / Vars.renderer.getDisplayScale(), 2)
                        + Mathf.pow(x - camera.position.x, 2)
        );
        if (den == 0){
            return 0;
        }
        return num / den;
    }

    public static float yOffset(float y, float height){
        float num = (y - camera.position.y) * height;
        float den = Mathf.sqrt(
                Mathf.pow(CAMERA_HEIGHT / Vars.renderer.getDisplayScale(), 2)
                        + Mathf.pow(y - camera.position.y, 2)
        );
        if (den == 0){
            return 0;
        }
        return num / den;
    }

    public static float hScale(float height){
        return 1f + hMul(height);
    }

    public static float hMul(float height){
        return height * Vars.renderer.getDisplayScale();
    }
}
