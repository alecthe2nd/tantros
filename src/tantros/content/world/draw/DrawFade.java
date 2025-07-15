package tantros.content.world.draw;

import arc.graphics.g2d.Draw;
import mindustry.gen.Building;
import mindustry.world.draw.DrawBlock;
import mindustry.world.draw.DrawMulti;
import mindustry.world.draw.DrawRegion;
import tantros.content.world.draw.util.NumberProviderConsumer;

public class DrawFade extends DrawMulti implements NumberProviderConsumer {

    public String fadeSourceName;

    public float upperFade = 1f;

    public float lowerFade = 0f;

    public DrawFade( String suffix, String depthSourceName){
        this(new DrawRegion(suffix), depthSourceName);
    }

    public DrawFade(DrawBlock drawer, String depthSourceName){
        this.drawers = new DrawBlock[]{
                drawer
        };
        this.fadeSourceName = depthSourceName;
    }

    @Override
    public void draw(Building build) {
        float scaleFactor = getValue(fadeSourceName, build);
        Draw.alpha((scaleFactor * (upperFade - lowerFade)) + lowerFade);
        super.draw(build);
        Draw.color();
    }
}
