package tantros.content.blocks;

import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.struct.EnumSet;
import arc.struct.Seq;
import mindustry.Vars;
import mindustry.content.Fx;
import mindustry.content.Items;
import mindustry.content.Liquids;
import mindustry.gen.Building;
import mindustry.gen.Sounds;
import mindustry.graphics.Layer;
import mindustry.type.Category;
import mindustry.type.LiquidStack;
import mindustry.world.Block;
import mindustry.world.blocks.production.GenericCrafter;
import mindustry.world.blocks.production.Pump;
import mindustry.world.draw.*;
import mindustry.world.meta.BlockFlag;
import mindustry.world.meta.BlockGroup;
import mindustry.world.meta.BuildVisibility;
import mindustry.world.meta.Env;
import tantros.content.recipes.TantrosRecipes;
import tantros.type.Resource;
import tantros.type.production.*;
import tantros.world.blocks.drill.CustomDrawerBeamDrill;
import tantros.world.blocks.drill.CustomDrawerDrill;
import tantros.world.blocks.production.ProductionBlock;
import tantros.world.blocks.production.RecipeCrafter;
import tantros.world.blocks.production.Sifter;
import tantros.world.draw.*;
import tantros.world.draw.DrawFade;
import tantros.world.draw.extended.DrawMultiExtended;
import tantros.world.draw.wallDrill.DrawBoreBit;
import tantros.world.draw.wallDrill.DrawDrillBit;
import tantros.world.consumers.ConsumeEnv;
import tantros.world.draw.wallDrill.DrawPlacementLines;
import tantros.world.environment.LocalEnv;

import static mindustry.type.ItemStack.with;

public class TantrosSource {

    public static Block
            testProdBlock,
            testBlock,
            mechanicalBore,
            copperBore,
            siltSifter,
            deepBoreDrill,
            deepLaserDrill,
            boreholeDrill,
            seawaterIntake,
            effervescenceCollector,
            atmosphereIntakeTower
    ;

