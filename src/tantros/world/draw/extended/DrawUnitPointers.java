package tantros.world.draw.extended;

import arc.math.geom.Vec2;
import mindustry.Vars;
import mindustry.gen.Building;
import mindustry.gen.Unit;
import mindustry.graphics.Drawf;
import tantros.type.buildingState.logic.UnitLinks;
import tantros.world.blocks.BlockExtended;

public class DrawUnitPointers extends DrawBlockExtended{

    public int lineLength = 4;
    public Vec2 temp = new Vec2();

    @Override
    public void draw(Building build) {
        if(!(build instanceof BlockExtended.BuildExtended owner))return;
        UnitLinks links = owner.getState(UnitLinks.class);
        if(links == null) return;
        for(Unit unit: links.unitLinks){
            temp.set(unit).sub(build).nor().scl(lineLength);
            Drawf.line(build.team.color, build.x, build.y, build.x + temp.x, build.y + temp.y);
        }
    }
}
