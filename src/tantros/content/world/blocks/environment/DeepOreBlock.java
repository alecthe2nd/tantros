package tantros.content.world.blocks.environment;

import arc.graphics.g2d.Draw;
import mindustry.graphics.Layer;
import mindustry.type.Item;
import mindustry.world.Tile;
import mindustry.world.blocks.environment.OreBlock;

public class DeepOreBlock extends OreBlock {



    public DeepOreBlock(String name, Item ore) {
        super(name, ore);
    }

    public DeepOreBlock(Item ore) {
        this("deep-ore-" + ore.name);
    }

    public DeepOreBlock(String name) {
        super(name);
    }

    @Override
    public void drawBase(Tile tile) {
    }

    public void drawDeep(Tile tile){
        Draw.z(Layer.floor);
        super.drawBase(tile);
    }
}
