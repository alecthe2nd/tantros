package tantros.world.draw;

import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.world.Block;
import mindustry.world.draw.DrawBlock;

public class DrawTeamRegion extends DrawBlock {

    @Override
    public void draw(Building build) {
        build.drawTeamTop();
    }
}
