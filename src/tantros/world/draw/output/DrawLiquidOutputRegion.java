package tantros.world.draw.output;

import arc.Core;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.util.Eachable;
import mindustry.entities.units.BuildPlan;
import mindustry.gen.Building;
import mindustry.type.LiquidStack;
import mindustry.world.Block;
import tantros.world.draw.util.DrawUtil;

import java.util.HashMap;

public class DrawLiquidOutputRegion implements DrawOutput {

    private final HashMap<String, TextureRegion[]> liquidOutputRegions = new HashMap<>();
    public boolean symmetricSprite;

    public DrawLiquidOutputRegion(){
        this(true);
    }

    public DrawLiquidOutputRegion(boolean symmetricSprite){
        this.symmetricSprite = symmetricSprite;
    }

    @Override
    public void draw(Building build, LiquidOutputRenderContext context){
        if(context.side() != -1 && context.liquid() != null && liquidOutputRegions.containsKey(context.liquid().name)){
            int realRot = (context.side() + build.rotation) % 4;
            DrawUtil.drawFlipped(liquidOutputRegions.get(context.liquid().name)[realRot > 1 ? 1 : 0], (region)->{
                Draw.rect(region, build.x, build.y, realRot * 90);
            }, false, !symmetricSprite && build.rotation % 2 == 0);
        }
    }

    @Override
    public void drawPlan(Block block, BuildPlan plan, Eachable<BuildPlan> list, LiquidOutputRenderContext context) {
        if(context.side() != -1 && context.liquid() != null && liquidOutputRegions.containsKey(context.liquid().name)){
            int realRot = (context.side() + plan.rotation) % 4;
            DrawUtil.drawFlipped(liquidOutputRegions.get(context.liquid().name)[realRot > 1 ? 1 : 0], (region)->{
                Draw.rect(region, plan.drawx(), plan.drawy(), realRot * 90);
            }, false, !symmetricSprite && plan.rotation % 2 == 0);

        }
    }

    @Override
    public void load(Block block){
        var crafter = expectCrafter(block);

        if(crafter.outputLiquids == null) return;

        for(LiquidStack output: crafter.outputLiquids){
            TextureRegion[] shades = new TextureRegion[2];
            for(int j = 1; j <= shades.length; j++){
                shades[j - 1] = Core.atlas.find(block.name + "-" + output.liquid.name + "-output" + j);
            }
            liquidOutputRegions.put(output.liquid.name, shades);
        }
    }

}
