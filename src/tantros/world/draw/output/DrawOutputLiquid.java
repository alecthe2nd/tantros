package tantros.world.draw.output;

import arc.Core;
import arc.graphics.g2d.TextureRegion;
import arc.util.Eachable;
import mindustry.entities.units.BuildPlan;
import mindustry.gen.Building;
import mindustry.graphics.Drawf;
import mindustry.world.Block;
import tantros.world.draw.util.DrawUtil;

public class DrawOutputLiquid implements DrawOutput {

    private final TextureRegion[] outputRegions = new TextureRegion[2];;
    public boolean symmetricSprite = true;
    public String suffix = "-liquid";

    public float alpha = 1f;

    public DrawOutputLiquid(){

    }

    public DrawOutputLiquid(boolean symmetricSprite) {
        this();
        this.symmetricSprite = symmetricSprite;
    }

    public DrawOutputLiquid(String suffix){
        this();
        this.suffix = suffix;
    }

    public DrawOutputLiquid(String suffix, boolean symmetricSprite){
        this(symmetricSprite);
        this.suffix = suffix;
    }

    @Override
    public void draw(Building build, LiquidOutputRenderContext context){
        if(context.side() != -1){
            int realRot = (context.side() + build.rotation) % 4;
            DrawUtil.drawFlipped(outputRegions[realRot > 1 ? 1 : 0], (region)->{
                Drawf.liquid(region,
                        build.x,
                        build.y,
                        build.liquids.get(context.liquid()) / build.block.liquidCapacity * alpha,
                        context.liquid().color,
                        realRot * 90);
            }, false, !symmetricSprite && build.rotation % 2 == 0);
        }
    }

    @Override
    public void drawPlan(Block block, BuildPlan plan, Eachable<BuildPlan> list, LiquidOutputRenderContext context) {

    }

    @Override
    public void load(Block block){
        if(!block.hasLiquids){
            throw new RuntimeException("Block '" + block + "' has a DrawLiquidRegion, but hasLiquids is false! Make sure it is true.");
        }

        for(int j = 1; j <= outputRegions.length; j++){
            outputRegions[j - 1] = Core.atlas.find(block.name + suffix + "-output" + j);
        }
    }

}