    public static void load(){

        testProdBlock = new ProductionBlock("test-prod-block"){{
            requirements(Category.production, with(Items.copper, 12));

            size = 2;
            researchCost = with(Items.copper, 10);
            productionTime = 100;
            drawArrow = true;
            warmupEffectsProduction = true;

            drawer = new DrawMultiExtended(
                    new DrawDefault(),
                    new DrawBoreBit(),
                    new DrawPlacementLines()
            );
            consumePower(1f);
            produce(new ProduceIfCooldown(
                    new ProduceWallOre(5,10),
                    240
                    )
            );
        }};

        testBlock = new RecipeCrafter("test-block"){{
            requirements(Category.production, with(Items.copper, 12));

            size = 2;
            researchCost = with(Items.copper, 10);
            productionTime = 100;

            addRecipe(TantrosRecipes.metaglassAnnealing);
        }};

        mechanicalBore = new CustomDrawerBeamDrill("mechanical-bore"){{
            requirements(Category.production, with(Items.copper, 12));

            drillTime = 240f;
            tier = 2;
            size = 2;
            range = 2;
            researchCost = with(Items.copper, 10);
            hideDatabase = true;
            buildVisibility = BuildVisibility.hidden;

            drawer = new DrawMulti(
                    new DrawDrillBit(),
                    new DrawDefault()
            );
            optionalBoostIntensity = 1;
        }};

        copperBore = new ProductionBlock("copper-bore"){{
            requirements(Category.production, with(Items.copper, 12));

            productionTime = 240f;
            size = 2;
            researchCost = with(Items.copper, 10);
            warmupEffectsProduction = true;
            regionRotated1 = -2;
            ignoreLineRotation = true;

            drawer = new DrawMultiExtended(
                    new DrawDefault(),
                    new DrawBoreBit(),
                    new DrawPlacementLines()
            );
            produce(new ProduceIfCooldown(
                            new ProduceWallOre(2,2),
                            120
                    )
            );
            flags = EnumSet.of(BlockFlag.drill);
        }};

        siltSifter = new Sifter("silt-sifter"){{
            requirements(Category.production, with(Items.copper, 16, Items.oxide, 8));
            tier = 1;
            drillTime = 600;
            size = 2;
            liquidBoostIntensity = 1.0f;

            blockedItems = Seq.with(
                    Items.copper,
                    Items.coal,
                    Items.lead,
                    Items.scrap
            );
            researchCost = with(Items.copper, 10);

            consume(new ConsumeEnv(LocalEnv.with(Liquids.water)));
            flags = EnumSet.of(BlockFlag.drill);
        }};

        deepBoreDrill = new ProductionBlock("deep-bore-drill"){{
            requirements(Category.production, with(Items.copper, 12, Items.metaglass, 30, Items.graphite, 25, Items.silicon, 10));
            productionTime = 60;
            size = 3;
            squareSprite = false;

            drawer = new DrawMultiExtended(
                    new DrawDefault(),
                    new DrawLoweredCooldown(
                            //new DrawSpin("-rotator", 6f, Building::totalProgress),
                            new DrawRegion("-rotator", 100f, true),
                            new DrawRegion("-rotator-rig")
                    ){{
                        lowerScale = 0.65f;
                        lowerBrightness = 0.75f;
                    }}
                    ,
                    new DrawRegion("-top")
            );

            //moderate power usage
            consumePower(40f/60f);

            liquidCapacity = 10;

            //hydrogen is needed to keep the borehole pressurized
            consumeLiquid(Liquids.hydrogen, 2f/60f);

            //optional ozone input burns away unwanted dust or impurities
            consumeLiquid(Liquids.ozone, 1f/60f).boost();

            produce(new ProduceIfCooldown(new ProduceDeepOre(3), 120));
        }};

        deepLaserDrill = new CustomDrawerDrill("deep-laser-drill"){{
            requirements(Category.production, with(
                    Items.metaglass, 50,
                    Items.titanium, 25,
                    Items.oxide, 20,
                    Items.graphite, 20,
                    Items.silicon, 30
            ));
            tier = 5;
            drillTime = 60;
            size = 4;
            squareSprite = false;
            liquidBoostIntensity = 1f;

            setNumberSource("laser_fade", () -> (build)-> (build.efficiency > 0)? Mathf.sin(build.totalProgress(), 6f, 1f) * Mathf.clamp(build.warmup()): 0f);

            drawer = new DrawMulti(
                    new DrawDefault(),
                    new DrawSpin("-rotator", 6f, Building::totalProgress),
                    new DrawIf(
                            new DrawFade(
                                    new DrawMulti(
                                            new DrawSpin("-rotator-laser", 6f, Building::totalProgress){{
                                                useSpinSprite = false;
                                            }},
                                            new DrawRegion("-point")
                                    ),
                                    "laser_fade"
                            ){{
                                lowerFade = 0.65f;
                            }},
                            (build) -> build.efficiency > 0
                    ),

                    new DrawRegion("-top")
            );

            //space ore sources cannot be deep enough to warrant this drill
            envEnabled ^= Env.space;

            //this device is designed for underwater usage
            envEnabled |= Env.underwater;

            //moderate power usage
            consumePower(80f/60f);

            liquidCapacity = 10;

            //needs nitrogen to maintain an inert atmosphere within the borehole
            consumeLiquid(Liquids.nitrogen, 4f/60f);
        }};

        boreholeDrill = new ProductionBlock("borehole-drill"){{
            requirements(Category.production, with(Items.metaglass, 120, Items.graphite, 240, Items.silicon, 105, Items.tungsten, 360));

            productionTime = 30;

            //moderate power usage
            //consumePower(240f/60f);

            produce(new ProduceDeepOre(10));

            produce(new SimpleProduce(new Resource().withLiquids(LiquidStack.with(Liquids.slag, 10))));

            size = 5;
            squareSprite = false;

            drawer = new DrawMultiExtended(
                    new DrawRegion("-bottom"),
                    new DrawDefault(),
                    new DrawRegion("-rotator", 6f, true)
                    //new DrawRegion("-top")
            );

            //space ore sources cannot be deep enough to warrant this drill
            envEnabled ^= Env.space;

            //this device is designed for underwater usage
            envEnabled |= Env.underwater;

            itemCapacity = 50;
            liquidCapacity = 20;
        }};

        seawaterIntake = new GenericCrafter("seawater-intake"){{
            requirements(Category.production, with(Items.metaglass, 15, Items.graphite, 5, Items.copper, 10));
            size = 1;
            envEnabled |= Env.underwater;

            hasLiquids = true;

            drawer = new DrawMulti(new DrawRegion("-bottom"), new DrawLiquidTile(Liquids.water, 4.1f), new DrawDefault(),
                    new DrawParticles(){{
                        color = Liquids.water.color;
                        alpha = 0.4f;
                        particleSize = 2f;
                        particles = 5;
                        particleRad = 4f;
                        particleLife = 280f;
                    }});

            liquidCapacity = 10f;
            ambientSound = Sounds.loopExtract;
            ambientSoundVolume = 0.06f;

            outputLiquid = new LiquidStack(Liquids.water, 5f / 60f);
            consume(new ConsumeEnv(LocalEnv.with(Liquids.water)));

        }};

        effervescenceCollector = new Pump("effervescence-collector"){{
            requirements(Category.production, with(Items.metaglass, 15, Items.graphite, 5, Items.copper, 10));
            size = 3;
            envEnabled |= Env.underwater;
            //customShadow = true;

            drawer = new DrawMulti(
                        new DrawParticles(){{
                            color = Liquids.water.color;
                            alpha = 0.4f;
                            particleSize = 2f;
                            particles = 20;
                            particleRad = 16f;
                            particleLife = 280f;
                        }},
                        new DrawRegion("-rotator", 0.6f, true),
                        new DrawDefault(),
                        new DrawParticles(){{
                            color = Liquids.water.color;
                            alpha = 0.4f;
                            particleSize = 2f;
                            particles = 10;
                            particleRad = 10f;
                            particleLife = 140f;
                            reverse = true;
                        }}
            );

            pumpAmount = 3f/(60f*size*size);
            liquidCapacity = 10f;
            ambientSound = Sounds.loopExtract;
            ambientSoundVolume = 0.06f;

            //consumePower(20f/60f);
            consume(new ConsumeEnv(LocalEnv.with(Liquids.water)));

        }};

        atmosphereIntakeTower = new GenericCrafter("atmosphere-intake"){{
            requirements(Category.production, with(Items.copper, 40, Items.titanium, 15, Items.metaglass, 50, Items.graphite, 30));
            craftEffect = Fx.none;
            outputLiquids = LiquidStack.with(
                    Liquids.ozone, 2f / 60f,
                    Liquids.nitrogen, 8f / 60f
            );
            craftTime = 120;
            size = 3;
            hasPower = true;
            hasLiquids = true;
            liquidCapacity = 30;
            rotate = true;
            invertFlip = true;
            group = BlockGroup.liquids;
            regionRotated1 = 10;
            liquidOutputDirections = new int[]{1, 3};

            drawer = new DrawMulti(
                    new DrawRegion("-bottom"),
                    new DrawLiquidTile(Liquids.nitrogen),
                    new DrawRegion(),
                    new DrawLiquidOutputs(),
                    new DrawRegion("-vent-shadow"){
                        {
                            x = -(size * Vars.tilesize) / 2f;
                            y = -(size * Vars.tilesize) / 2f;
                            rotation = 45f;
                            layer = Layer.light + 1.05f;
                        }

                        @Override
                        public TextureRegion[] icons(Block block) {
                            return new TextureRegion[]{};
                        }
                    },
                    new DrawSurfaceRipples(){
                        {
                            color = Liquids.water.color;
                            sides = 24;
                            recurrence = 15f;
                            spread = 0;
                            radius = 12f;
                            amount = 12;
                            timeScl = 90f;
                            layer = Layer.light + 1.1f;
                        }},
                    new DrawLayeredRegion("-vent-bottom", Layer.light + 1.1f),
                    new DrawLayeredRegion("-vent", Layer.light + 1.1f),
                    new DrawParticles(){
                        {
                            color = Liquids.nitrogen.color;
                            alpha = 0.6f;
                            particleSize = 4f;
                            particles = 10;
                            particleRad = 12f;
                            particleLife = 140f;
                        }
                        @Override
                        public void draw(Building build){
                            Draw.z(Layer.light + 1.1f);
                            super.draw(build);
                        }
                    });
            dumpExtraLiquid = true;
            consumePower(45f / 60f);
        }};
    }


}
