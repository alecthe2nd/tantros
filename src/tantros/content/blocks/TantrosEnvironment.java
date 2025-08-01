package tantros.content.blocks;

import mindustry.content.Blocks;
import mindustry.world.Block;
import mindustry.world.blocks.environment.StaticWall;

public class TantrosEnvironment {



    public static Block

            //wall
            redmatWall,
            bluematWall
    ;

    public static void load(){

        redmatWall = new StaticWall("redmat-wall"){{
            Blocks.redmat.asFloor().wall = this;
        }};
        bluematWall = new StaticWall("bluemat-wall"){{
            Blocks.bluemat.asFloor().wall = this;
        }};
    }

}
