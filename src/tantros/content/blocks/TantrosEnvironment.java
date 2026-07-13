package tantros.content.blocks;

import mindustry.content.Blocks;
import mindustry.content.Items;
import mindustry.game.SpawnGroup;
import mindustry.game.Team;
import mindustry.world.Block;
import mindustry.world.blocks.environment.Floor;
import mindustry.world.blocks.environment.SeaBush;
import mindustry.world.blocks.environment.Seaweed;
import mindustry.world.blocks.environment.StaticWall;
import tantros.content.world.TantrosItems;
import tantros.content.TantrosUnitTypes;
import tantros.world.blocks.environment.AmbientSpawn;
import tantros.world.blocks.environment.TallSeaBush;

public class TantrosEnvironment {



    public static Block

            testBoatSpawn,
            redmatBloom,
            bluematBloom,
            bleachmat,
            deadmat,
            redmatWall,
            bluematWall,
            bleachmatWall,
            deadmatWall,
            deadReadweed,
            deadPurBush,
            kelp,
            beryllicSand,
            oxideWall
    ;

    public static void load(){

        testBoatSpawn = new AmbientSpawn("large-fisk-spawn", new SpawnGroup(TantrosUnitTypes.largeFisk){{
            team = Team.blue;
        }});


        Blocks.redmat.asFloor().itemDrop = TantrosItems.redcyst;
        redmatBloom = new Floor("redmat-bloom"){{
            variants = 3;
            itemDrop = TantrosItems.redcyst;
        }};

        Blocks.bluemat.asFloor().itemDrop = TantrosItems.bluecyst;
        bluematBloom = new Floor("bluemat-bloom"){{
            variants = 3;
            itemDrop = TantrosItems.bluecyst;
        }};

        bleachmat = new Floor("bleachmat"){{
            variants = 3;
        }};
        deadmat = new Floor("deadmat"){{
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
        deadmatWall = new StaticWall("deadmat-wall"){{
            bleachmat.asFloor().wall = this;
            variants = 3;
        }};

        deadReadweed = new Seaweed("dead-redweed"){{
            variants = 3;
        }};

        deadPurBush = new SeaBush("dead-pur-bush"){{
            sclMin = 50f;
            sclMax = 100f;
            magMin = 1f;
            magMax = 5f;
        }};

        /*yellowCoral = new SeaBush("yellowcoral"){{
            lobesMin = 2;
            lobesMax = 3;
            magMax = 8f;
            magMin = 2f;
            origin = 0.3f;
            spread = 40f;
            sclMin = 60f;
            sclMax = 100f;
        }};*/


        kelp = new TallSeaBush("kelp"){{
            //bluemat.asFloor().decoration = this;
            lobesMin = 4;
            lobesMax = 6;
            levelsMin = 3;
            levelsMax = 7;
        }};

        beryllicSand = new Floor("beryllic-sand"){{
            variants = 3;
            itemDrop = Items.sand;
        }};

        oxideWall = new StaticWall("oxide-wall"){{
            itemDrop = Items.oxide;
            variants = 3;
        }};
    }

}
