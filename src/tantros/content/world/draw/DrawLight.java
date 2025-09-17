package tantros.content.world.draw;

import arc.graphics.Color;
import arc.math.Mathf;
import mindustry.gen.Building;
import mindustry.graphics.Drawf;
import mindustry.world.draw.DrawBlock;

public class DrawLight extends DrawBlock {

    public float lightX = 0,
            lightY = 0,
            lightRadius = 60f,
            lightAlpha = 0.65f,
            lightSinScl = 10f,
            lightSinMag = 5;

    public Color flameColor = Color.valueOf("ffc999");



    @Override
    public void drawLight(Building build){
        Drawf.light(build.x + lightX, build.y + lightY, (lightRadius + Mathf.absin(lightSinScl, lightSinMag)) * build.warmup() * build.block.size, flameColor, lightAlpha);
    }
}
