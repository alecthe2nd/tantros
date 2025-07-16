package tantros.content.world.draw.output;

import arc.util.Eachable;
import mindustry.entities.units.BuildPlan;
import mindustry.gen.Building;
import mindustry.world.Block;
import mindustry.world.blocks.production.GenericCrafter;

public interface DrawOutput {
    void draw(Building build, LiquidOutputRenderContext context);

    void drawPlan(Block block, BuildPlan plan, Eachable<BuildPlan> list, LiquidOutputRenderContext context);

    void load(Block block);

    default GenericCrafter expectCrafter(Block block) {
        if (!(block instanceof GenericCrafter crafter))
            throw new ClassCastException("This drawer requires the block to be a GenericCrafter. Use a different drawer.");
        return crafter;
    }
}
