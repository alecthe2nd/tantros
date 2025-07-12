package tantros.content.world.draw;

import arc.func.Cons;
import arc.func.Func;
import arc.graphics.g2d.Draw;
import arc.struct.Seq;
import arc.util.Eachable;
import arc.util.Timer;
import mindustry.entities.units.BuildPlan;
import mindustry.gen.Building;
import mindustry.world.Block;
import mindustry.world.draw.DrawBlock;
import mindustry.world.draw.DrawMulti;
import mindustry.world.draw.DrawRegion;

import java.util.function.Function;
import java.util.function.Supplier;

public class DrawLowered extends DrawMulti {

    public Func<Building, Float> depthSource;

    public float upperScale = 1f;

    public float lowerScale = 0.1f;

    public float upperBrightness = 1f;

    public float lowerBrightness = 0.1f;

    public DrawLowered( String suffix, Func<Building, Float> depthSource){
        this(new DrawRegion(suffix), depthSource);
    }

    public DrawLowered( DrawBlock drawer, Func<Building, Float> depthSource){
        this.drawers = new DrawBlock[]{
                drawer
        };
        this.depthSource = depthSource;
    }

    @Override
    public void draw(Building build) {
        float scaleFactor = (1 - depthSource.get(build));

        Draw.scl((scaleFactor * (upperScale - lowerScale)) + lowerScale);
        Draw.colorl((scaleFactor * (upperBrightness - lowerBrightness)) + lowerBrightness);
        super.draw(build);
        Draw.color();
        Draw.scl();
    }
}
