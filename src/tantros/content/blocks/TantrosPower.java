package tantros.content.blocks;

import mindustry.content.Fx;
import mindustry.content.Items;
import mindustry.content.Liquids;
import mindustry.type.Category;
import mindustry.type.LiquidStack;
import mindustry.world.Block;
import mindustry.world.blocks.power.ConsumeGenerator;
import mindustry.world.draw.*;
import mindustry.world.meta.Env;
import tantros.content.TantrosFx;
import tantros.content.world.TantrosLiquids;
import tantros.world.consumers.ConsumeEnv;
import tantros.world.environment.LocalEnv;

import static mindustry.type.ItemStack.with;

public class TantrosPower {

    public static Block

            tidalTurbine,
            steamTurbine,
            steamDynamo
                    ;

    public static void load(){

        tidalTurbine = new ConsumeGenerator("tidal-turbine"){{
            requirements(Category.power, with(Items.copper, 25, Items.lead, 10));
            powerProduction = 1f;
            customShadow = true;

            envEnabled = Env.underwater;
            drawer = new DrawMulti(
                    new DrawRegion("-rotator", 0.6f, true),
                    new DrawDefault());
            size = 6;
            consume(new ConsumeEnv(LocalEnv.with(Liquids.water)));
        }};

        steamTurbine = new ConsumeGenerator("steam-turbine"){{
            requirements(Category.power, with(Items.metaglass, 15, Items.copper, 10, Items.lead, 8));
            powerProduction = 80f/60f;
            envEnabled |= Env.underwater;

            drawer = new DrawMulti(
                    new DrawDefault(),
                    new DrawLiquidTile(),
                    new DrawBlurSpin("-rotator", 6f),
                    new DrawRegion("-top")
            );
            hasPower = true;
            hasLiquids = true;
            liquidCapacity = 12f;
            consumeLiquids(LiquidStack.with(TantrosLiquids.steam, 10f/60f));

            effectChance = 1/100f;
            generateEffect = TantrosFx.parallaxBubble;

            size = 1;
        }};

        steamDynamo = new ConsumeGenerator("steam-dynamo"){{
            requirements(Category.power, with(Items.metaglass, 50, Items.copper, 30, Items.titanium, 25));
            powerProduction = 960/60f;
            envEnabled |= Env.underwater;

            effectChance = 4/60f;

            generateEffect = TantrosFx.parallaxBubble;

            drawer = new DrawMulti(
                    new DrawRegion("-bottom"),
                    new DrawBlurSpin("-rotator", 12f),
                    new DrawLiquidTile(),
                    new DrawDefault(),
                    new DrawRegion("-top")
            );
            hasPower = true;
            hasLiquids = true;
            liquidCapacity = 100f;
            consumeLiquids(LiquidStack.with(TantrosLiquids.steam, 80f/60f));

            size = 2;
        }};

    }
}
