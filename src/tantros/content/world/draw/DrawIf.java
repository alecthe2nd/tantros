package tantros.content.world.draw;

import arc.graphics.g2d.TextureRegion;
import arc.struct.Seq;
import arc.util.Eachable;
import mindustry.entities.units.BuildPlan;
import mindustry.gen.Building;
import mindustry.world.Block;
import mindustry.world.draw.DrawBlock;

import java.util.function.Predicate;

public class DrawIf extends DrawBlock {

    public DrawBlock drawIf;

    public boolean defaultTo = false;

    public Predicate<Building> condition;

    public DrawIf(DrawBlock drawIf, Predicate<Building> condition){
        this.drawIf = drawIf;
        this.condition = condition;
    }

    public boolean conditionMet(Building build){
        return (condition == null) ? defaultTo: this.condition.test(build);
    }

    @Override
    public void getRegionsToOutline(Block block, Seq<TextureRegion> out){
        drawIf.getRegionsToOutline(block, out);
    }

    @Override
    public void draw(Building build){
        if(conditionMet(build)){
            drawIf.draw(build);
        }
    }

    @Override
    public void drawPlan(Block block, BuildPlan plan, Eachable<BuildPlan> list){
        drawIf.drawPlan(block, plan, list);
    }

    @Override
    public void drawLight(Building build){
        if(conditionMet(build)){
            drawIf.drawLight(build);
        }
    }

    @Override
    public void load(Block block){
        drawIf.load(block);
    }

    @Override
    public TextureRegion[] icons(Block block){
        return drawIf.icons(block);
    }
}