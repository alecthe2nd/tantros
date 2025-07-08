package tantros.content.world.draw;

import mindustry.world.blocks.storage.CoreBlock;
import mindustry.world.draw.DrawDefault;
import mindustry.world.draw.DrawMulti;

public class DrawCore extends DrawMulti {

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

}
