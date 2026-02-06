package tantros.world.draw.extended;

import mindustry.graphics.Drawf;
import tantros.type.blockConfig.RangeConfig;
import tantros.world.blocks.BlockExtended;

import static mindustry.Vars.tilesize;

public class DrawPlacementRange extends DrawBlockExtended{

    @Override
    public void drawPlace(int x, int y, int rotation, boolean valid, BlockExtended block) {
        RangeConfig range = block.getBlockConfig(RangeConfig.class);
        if(range == null) return;
        Drawf.circles(x*tilesize + block.offset, y*tilesize + block.offset, range.range);
    }
}
