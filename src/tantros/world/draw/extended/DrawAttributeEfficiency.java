package tantros.world.draw.extended;

import arc.Core;
import tantros.type.blockConfig.AttributeConfig;
import tantros.world.blocks.BlockExtended;

public class DrawAttributeEfficiency extends DrawBlockExtended{

    @Override
    public void drawPlace(int x, int y, int rotation, boolean valid, BlockExtended block) {

        AttributeConfig config = block.getBlockConfig(AttributeConfig.class);
        if(config == null) return;

        block.drawPlaceText(Core.bundle.formatFloat("bar.efficiency", block.sumAttribute(config.attribute, x, y) * 100 * config.displayEfficiencyScale, 1), x, y, valid);
    }
}
