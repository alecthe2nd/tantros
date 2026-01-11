package tantros.world.draw;

import mindustry.gen.Building;
import mindustry.world.draw.DrawBlock;
import mindustry.world.draw.DrawMulti;

import java.util.function.Predicate;

public class DrawIfElse extends DrawMulti {

    public DrawIfElse(DrawBlock drawIf, DrawBlock drawElse, Predicate<Building> condition){
        this.drawers = new DrawBlock[]{
                new DrawIf(drawIf, condition),
                new DrawElse(drawElse, condition)
        };
    }
}
