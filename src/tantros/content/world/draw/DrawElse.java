package tantros.content.world.draw;

import mindustry.gen.Building;
import mindustry.world.draw.DrawBlock;

import java.util.function.Predicate;

public class DrawElse extends DrawIf {

    public DrawElse(DrawBlock drawIf, Predicate<Building> condition){
        super(drawIf, condition);
    }

    public boolean conditionMet(Building build){
        return !super.conditionMet(build);
    }

}