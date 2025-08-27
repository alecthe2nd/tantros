package tantros.content.blocks;

import arc.graphics.Color;
import arc.math.Interp;
import mindustry.content.Fx;
import mindustry.content.Items;
import mindustry.content.Liquids;
import mindustry.gen.Sounds;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.type.LiquidStack;
import mindustry.world.Block;
import mindustry.world.blocks.production.GenericCrafter;
import mindustry.world.draw.*;
import mindustry.world.meta.BlockGroup;
import mindustry.world.meta.Env;
import tantros.content.world.TantrosLiquids;
import tantros.content.world.blocks.production.Boiler;
import tantros.content.world.draw.DrawIconOverride;
import tantros.content.world.draw.output.DrawLiquidOutputRegion;
import tantros.content.world.draw.output.DrawMultiLiquidOutput;
import tantros.content.world.draw.output.DrawOutputLiquid;
import tantros.content.world.draw.output.DrawOutputRegion;

import static mindustry.type.ItemStack.with;

public class TantrosProduction {

    public static Block
            metaglassAnnealer,
            graphiticDecomposer,
            siliconPressureSmelter,
            electrolysisChamber,
            electricBoiler,
            combustionBoiler,
            hydrogenCatalysisBoiler,
            oxidizationReactor,
            steamCombustor
                    ;

