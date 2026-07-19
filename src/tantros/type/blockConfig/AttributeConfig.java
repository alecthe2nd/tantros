package tantros.type.blockConfig;

import mindustry.world.meta.Attribute;
import tantros.world.blocks.BlockExtended;

public class AttributeConfig implements BlockConfig{

    public Attribute attribute = Attribute.heat;

    public float minEfficiency = 0f;

    public float displayEfficiencyScale = 1;
    public float efficiencyScale = 1;

    public int size = 1;
    public boolean floating = false;

    public String attributeStateName;

    @Override
    public void apply(BlockExtended block) {
        this.size = block.size;
        this.floating = block.floating;
    }
}
