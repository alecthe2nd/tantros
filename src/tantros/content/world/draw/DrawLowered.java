package tantros.content.world.draw;

import arc.graphics.g2d.Draw;
import mindustry.gen.Building;
import mindustry.world.draw.DrawBlock;
import mindustry.world.draw.DrawMulti;
import mindustry.world.draw.DrawRegion;
import tantros.content.world.draw.util.NumberProviderConsumer;

public class DrawLowered extends DrawMulti implements NumberProviderConsumer {

    public String depthSourceName;

    public float upperScale = 1f;

    public float lowerScale = 0.1f;

    public float upperBrightness = 1f;

    public float lowerBrightness = 0.1f;

    public DrawLowered( String suffix, String depthSourceName){
        this(new DrawRegion(suffix), depthSourceName);
    }

    public DrawLowered( DrawBlock drawer, String depthSourceName){
        this.drawers = new DrawBlock[]{
                drawer
        };
        this.depthSourceName = depthSourceName;
    }

    @Override
    public void draw(Building build) {

        float scaleFactor = (1 - getValue(depthSourceName, build));

        Draw.scl((scaleFactor * (upperScale - lowerScale)) + lowerScale);
        Draw.colorl((scaleFactor * (upperBrightness - lowerBrightness)) + lowerBrightness);
        super.draw(build);
        Draw.color();
        Draw.scl();
    }
}
