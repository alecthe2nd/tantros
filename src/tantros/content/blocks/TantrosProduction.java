package tantros.content.blocks;

import arc.graphics.Color;
import arc.math.Interp;
import mindustry.content.Fx;
import mindustry.content.Items;
import mindustry.content.Liquids;
import mindustry.entities.effect.MultiEffect;
import mindustry.gen.Sounds;
import mindustry.graphics.Pal;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.type.LiquidStack;
import mindustry.world.Block;
import mindustry.world.blocks.heat.HeatProducer;
import mindustry.world.blocks.production.GenericCrafter;
import mindustry.world.consumers.ConsumeLiquid;
import mindustry.world.draw.*;
import mindustry.world.meta.Attribute;
import mindustry.world.meta.BlockGroup;
import mindustry.world.meta.Env;
import tantros.content.TantrosFx;
import tantros.content.world.TantrosItems;
import tantros.content.world.TantrosLiquids;
import tantros.type.blockConfig.AttributeConfig;
import tantros.type.blockConfig.HeatConsumptionConfig;
import tantros.type.blockConfig.HeatProductionConfig;
import tantros.type.effect.PressureExplosionOnDestruction;
import tantros.type.production.ProduceBoilerLiquid;
import tantros.type.production.ProduceHeat;
import tantros.world.blocks.production.Boiler;
import tantros.world.blocks.production.ProductionBlock;
import tantros.world.consumers.ConsumeAttributeTile;
import tantros.world.consumers.ConsumeBoostWrapper;
import tantros.world.consumers.ConsumeHeat;
import tantros.world.draw.DrawIconOverride;
import tantros.world.draw.DrawLight;
import tantros.world.draw.extended.*;
import tantros.world.draw.output.DrawLiquidOutputRegion;
import tantros.world.draw.output.DrawMultiLiquidOutput;
import tantros.world.draw.output.DrawOutputLiquid;
import tantros.world.draw.output.DrawOutputRegion;
import tantros.world.blocks.production.RecipeCrafter;
import tantros.content.recipes.TantrosRecipes;

import static mindustry.type.ItemStack.with;

public class TantrosProduction {

    public static Block
            metaglassAnnealer,
            graphiticDecomposer,
            siliconPressureSmelter,
            nanostructureWeaver,
            electrolysisChamber,
            combustionHeater,
            cystCombustionHeater,
            geothermalHeater,
            hydrogenCatalysisHeater,
            sealedElectricHeater,
            oxidizationHeater,
            copperBoiler,
            simpleBoiler,
            pneumaticPress
                    ;

