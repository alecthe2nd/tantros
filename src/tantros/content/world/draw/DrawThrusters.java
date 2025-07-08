package tantros.content.world.draw;

import arc.graphics.g2d.Draw;
import arc.math.Interp;
import mindustry.gen.Building;
import mindustry.world.blocks.storage.CoreBlock;
import mindustry.world.draw.DrawBlock;

public class DrawThrusters extends DrawBlock {

    public float alpha = -1;

    @Override
    public void draw(Building build) {
        if (build instanceof CoreBlock.CoreBuild core && core.thrusterTime > 0){
            if (alpha >= 0) {
                Draw.alpha(alpha);
            } else {
                Draw.alpha(Interp.pow4In.apply(core.thrusterTime));
            }
            core.drawThrusters(core.thrusterTime);
            Draw.reset();
        }
    }
}
