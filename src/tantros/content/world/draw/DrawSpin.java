package tantros.content.world.draw;

import arc.Core;
import arc.func.Func;
import arc.graphics.g2d.TextureRegion;
import mindustry.gen.Building;
import mindustry.graphics.Drawf;
import mindustry.world.Block;
import mindustry.world.Build;
import mindustry.world.draw.DrawBlock;

import java.util.function.Supplier;

public class DrawSpin extends DrawBlock {
    public TextureRegion region;
    public String suffix = "";
    public float rotateSpeed = 1f, x, y;

    public Func<Building, Float> timeSource;


    public DrawSpin(String suffix, float speed){
        this(suffix, speed, null);
    }
    public DrawSpin(String suffix, float speed, Func<Building, Float> timeSource){
        this.suffix = suffix;
        rotateSpeed = speed;
        this.timeSource = timeSource;
    }

    public DrawSpin(){
    }

    @Override
    public void draw(Building build){
        if (timeSource == null){
            timeSource = Building::totalProgress;
        }
        Drawf.spinSprite(region, build.x + x, build.y + y, timeSource.get(build) * rotateSpeed);
    }

    @Override
    public TextureRegion[] icons(Block block){
        return new TextureRegion[]{region};
    }

    @Override
    public void load(Block block){
        region = Core.atlas.find(block.name + suffix);
    }
}