    public static void load(){

        metaglassAnnealer = new RecipeCrafter("metaglass-annealer"){{
            requirements(Category.crafting, with(Items.copper, 80, Items.oxide, 40));
            addRecipe(TantrosRecipes.metaglassAnnealing);
            addRecipe(TantrosRecipes.surgeAnnealing);
            addRecipe(TantrosRecipes.slagSurgeAnnealing);
            craftEffect = TantrosFx.parallaxBubble;
            size = 3;
            envEnabled |= Env.underwater;
            envDisabled = Env.none;
            itemCapacity = 30;
            drawer = new DrawMulti(new DrawDefault(), new DrawWarmupRegion());
            fogRadius = 3;
            ambientSound = Sounds.loopSmelter;
            ambientSoundVolume = 0.12f;
            researchCostMultiplier = 0.5f;
        }};

        graphiticDecomposer = new RecipeCrafter("graphitic-decomposer"){{
            requirements(Category.crafting, with(Items.copper, 40, Items.oxide, 20, Items.metaglass, 30));
            addRecipe(TantrosRecipes.bluecystDecomposition);
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
            requirements(Category.crafting, with(Items.copper, 30, Items.oxide, 20, Items.metaglass, 40));

            addRecipe(TantrosRecipes.siliconPressureSmelting);
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
            researchCostMultiplier = 0.5f;
        }};

        nanostructureWeaver = new RecipeCrafter("nanostructure-weaver"){{
            requirements(Category.crafting, with(Items.copper, 30, Items.oxide, 20, Items.metaglass, 40, Items.graphite, 50));

            addRecipe(TantrosRecipes.siliconWeaving);
            addRecipe(TantrosRecipes.phaseWeaving);
            craftEffect = TantrosFx.fourBubbles;
            size = 4;
            envEnabled |= Env.space | Env.underwater;
            envDisabled = Env.none;
            itemCapacity = 30;
            liquidCapacity = 30;
            drawer = new DrawMulti(new DrawRegion("-bottom"), new DrawMultiWeave(){{
                rotateSpeed = 20;
            }}, new DrawDefault());
            squareSprite = false;
            fogRadius = 3;
            ambientSound = Sounds.loopSmelter;
            ambientSoundVolume = 0.12f;
            researchCostMultiplier = 0.5f;
        }};

        cystCombustionHeater = new ProductionBlock("cyst-combustion-heater"){
            {
                requirements(Category.crafting, with(Items.copper, 20, Items.oxide, 10, Items.metaglass, 30));
                craftEffect = new MultiEffect(TantrosFx.parallaxBubble);

                //addRecipe(TantrosRecipes.hydrogenCombustion);
                size = 1;
                drawer = new DrawMultiExtended(
                        new DrawRegion("-bottom"),
                        new DrawWarmupRegion(){{
                            color = Pal.darkFlame;
                        }},
                        new DrawParticles(){{
                            color = Color.valueOf("ed8e38");
                            reverse = true;
                            particleSize = 2f;
                            particles = 20;
                            particleRad = 4f;
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

                consumeItems(ItemStack.with(TantrosItems.redcyst, 1));

                HeatProductionConfig config = new HeatProductionConfig();
                config.heatOutput = 5;
                config.sideOutputs[0] = 1;
                produce(new ProduceHeat(config));
                productionTime = 60f;

                rotate = true;
                rotateDraw = false;
                drawArrow = true;
            }
        };

        geothermalHeater = new ProductionBlock("geothermal-heater"){{
            requirements(Category.crafting, with(Items.oxide, 50, Items.copper, 15));
            productionTime = 60;
            size = 2;
            squareSprite = false;

            hasLiquids = true;

            drawer = new DrawMultiExtended(
                    new DrawDefault(),
                    new DrawHeatOutputExtended(),
                    new DrawAttributeEfficiency()
            );

            AttributeConfig attributeConfig = new AttributeConfig();
            attributeConfig.attribute = Attribute.heat;
            attributeConfig.displayEfficiencyScale = attributeConfig.efficiencyScale = 1/3f;

            HeatProductionConfig heatProductionConfig = new HeatProductionConfig();
            heatProductionConfig.heatOutput = 5f;
            heatProductionConfig.sideOutputs[0] = 1f;

            putBlockConfig(attributeConfig);
            putBlockConfig(heatProductionConfig);
            consume(new ConsumeAttributeTile());

            consume(new ConsumeBoostWrapper(new ConsumeLiquid(Liquids.slag, 10f/60f), 2.5f));

            produce(new ProduceHeat(heatProductionConfig));

            rotate = true;
            rotateDraw = false;
            drawArrow = true;
            floating = true;
        }};

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

                addRecipe(TantrosRecipes.electricHeating);
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

                addRecipe(TantrosRecipes.carbonOxidization);
                addRecipe(TantrosRecipes.berylliumOxidization);
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
                stressExplosiveness = 10;

                craftTime = 60f;

                hasLiquids = true;
                liquidCapacity = 40;
                consumeLiquids(LiquidStack.with(Liquids.water, 5f/60f));

                heatRequirement = 5f;

                overheatScale = 1.0f;

                maxEfficiency = 6f;

                outputLiquids = LiquidStack.with(TantrosLiquids.steam, 10f/60f);
            }
        };

        simpleBoiler = new ProductionBlock("simple-boiler"){{
            requirements(Category.crafting, with(Items.copper, 30, Items.oxide, 20));

            size = 2;
            drawer = new DrawMultiExtended(
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
                    new DrawHeatInputExtended(),
                    new DrawPressureWarning()
            );
            squareSprite = false;

            hasLiquids = true;
            liquidCapacity = 40;
            consume(new ConsumeHeat(
               new HeatConsumptionConfig(5, 6)
            ));
            consumeLiquids(LiquidStack.with(Liquids.water, 5f/60f));
            ProduceBoilerLiquid p = new ProduceBoilerLiquid(new LiquidStack(TantrosLiquids.steam, 10f/60f));
            p.config.pressureCapacity = 30;
            produce(p);
            effects.add(new PressureExplosionOnDestruction(10));
        }};

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

        pneumaticPress = new RecipeCrafter("pneumatic-press"){{
            requirements(Category.crafting, with(Items.copper, 40, Items.oxide, 20, Items.metaglass, 30));
            addRecipe(TantrosRecipes.bluecystOilification);
            addRecipe(TantrosRecipes.redcystOilification);
            addRecipe(TantrosRecipes.graphitePressing);
            addRecipe(TantrosRecipes.plastaniumCompressing);

            craftEffect = new MultiEffect(
                    TantrosFx.parallaxBubble,
                    TantrosFx.parallaxBubble,
                    Fx.steam
                    );
            size = 2;
            itemCapacity = 30;
            liquidCapacity = 30;

            drawer = new DrawMulti(
                    new DrawDefault()
            );
            }};
    }
}
