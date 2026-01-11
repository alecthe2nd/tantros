package tantros.world.draw.extended;

import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.struct.Seq;
import arc.util.Eachable;
import arc.util.Log;
import mindustry.entities.units.BuildPlan;
import mindustry.gen.Building;
import mindustry.world.Block;
import mindustry.world.draw.DrawBlock;
import mindustry.world.draw.DrawMulti;
import tantros.world.blocks.production.ProductionBlock;

public class DrawMultiExtended extends DrawBlockExtended{
    public DrawBlock[] drawers = {};

    public DrawMultiExtended(){
    }

    public DrawMultiExtended(DrawBlock... drawers){
        this.drawers = drawers;
    }

    public DrawMultiExtended(Seq<DrawBlock> drawers){
        this.drawers = drawers.toArray(DrawBlock.class);
    }

    @Override
    public void getRegionsToOutline(Block block, Seq<TextureRegion> out){
        for(var draw : drawers){
            draw.getRegionsToOutline(block, out);
        }
    }

    @Override
    public void draw(Building build){
        for(var draw : drawers){
            draw.draw(build);
        }
    }

    @Override
    public void drawPlan(Block block, BuildPlan plan, Eachable<BuildPlan> list){
        for(var draw : drawers){
            draw.drawPlan(block, plan, list);
        }
    }

    @Override
    public void drawLight(Building build){
        for(var draw : drawers){
            draw.drawLight(build);
        }
    }

    @Override
    public void load(Block block){
        for(var draw : drawers){
            draw.load(block);
        }
    }

    @Override
    public TextureRegion[] icons(Block block){
        var result = new Seq<TextureRegion>();
        for(var draw : drawers){
            result.addAll(draw.icons(block));
        }
        return result.toArray(TextureRegion.class);
    }

    @Override
    public void drawPlace(int x, int y, int rotation, boolean valid, ProductionBlock block) {
        for(var draw : drawers){
            if(draw instanceof DrawBlockExtended extendedDraw){
                extendedDraw.drawPlace(x,y,rotation,valid, block);
            }
        }
    }
}
