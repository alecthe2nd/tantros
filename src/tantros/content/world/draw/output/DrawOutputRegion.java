package tantros.content.world.draw.output;

import arc.Core;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.util.Eachable;
import mindustry.entities.units.BuildPlan;
import mindustry.gen.Building;
import mindustry.type.LiquidStack;
import mindustry.world.Block;
import tantros.content.world.draw.util.DrawUtil;

import java.util.HashMap;

public class DrawOutputRegion implements DrawOutput {

    private final TextureRegion[] outputRegions = new TextureRegion[2];;
    public boolean symmetricSprite;
    public String suffix;

    public DrawOutputRegion(){
        this("", true);
    }

    public DrawOutputRegion(boolean symmetricSprite) {
        this("", symmetricSprite);
    }

    public DrawOutputRegion(String suffix){
        this(suffix, true);
    }

    public DrawOutputRegion(String suffix, boolean symmetricSprite){
        this.symmetricSprite = symmetricSprite;
        this.suffix = suffix;
    }

    @Override
    public void draw(Building build, LiquidOutputRenderContext context){
        if(context.side() != -1){
            int realRot = (context.side() + build.rotation) % 4;
            DrawUtil.drawFlipped(outputRegions[realRot > 1 ? 1 : 0], (region)->{
                Draw.rect(region, build.x, build.y, realRot * 90);
            }, false, !symmetricSprite && build.rotation % 2 == 0);
        }
    }

    @Override
    public void drawPlan(Block block, BuildPlan plan, Eachable<BuildPlan> list, LiquidOutputRenderContext context) {
        if(context.side() != -1){
            int realRot = (context.side() + plan.rotation) % 4;
            DrawUtil.drawFlipped(outputRegions[realRot > 1 ? 1 : 0], (region)->{
                Draw.rect(region, plan.drawx(), plan.drawy(), realRot * 90);
            }, false, !symmetricSprite && plan.rotation % 2 == 0);

        }
    }

    @Override
    public void load(Block block){
        for(int j = 1; j <= outputRegions.length; j++){
            outputRegions[j - 1] = Core.atlas.find(block.name + suffix + "-output" + j);
        }
    }

}
