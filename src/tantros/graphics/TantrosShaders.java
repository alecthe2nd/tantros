package tantros.graphics;

import arc.Core;
import arc.Events;
import arc.files.Fi;
import arc.graphics.Gl;
import arc.graphics.g2d.Draw;
import arc.graphics.gl.FrameBuffer;
import arc.graphics.gl.Shader;
import mindustry.game.EventType;
import mindustry.graphics.CacheLayer;
import mindustry.graphics.Shaders;

public class TantrosShaders {

    public static Shader radar;

    public static void init() {

        radar = new RadarShader();
    }

}
