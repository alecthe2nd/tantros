package tantros.graphics;

import arc.graphics.gl.Shader;
import mindustry.graphics.Shaders;

public class TantrosShaders {
    public static UndergroundRadarShader undergroundRadar;

    public static void load(){
        undergroundRadar = new UndergroundRadarShader();
    }

    public static class UndergroundRadarShader extends Shaders.LoadShader{

        public float fadeAmount;

        public UndergroundRadarShader() {
            super("undergroundRadar", "screenspace");
        }
    }
}
