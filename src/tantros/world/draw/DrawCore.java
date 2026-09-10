package tantros.world.draw;

import arc.Core;
import arc.graphics.g2d.TextureRegion;
import mindustry.world.Block;
import mindustry.world.blocks.storage.CoreBlock;
import mindustry.world.draw.DrawDefault;
import mindustry.world.draw.DrawMulti;

public class DrawCore extends DrawMulti {

    public TextureRegion fullIcon;

    public DrawCore(){
        super(
                new DrawMulti(
                        new DrawIf(
                                new DrawThrusters(){{
                                    alpha = 1.0f;
                                }},
                                (build)-> build instanceof CoreBlock.CoreBuild core && core.thrusterTime > 0
                        ),
                        new DrawDefault(),
                        new DrawTeamRegion(),
                        new DrawIf(
                                new DrawThrusters(),
                                (build)-> build instanceof CoreBlock.CoreBuild core && core.thrusterTime > 0
                        )
                )
        );
    }

    public TextureRegion[] icons(Block block) {
        return new TextureRegion[]{fullIcon};
    }

    public void load(Block block){
        this.fullIcon = Core.atlas.find(block.name + "-full");
    }

}
