package tantros.content.world.draw;

import mindustry.gen.Building;
import mindustry.world.draw.DrawBlock;

public class DrawTeamRegion extends DrawBlock {

    @Override
    public void draw(Building build) {
        build.drawTeamTop();
    }
}
