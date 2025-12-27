package tantros.content.blocks;

import arc.graphics.Color;
import arc.math.Interp;
import arc.struct.Seq;
import mindustry.content.Fx;
import mindustry.content.Items;
import mindustry.content.Liquids;
import mindustry.entities.effect.MultiEffect;
import mindustry.gen.Sounds;
import mindustry.graphics.Pal;
import mindustry.type.Category;
import mindustry.type.LiquidStack;
import mindustry.world.Block;
import mindustry.world.blocks.heat.HeatProducer;
import mindustry.world.blocks.production.GenericCrafter;
import mindustry.world.draw.*;
import mindustry.world.meta.BlockGroup;
import mindustry.world.meta.Env;
import tantros.content.TantrosFx;
import tantros.content.world.TantrosLiquids;
import tantros.world.blocks.production.Boiler;
import tantros.content.world.draw.DrawIconOverride;
import tantros.content.world.draw.DrawLight;
import tantros.content.world.draw.output.DrawLiquidOutputRegion;
import tantros.content.world.draw.output.DrawMultiLiquidOutput;
import tantros.content.world.draw.output.DrawOutputLiquid;
import tantros.content.world.draw.output.DrawOutputRegion;
import tantros.world.blocks.production.RecipeCrafter;
import tantros.world.consumers.ConsumeRecipes;
import tantros.content.recipes.TantrosRecipes;

import static mindustry.type.ItemStack.with;

public class TantrosProduction {

    public static Block
            metaglassAnnealer,
            graphiticDecomposer,
            siliconPressureSmelter,
            electrolysisChamber,
            combustionHeater,
            hydrogenCatalysisHeater,
            sealedElectricHeater,
            oxidizationHeater,
            copperBoiler
                    ;

