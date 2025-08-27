package tantros.content.blocks;

import mindustry.content.Blocks;
import mindustry.game.SpawnGroup;
import mindustry.game.Team;
import mindustry.world.Block;
import mindustry.world.blocks.environment.StaticWall;
import tantros.content.world.TantrosUnitTypes;
import tantros.world.blocks.environment.AmbientSpawn;
import tantros.world.blocks.environment.TallSeaBush;

public class TantrosEnvironment {



    public static Block

            testBoatSpawn,
            redmatWall,
            bluematWall,
            kelp
    ;

    public static void load(){

        testBoatSpawn = new AmbientSpawn("large-fisk-spawn", new SpawnGroup(TantrosUnitTypes.largeFisk){{
            team = Team.blue;
        }});

        redmatWall = new StaticWall("redmat-wall"){{
            Blocks.redmat.asFloor().wall = this;
        }};
        bluematWall = new StaticWall("bluemat-wall"){{
            Blocks.bluemat.asFloor().wall = this;
        }};


        kelp = new TallSeaBush("kelp"){{
            //bluemat.asFloor().decoration = this;
            lobesMin = 4;
            lobesMax = 6;
        }};
    }

}
