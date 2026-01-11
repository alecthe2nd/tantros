package tantros.world.draw;

import arc.Core;
import arc.graphics.g2d.TextureRegion;
import arc.struct.Seq;
import mindustry.world.Block;
import mindustry.world.draw.*;

public class DrawIconOverride extends DrawMulti {

    public TextureRegion icon;

    public DrawIconOverride() {
        super();
    }

    public DrawIconOverride(DrawBlock... drawers) {
        super(drawers);
    }

    public DrawIconOverride(Seq<DrawBlock> drawers) {
        super(drawers);
    }

    @Override
    public TextureRegion[] icons(Block block) {
        return new TextureRegion[]{icon};
    }

    @Override
    public void load(Block block) {
        icon = Core.atlas.find(block.name + "-icon");
        super.load(block);
    }
}
