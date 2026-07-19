package tantros.world.draw.extended;

import mindustry.gen.Building;
import mindustry.world.draw.DrawBlock;
import tantros.world.blocks.BlockExtended;

public class DrawBlockExtended extends DrawBlock {

    @Override
    public final void draw(Building build) {
        if(build instanceof BlockExtended.BuildExtended ext){
            this.draw(ext);
        }
        super.draw(build);
    }

    public void draw(BlockExtended.BuildExtended build){
        super.draw(build);
    }

    public void drawPlace(int x, int y, int rotation, boolean valid, BlockExtended block){

    }

    public void drawConfigure(BlockExtended.BuildExtended build){

    }

    public void drawSelect(BlockExtended.BuildExtended build){

    }

}
