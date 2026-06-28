package tantros.content.blocks;

import mindustry.content.Blocks;
import mindustry.content.Items;
import mindustry.game.SpawnGroup;
import mindustry.game.Team;
import mindustry.world.Block;
import mindustry.world.blocks.environment.Floor;
import mindustry.world.blocks.environment.StaticWall;
import tantros.content.world.TantrosItems;
import tantros.content.TantrosUnitTypes;
import tantros.world.blocks.environment.AmbientSpawn;
import tantros.world.blocks.environment.TallSeaBush;
import tantros.world.blocks.environment.TallSeaBushMindusty;

public class TantrosEnvironment {



    public static Block

            testBoatSpawn,
            bleachmat,
            redmatWall,
            bluematWall,
            bleachmatWall,
            kelp,
            kelp_mindusty,
            beryllicSand,
            oxideWall
    ;

    public static void load(){

        testBoatSpawn = new AmbientSpawn("large-fisk-spawn", new SpawnGroup(TantrosUnitTypes.largeFisk){{
            team = Team.blue;
        }});


        Blocks.redmat.asFloor().itemDrop = TantrosItems.redcyst;
        Blocks.bluemat.asFloor().itemDrop = TantrosItems.bluecyst;
        bleachmat = new Floor("bleachmat"){{
            variants = 3;
        }};

        redmatWall = new StaticWall("redmat-wall"){{
            Blocks.redmat.asFloor().wall = this;
            variants = 3;
        }};
        bluematWall = new StaticWall("bluemat-wall"){{
            Blocks.bluemat.asFloor().wall = this;
            variants = 3;
        }};
        bleachmatWall = new StaticWall("bleachmat-wall"){{
            bleachmat.asFloor().wall = this;
            variants = 3;
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

        beryllicSand = new Floor("beryllic-sand"){{
            variants = 3;
        }};

        oxideWall = new StaticWall("oxide-wall"){{
            itemDrop = Items.oxide;
            variants = 3;
        }};
    }

}