    public static void load(){

        metaglassAnnealer = new GenericCrafter("metaglass-annealer"){{
            requirements(Category.crafting, with(Items.copper, 80, Items.lead, 40));
            craftEffect = Fx.airBubble;
            outputItem = new ItemStack(Items.metaglass, 4);
            craftTime = 120f;
            size = 3;
            hasPower = true;
            hasLiquids = false;
            envEnabled |= Env.underwater;
            envDisabled = Env.none;
            itemCapacity = 30;
            drawer = new DrawMulti(new DrawDefault(), new DrawWarmupRegion());
            fogRadius = 3;
            ambientSound = Sounds.smelter;
            ambientSoundVolume = 0.12f;

            consumeItems(with(Items.lead, 2, Items.sand, 3));
            consumePower(1.5f);
        }};

        graphiticDecomposer = new GenericCrafter("graphitic-decomposer"){{
            requirements(Category.crafting, with(Items.copper, 40, Items.lead, 10, Items.metaglass, 30));
            craftEffect = Fx.none;
            outputItem = new ItemStack(Items.graphite, 1);
            outputLiquid = new LiquidStack(Liquids.hydrogen, 0.5f / 60f);
            craftTime = 120f;
            size = 2;
            hasPower = true;
            hasLiquids = true;
            hasItems = true;
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
            consumeItems(with(Items.coal, 2));
            ignoreLiquidFullness = true;
            consumePower(30f / 60f);
        }};

        siliconPressureSmelter = new GenericCrafter("silicon-pressure-smelter"){{
            requirements(Category.crafting, with(Items.copper, 30, Items.lead, 20, Items.metaglass, 40, Items.graphite, 50));
            craftEffect = Fx.none;
            outputItem = new ItemStack(Items.silicon, 3);
            craftTime = 60f;
            size = 3;
            hasPower = true;
            hasItems = true;
            hasLiquids = true;
            envEnabled |= Env.space | Env.underwater;
            envDisabled = Env.none;
            itemCapacity = 30;
            liquidCapacity = 30;
            drawer = new DrawMulti(new DrawRegion("-bottom"), new DrawArcSmelt(), new DrawDefault());
            squareSprite = false;
            fogRadius = 3;
            ambientSound = Sounds.smelter;
            ambientSoundVolume = 0.12f;

            consumeItems(with(Items.coal, 1, Items.sand, 3));
            consumeLiquids(LiquidStack.with(Liquids.hydrogen, 0.5f/60f));
            consumePower(120f/60f);
        }};

        electricBoiler = new Boiler("electric-boiler"){
            {
                requirements(Category.crafting, with(Items.copper, 20, Items.metaglass, 30, Items.graphite, 30));
                craftEffect = Fx.none;

                size = 1;
                drawer = new DrawMulti(
                        new DrawRegion("-bottom"),
                        new DrawLiquidTile(Liquids.water, 1f),
                        //new DrawRegion("-heater"),
                        new DrawBubbles(){{
                            radius = 1;
                            spread = 2;
                            sides = 8;
                        }},
                        new DrawParticles(){{
                            color = TantrosLiquids.steam.color;
                            reverse = true;
                            particleSize = 2f;
                            particles = 10;
                            particleRad = 4f;
                            particleLife = 60f;
                        }},
                        new DrawDefault()
                        //new DrawWarmupRegion(),
                        //new DrawRegion("-glass")
                );
                squareSprite = false;

                craftTime = 10f;

                hasLiquids = true;
                liquidCapacity = 5;
                envDisabled |= Env.oxygen;
                consumePower(120f/60f);
                consumeLiquids(LiquidStack.with(Liquids.water, 2.5f/60f));

                outputLiquids = LiquidStack.with(TantrosLiquids.steam, 5f/60f);
            }
        };

        combustionBoiler = new Boiler("combustion-boiler"){
            {
                requirements(Category.crafting, with(Items.copper, 20, Items.lead, 10, Items.metaglass, 30));
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
                        new DrawWarmupRegion()
                );
                squareSprite = false;

                craftTime = 120f;

                hasItems = true;
                hasLiquids = true;
                itemCapacity = 10;
                liquidCapacity = 10;
                envDisabled |= Env.oxygen;
                consumeLiquids(LiquidStack.with(Liquids.water, 5f/60f, Liquids.ozone, 1f/60f));
                consumeItems(with(Items.coal, 1));

                outputLiquids = LiquidStack.with(TantrosLiquids.steam, 10f/60f);
            }
        };
        hydrogenCatalysisBoiler = new Boiler("hydrogen-catalysis-boiler"){
            {
                requirements(Category.crafting, with(Items.copper, 20, Items.lead, 10, Items.metaglass, 30));
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
                        new DrawWarmupRegion()
                );
                squareSprite = false;

                craftTime = 120f;

                hasItems = true;
                hasLiquids = true;
                itemCapacity = 10;
                liquidCapacity = 10;
                envDisabled |= Env.oxygen;
                consumeLiquids(LiquidStack.with(Liquids.water, 5f/60f, Liquids.hydrogen, 1f/60f));
                consumeItems(with(Items.coal, 1));

                outputLiquids = LiquidStack.with(TantrosLiquids.steam, 10f/60f);
            }
        };

        steamCombustor = new Boiler("steam-combustor"){
            {
                requirements(Category.crafting, with(Items.copper, 30, Items.lead, 15, Items.metaglass, 36));
                craftEffect = Fx.none;

                size = 2;
                drawer = new DrawMulti(
                        new DrawRegion("-bottom"),
                        new DrawParticles(){{
                            color = TantrosLiquids.steam.color;
                            reverse = true;
                            particleSize = 2f;
                            particles = 20;
                            particleRad = 8f;
                            particleLife = 60f;
                        }},
                        new DrawDefault(),
                        new DrawWarmupRegion(),
                        new DrawGlowRegion(){{
                            color = Liquids.hydrogen.color;
                        }}
                );
                squareSprite = false;

                craftTime = 120f;

                hasItems = true;
                hasLiquids = true;
                itemCapacity = 10;
                liquidCapacity = 10;
                envDisabled |= Env.oxygen;
                consumeLiquids(LiquidStack.with(Liquids.ozone, 2f / 60, Liquids.hydrogen, 3f / 60));

                outputLiquids = LiquidStack.with(TantrosLiquids.steam, 10f / 60f);
            }
        };

        oxidizationReactor = new Boiler("oxidization-reactor"){
            {
                requirements(Category.crafting, with(Items.metaglass, 20, Items.graphite, 10, Items.beryllium, 30));

                drawer = new DrawMulti(
                        new DrawRegion("-bottom"),
                        new DrawLiquidRegion(),
                        new DrawDefault(),
                        new DrawGlowRegion()
                );
                squareSprite = false;

                size = 3;
                craftTime = 60f * 2f;

                hasItems = true;
                hasLiquids = true;
                itemCapacity = 30;
                liquidCapacity = 30;
                envDisabled |= Env.oxygen;

                consumeLiquids(LiquidStack.with(Liquids.ozone, 4f / 60f, Liquids.water, 15f/60f));
                consumeItems(with(Items.beryllium, 2));
                consumePower(30f/60f);

                outputLiquids = LiquidStack.with(TantrosLiquids.steam, 30f/60f);
                outputItems = ItemStack.with(Items.oxide, 2);
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
            consumePower(30f/60f);

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

            ambientSound = Sounds.electricHum;
            ambientSoundVolume = 0.08f;

            regionRotated1 = 3;
            outputLiquids = LiquidStack.with(Liquids.ozone, 2f / 60, Liquids.hydrogen, 3f / 60);
            liquidOutputDirections = new int[]{1, 3};
        }};
    }
}
