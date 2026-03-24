package tantros.content.blocks;

import arc.graphics.Color;
import mindustry.content.Fx;
import mindustry.content.Items;
import mindustry.content.Liquids;
import mindustry.gen.Sounds;
import mindustry.type.Category;
import mindustry.type.LiquidStack;
import mindustry.world.Block;
import mindustry.world.blocks.power.BeamNode;
import mindustry.world.blocks.power.ConsumeGenerator;
import mindustry.world.draw.*;
import mindustry.world.meta.Env;
import tantros.content.TantrosFx;
import tantros.content.world.TantrosLiquids;
import tantros.type.production.ProducePower;
import tantros.world.blocks.BlockExtended;
import tantros.world.blocks.production.ProductionBlock;
import tantros.world.consumers.ConsumeEnv;
import tantros.world.draw.extended.DrawMultiExtended;
import tantros.world.environment.LocalEnv;

import static mindustry.type.ItemStack.with;

public class TantrosPower {

    public static Block

            tidalTurbine,
            steamTurbine,
            steamDynamo,
            sealedBeamNode,
            steamEngineVanilla,
            steamEngine
                    ;

    public static void load(){


        sealedBeamNode = new BeamNode("sealed-beam-node"){{
            requirements(Category.power, with(Items.oxide, 4, Items.lead, 4));
            consumesPower = outputsPower = true;
            health = 90;
            range = 8;
            buildCostMultiplier = 2.5f;

            consumePowerBuffered(1000f);
        }};

        tidalTurbine = new ConsumeGenerator("tidal-turbine"){{
            requirements(Category.power, with(Items.copper, 50, Items.oxide, 40, Items.lead, 30));
            powerProduction = 1f/2f;
            //customShadow = true;

            buildCostMultiplier = 3;

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

        steamEngineVanilla = new ConsumeGenerator("steam-engine-vanilla"){{
                requirements(Category.power, with(Items.graphite, 40));
                powerProduction = 550f / 60f;
                consumeLiquids(LiquidStack.with(TantrosLiquids.steam, 5f / 60f));
                size = 3;
                drawer = new DrawMulti(new DrawRegion("-bottom"), new DrawPistons() {{
                    sinMag = 3f;
                    sinScl = 5f;
                    sideOffset = 30f;
                }}/*, new DrawRegion("-mid"), new DrawLiquidTile(Liquids.arkycite, 37f / 4f), new DrawDefault(), new DrawGlowRegion(){{
                alpha = 1f;
                glowScale = 5f;
                color = Color.valueOf("c967b099");
            }}*/);
                generateEffect = Fx.none;

                liquidCapacity = 20f * 5;

                ambientSound = Sounds.loopSmelter;
                ambientSoundVolume = 0.06f;

                warmupSpeed = 0.0005f;
        }};

        steamEngine = new ProductionBlock("steam-engine"){{
            requirements(Category.power, with(Items.graphite, 40));
            ProducePower powerOut = new ProducePower(550f/60f);
            powerOut.powerOutput = (b)-> powerOut.maxPower * b.warmup();
            produce(powerOut);
            consumeLiquids(LiquidStack.with(TantrosLiquids.steam, 5f / 60f));
            size = 3;
            drawer = new DrawMultiExtended(new DrawRegion("-bottom"), new DrawPistons() {{
                sinMag = 3f;
                sinScl = 5f;
                sideOffset = 180f;
                angleOffset = 45f;
            }}, new DrawRegion("-flywheel", 3f, true)
                    /*, new DrawRegion("-mid"), new DrawLiquidTile(Liquids.arkycite, 37f / 4f)*/, new DrawDefault()/*, new DrawGlowRegion(){{
                alpha = 1f;
                glowScale = 5f;
                color = Color.valueOf("c967b099");
            }}*/);

            liquidCapacity = 20f * 5;

            ambientSound = Sounds.loopSmelter;
            ambientSoundVolume = 0.06f;
            warmupEffectsProduction = true;
            warmupSpeed = 0.0005f;
        }};
    }
}
