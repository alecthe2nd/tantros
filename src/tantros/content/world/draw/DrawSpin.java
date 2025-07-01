package tantros.content.world.draw;

import arc.Core;
import arc.graphics.g2d.TextureRegion;
import mindustry.gen.Building;
import mindustry.graphics.Drawf;
import mindustry.world.Block;
import mindustry.world.draw.DrawBlock;

public class DrawSpin extends DrawBlock {
    public TextureRegion region;
    public String suffix = "";
    public float rotateSpeed = 1f, x, y;

    public DrawSpin(String suffix, float speed){
        this.suffix = suffix;
        rotateSpeed = speed;
    }

    public DrawSpin(){
    }

    @Override
    public void draw(Building build){
        Drawf.spinSprite(region, build.x + x, build.y + y, build.totalProgress() * rotateSpeed);
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
