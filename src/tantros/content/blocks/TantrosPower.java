package tantros.content.blocks;

import mindustry.content.Items;
import mindustry.content.Liquids;
import mindustry.type.Category;
import mindustry.world.Block;
import mindustry.world.blocks.power.ConsumeGenerator;
import mindustry.world.draw.DrawDefault;
import mindustry.world.draw.DrawMulti;
import mindustry.world.draw.DrawRegion;
import mindustry.world.meta.Env;
import tantros.content.world.blocks.power.PassiveGenerator;
import tantros.content.world.draw.DrawSpin;
import tantros.world.consumers.ConsumeEnv;
import tantros.world.environment.LocalEnv;

import static mindustry.type.ItemStack.with;

public class TantrosPower {

    public static Block

            tidalTurbine
                    ;

    public static void load(){

        tidalTurbine = new ConsumeGenerator("tidal-turbine"){{
            requirements(Category.power, with(Items.copper, 25, Items.lead, 10));
            powerProduction = 1f;

            envEnabled = Env.underwater;
            drawer = new DrawMulti(
                    new DrawRegion("-rotator", 0.6f, true),
                    new DrawDefault());
            size = 6;
            consume(new ConsumeEnv(LocalEnv.with(Liquids.water)));
        }};

    }
}
