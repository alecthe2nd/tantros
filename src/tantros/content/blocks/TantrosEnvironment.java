package tantros.content.blocks;

import mindustry.content.Blocks;
import mindustry.content.Items;
import mindustry.game.SpawnGroup;
import mindustry.game.Team;
import mindustry.world.Block;
import mindustry.world.blocks.environment.StaticWall;
import tantros.content.world.TantrosItems;
import tantros.content.TantrosUnitTypes;
import tantros.world.blocks.environment.AmbientSpawn;
import tantros.world.blocks.environment.TallSeaBush;
import tantros.world.blocks.environment.TallSeaBushMindusty;

public class TantrosEnvironment {



    public static Block

            testBoatSpawn,
            redmatWall,
            bluematWall,
            kelp,
            kelp_mindusty,
            kelp_parrallax,
            oxideWall
    ;

    public static void load(){

        testBoatSpawn = new AmbientSpawn("large-fisk-spawn", new SpawnGroup(TantrosUnitTypes.largeFisk){{
            team = Team.blue;
        }});


        Blocks.redmat.asFloor().itemDrop = TantrosItems.redcyst;
        Blocks.bluemat.asFloor().itemDrop = TantrosItems.bluecyst;

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
            levelsMin = 3;
            levelsMax = 7;
        }};


        kelp_mindusty = new TallSeaBushMindusty("kelp-mindusty"){{
            //bluemat.asFloor().decoration = this;
            lobesMin = 6;
            lobesMax = 6;
            levelsMin = 5;
            levelsMax = 5;
        }};

        oxideWall = new StaticWall("oxide-wall"){{
            itemDrop = Items.oxide;
            variants = 3;
        }};
    }

}