    public static void load(){

        metaglassAnnealer = new RecipeCrafter("metaglass-annealer"){{
            requirements(Category.crafting, with(Items.copper, 80, Items.oxide, 40));
            cons = new ConsumeRecipes(Seq.with(
                    TantrosRecipes.metaglassAnnealing,
                    TantrosRecipes.surgeAnnealing,
                    TantrosRecipes.slagSurgeAnnealing
            ));
            craftEffect = TantrosFx.parallaxBubble;
            size = 3;
            envEnabled |= Env.underwater;
            envDisabled = Env.none;
            itemCapacity = 30;
            drawer = new DrawMulti(new DrawDefault(), new DrawWarmupRegion());
            fogRadius = 3;
            ambientSound = Sounds.loopSmelter;
            ambientSoundVolume = 0.12f;
        }};

        graphiticDecomposer = new RecipeCrafter("graphitic-decomposer"){{
            requirements(Category.crafting, with(Items.copper, 40, Items.oxide, 20, Items.metaglass, 30));
            cons = new ConsumeRecipes(Seq.with(TantrosRecipes.coalDecomposition));
            craftEffect = Fx.none;
            size = 2;
            itemCapacity = 30;
            liquidCapacity = 30;

            drawer = new DrawMulti(
                    new DrawRegion("-bottom"),
                    new DrawLiquidTile(Liquids.hydrogen),
                    new DrawParticles(){{
                        color = Liquids.hydrogen.color;
                        alpha = 0.5f;
                        particleSize = 3f;
                        particles = 10;
                        particleRad = 4f;
                        particleLife = 200f;
                        reverse = true;
                        particleSizeInterp = Interp.one;
                    }},
                    new DrawDefault());
            ignoreLiquidFullness = true;
        }};

        siliconPressureSmelter = new RecipeCrafter("silicon-pressure-smelter"){{
            requirements(Category.crafting, with(Items.copper, 30, Items.oxide, 20, Items.metaglass, 40, Items.graphite, 50));
            cons = new ConsumeRecipes(Seq.with(TantrosRecipes.siliconPressureSmelting));
            craftEffect = Fx.none;
            size = 3;
            envEnabled |= Env.space | Env.underwater;
            envDisabled = Env.none;
            itemCapacity = 30;
            liquidCapacity = 30;
            drawer = new DrawMulti(new DrawRegion("-bottom"), new DrawArcSmelt(), new DrawDefault());
            squareSprite = false;
            fogRadius = 3;
            ambientSound = Sounds.loopSmelter;
            ambientSoundVolume = 0.12f;
        }};

        combustionHeater = new RecipeCrafter("combustion-heater"){
            {
                requirements(Category.crafting, with(Items.copper, 20, Items.oxide, 10, Items.metaglass, 30));
                craftEffect = new MultiEffect(TantrosFx.parallaxBubble,TantrosFx.parallaxBubble,TantrosFx.parallaxBubble,TantrosFx.parallaxBubble);
                cons = new ConsumeRecipes(Seq.with(
                        TantrosRecipes.coalCombustion,
                        TantrosRecipes.graphiteCombustion,
                        TantrosRecipes.hydrogenCombustion
                ));
                size = 2;
                drawer = new DrawMulti(
                        new DrawRegion("-bottom"),
                        new DrawWarmupRegion(){{
                            color = Pal.darkFlame;
                        }},
                        new DrawParticles(){{
                            color = Color.valueOf("ed8e38");
                            reverse = true;
                            particleSize = 2f;
                            particles = 20;
                            particleRad = 8f;
                            particleLife = 60f;
                        }},
                        new DrawDefault(),
                        new DrawHeatOutput(){{
                            glowMult = 1.8f;
                        }},
                        new DrawLight()
                );
                itemCapacity = 10;
                liquidCapacity = 10;
                envDisabled |= Env.oxygen;

                ambientSound = Sounds.loopFire;
                emitLight = true;
            }
        };

        hydrogenCatalysisHeater = new HeatProducer("hydrogen-catalysis-heater"){
            {
                requirements(Category.crafting, with(Items.copper, 20, Items.oxide, 10, Items.metaglass, 30));
                craftEffect = new MultiEffect(TantrosFx.parallaxBubble,TantrosFx.parallaxBubble,TantrosFx.parallaxBubble,TantrosFx.parallaxBubble);

                size = 2;
                drawer = new DrawMulti(
                        //new DrawRegion("-bottom"),
                        new DrawDefault(),
                        new DrawHeatOutput(){{
                            drawGlow = false;
                        }}
                );

                craftTime = 120f;

                hasItems = true;
                hasLiquids = true;
                itemCapacity = 10;
                liquidCapacity = 10;
                envDisabled |= Env.oxygen;
                consumeLiquids(LiquidStack.with(Liquids.hydrogen, 1f/60f));
                consumeItems(with(Items.coal, 1));

                ambientSound = Sounds.loopBio;
                emitLight = true;

                heatOutput = 5;
            }
        };

        sealedElectricHeater = new RecipeCrafter("sealed-electric-heater"){
            {
                requirements(Category.crafting, with(Items.copper, 20, Items.metaglass, 30, Items.graphite, 30));
                craftEffect = Fx.none;
                cons = new ConsumeRecipes(Seq.with(TantrosRecipes.electricHeating));
                size = 1;
                drawer = new DrawMulti(
                        //new DrawRegion("-bottom"),
                        new DrawDefault(),
                        new DrawHeatOutput(){{
                            drawGlow = false;
                        }}
                        //new DrawWarmupRegion(),
                        //new DrawRegion("-glass")
                );
                squareSprite = false;
                envDisabled |= Env.oxygen;
            }
        };

        oxidizationHeater = new RecipeCrafter("oxidization-heater"){
            {
                requirements(Category.crafting, with(Items.copper, 20, Items.oxide, 10, Items.metaglass, 30));

                cons = new ConsumeRecipes(Seq.with(
                        TantrosRecipes.carbonOxidization,
                        TantrosRecipes.berylliumOxidization
                ));
                size = 3;
                drawer = new DrawMulti(
                        new DrawRegion("-bottom"),
                        new DrawLiquidRegion(),
                        new DrawDefault(),
                        new DrawGlowRegion(),
                        new DrawHeatOutput(){{
                            glowMult = 1.8f;
                        }}
                );
                itemCapacity = 30;
                liquidCapacity = 30;
                hasLiquids = true;
                envDisabled |= Env.oxygen;

                ambientSound = Sounds.loopExtract;
                ambientSoundVolume = 0.08f;
                emitLight = true;
            }
        };

        copperBoiler = new Boiler("copper-boiler"){
            {
                requirements(Category.crafting, with(Items.copper, 30, Items.oxide, 20));
                craftEffect = Fx.none;

                size = 2;
                drawer = new DrawMulti(
                        new DrawRegion("-bottom"),
                        new DrawLiquidTile(Liquids.water, 2f),
                        new DrawBubbles(),
                        new DrawParticles(){{
                            color = TantrosLiquids.steam.color;
                            reverse = true;
                            particleSize = 2f;
                            particles = 20;
                            particleRad = 8f;
                            particleLife = 60f;
                        }},
                        new DrawDefault(),
                        //new DrawWarmupRegion(),
                        new DrawHeatInput()
                );
                squareSprite = false;

                craftTime = 60f;

                hasLiquids = true;
                liquidCapacity = 40;
                envDisabled |= Env.oxygen;
                consumeLiquids(LiquidStack.with(Liquids.water, 5f/60f));

                heatRequirement = 5f;

                overheatScale = 1.0f;

                maxEfficiency = 6f;

                outputLiquids = LiquidStack.with(TantrosLiquids.steam, 10f/60f);
            }
        };

        electrolysisChamber = new GenericCrafter("electrolysis-chamber"){{
            requirements(Category.crafting, with(Items.copper, 20, Items.metaglass, 30, Items.silicon, 15, Items.graphite, 20));
            size = 2;

            craftTime = 10f;
            rotate = true;
            invertFlip = true;
            group = BlockGroup.liquids;
            itemCapacity = 0;

            hasLiquids = true;
            liquidCapacity = 15f;

            consumeLiquid(TantrosLiquids.steam, 10f / 60f);
            consumePower(45f/60f);

            drawer = new DrawIconOverride(
                    new DrawMulti(
                            new DrawRegion("-bottom"),
                            new DrawLiquidTile(Liquids.water, 1f),
                            new DrawBubbles(Color.valueOf("7693e3")){{
                                sides = 10;
                                recurrence = 3f;
                                spread = 6;
                                radius = 1.5f;
                                amount = 20;
                            }},
                            new DrawRegion(),
                            new DrawMultiLiquidOutput(
                                    new DrawOutputRegion("-bottom"),
                                    new DrawOutputLiquid(),
                                    new DrawLiquidOutputRegion(false)
                            )
                    )
            );
            squareSprite = false;

            ambientSound = Sounds.loopElectricHum;
            ambientSoundVolume = 0.08f;

            regionRotated1 = 3;
            outputLiquids = LiquidStack.with(Liquids.ozone, 2f / 60, Liquids.hydrogen, 3f / 60);
            liquidOutputDirections = new int[]{1, 3};
        }};
    }
}
