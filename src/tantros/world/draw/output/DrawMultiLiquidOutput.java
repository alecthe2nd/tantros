package tantros.world.draw.output;

import arc.util.Eachable;
import mindustry.entities.units.BuildPlan;
import mindustry.gen.Building;
import mindustry.world.Block;
import mindustry.world.blocks.production.GenericCrafter;
import mindustry.world.draw.DrawBlock;

public class DrawMultiLiquidOutput extends DrawBlock {

    DrawOutput[] drawers;

    public DrawMultiLiquidOutput(DrawOutput... drawers){
        this.drawers = drawers;
    }

    @Override
    public void draw(Building build) {
        GenericCrafter crafter = expectCrafter(build.block);

        for(int i = 0; i < crafter.outputLiquids.length; i++){
            int side = i < crafter.liquidOutputDirections.length ? crafter.liquidOutputDirections[i] : -1;
            for (DrawOutput drawer: drawers) {
                drawer.draw(build, new LiquidOutputRenderContext(crafter.outputLiquids[i].liquid, side));
            }
        }
    }

    @Override
    public void drawPlan(Block block, BuildPlan plan, Eachable<BuildPlan> list) {
        GenericCrafter crafter = expectCrafter(block);

        for(int i = 0; i < crafter.outputLiquids.length; i++){
            int side = i < crafter.liquidOutputDirections.length ? crafter.liquidOutputDirections[i] : -1;
            for (DrawOutput drawer: drawers) {
                drawer.drawPlan(block, plan, list, new LiquidOutputRenderContext(crafter.outputLiquids[i].liquid, side));
            }
        }
    }

    @Override
    public void load(Block block) {
        for(DrawOutput drawer: drawers){
            drawer.load(block);
        }
    }
}
