package tantros.content;

import arc.Events;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.Interp;
import arc.struct.Seq;
import mindustry.Vars;
import mindustry.content.Fx;
import mindustry.content.Items;
import mindustry.content.Liquids;
import mindustry.content.UnitTypes;
import mindustry.entities.bullet.BasicBulletType;
import mindustry.entities.part.RegionPart;
import mindustry.entities.pattern.ShootAlternate;
import mindustry.gen.Building;
import mindustry.gen.Sounds;
import mindustry.graphics.Layer;
import mindustry.graphics.Pal;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.type.LiquidStack;
import mindustry.world.Block;
import mindustry.world.blocks.defense.Wall;
import mindustry.world.blocks.defense.turrets.ItemTurret;
import mindustry.world.blocks.distribution.Duct;
import mindustry.world.blocks.distribution.DuctBridge;
import mindustry.world.blocks.distribution.DuctRouter;
import mindustry.world.blocks.environment.OreBlock;
import mindustry.world.blocks.power.ConsumeGenerator;
import mindustry.world.blocks.power.PowerNode;
import mindustry.world.blocks.production.BeamDrill;
import mindustry.world.blocks.production.GenericCrafter;
import mindustry.world.blocks.storage.CoreBlock;
import mindustry.world.consumers.ConsumeCoolant;
import mindustry.world.draw.*;
import mindustry.world.meta.BlockGroup;
import mindustry.world.meta.BuildVisibility;
import mindustry.world.meta.Env;
import tantros.content.world.TantrosLiquids;
import tantros.content.world.blocks.effect.GroundPenetratingRadar;
import tantros.content.world.blocks.environment.DeepOreBlock;
import tantros.content.world.blocks.power.PassiveGenerator;
import tantros.content.world.blocks.production.Boiler;
import tantros.content.world.blocks.production.Sifter;
import tantros.content.world.blocks.storage.CustomCoreBlock;
import tantros.content.world.draw.DrawLayeredRegion;
import tantros.content.world.draw.DrawSpin;
import tantros.content.world.draw.DrawSurfaceRipples;

import static mindustry.type.ItemStack.with;

public class TantrosBlocks {

    public static Block
            copperDuct, copperDuctRouter, copperDuctBridge,
            pressurizedDuct,

            //drills
            copperBore, siltSifter, seawaterIntake,

            //storage
            coreShell,

            //crafters
            metaglassAnnealer, graphiticDecomposer,
                    atmosphereIntake,
                    siliconPressureSmelter,
                    combustionBoiler,

            //ore
            wallOreCopper, wallOreLead, wallOreCoal,
                    deepOreCopper,
                    deepOreLead,
                    deepOreCoal,
                    deepOreScrap,
                    deepOreTitanium,
                    deepOreBeryllium,
                    deepOreTungsten,
                    deepOreThorium,

            //power
            sealed_node, tidal_turbine,
                    steamTurbine,

            //defense
            copperBulkhead, largeCopperBulkhead,
                    deepSonar,

            //turrets
            bident;

    public static void load(){

        //region distribution
        copperDuctRouter = new DuctRouter("copper-duct-router"){{
            requirements(Category.distribution, with(Items.copper, 3));
            health = 90;
            speed = 12.28f;
            regionRotated1 = 1;
            solid = false;
            researchCost = with(Items.copper, 5);
        }};

        copperDuctBridge = new DuctBridge("copper-duct-bridge"){{
            requirements(Category.distribution, with(Items.copper, 3));
            health = 90;
            speed = 12.28f;
            researchCost = with(Items.copper, 5, Items.lead, 5);
        }};

        copperDuct = new Duct("copper-duct"){{
            requirements(Category.distribution, with(Items.copper, 1));
            health = 90;
            speed = 12.28f;
            researchCost = with(Items.copper, 5);
            bridgeReplacement = copperDuctBridge;
        }};
        pressurizedDuct = new Block("pressurized-duct");
        //endregion

        //region production
        copperBore = new BeamDrill("copper-bore"){{
            requirements(Category.production, with(Items.copper, 12));

            drillTime = 180f;
            tier = 2;
            size = 2;
            range = 2;
            researchCost = with(Items.copper, 10);

            consumeLiquid(Liquids.hydrogen, 0.25f / 60f).boost();
        }};

        siltSifter = new Sifter("silt-sifter"){{
            requirements(Category.production, with(Items.copper, 16));
            tier = 1;
            drillTime = 600;
            size = 2;

            blockedItems = Seq.with(
                    Items.copper,
                    Items.coal,
                    Items.lead,
                    Items.scrap
            );
            //silt sifter only works underwater
            envEnabled = Env.underwater;
            researchCost = with(Items.copper, 10);
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
            ambientSound = Sounds.extractLoop;
            ambientSoundVolume = 0.06f;

            outputLiquid = new LiquidStack(Liquids.water, 6f / 60f);
        }};

        //endregion
        //region storage

        coreShell = new CustomCoreBlock("core-shell"){{
            requirements(Category.effect, BuildVisibility.coreZoneOnly, with(Items.copper, 800, Items.lead, 800, Items.metaglass, 1000));
            alwaysUnlocked = true;
            squareSprite = false;
            isFirstTier = true;
            unitType = UnitTypes.alpha;
            health = 1100;
            hasItems = true;
            itemCapacity = 4000;
            size = 4;
            buildCostMultiplier = 2f;

            unitCapModifier = 8;
        }};

        //endregion

        //region ore
        deepOreCopper = new DeepOreBlock("ore-deep-copper", Items.copper);

        deepOreLead = new DeepOreBlock("ore-deep-lead", Items.lead);

        deepOreCoal = new DeepOreBlock("ore-deep-coal", Items.coal);

        deepOreTitanium = new DeepOreBlock("ore-deep-titanium", Items.titanium);

        deepOreBeryllium = new DeepOreBlock("ore-deep-beryllium", Items.beryllium);

        deepOreTungsten = new DeepOreBlock("ore-deep-tungsten", Items.tungsten);

        deepOreThorium = new DeepOreBlock("ore-deep-thorium", Items.thorium);

        wallOreCopper = new OreBlock("ore-wall-copper", Items.copper){{
            wallOre = true;
        }};
        wallOreLead = new OreBlock("ore-wall-lead", Items.lead){{
            wallOre = true;
        }};
        wallOreCoal = new OreBlock("ore-wall-coal", Items.coal){{
            wallOre = true;
        }};
        //endregion

        //region power
        sealed_node = new PowerNode("sealed-node"){{
            requirements(Category.power, with(Items.copper, 2, Items.lead, 6));
            maxNodes = 10;
            laserRange = 6;
            buildCostMultiplier = 2.5f;
        }};

        tidal_turbine = new PassiveGenerator("tidal-turbine"){{
            requirements(Category.power, with(Items.copper, 25, Items.lead, 10));
            powerProduction = 1f;

            envEnabled = Env.underwater;
            drawer = new DrawMulti(new DrawSpin("-rotator", 0.6f), new DrawDefault());
            size = 6;
        }};

        steamTurbine = new ConsumeGenerator("steam-turbine"){{
            requirements(Category.power, with(Items.metaglass, 35, Items.copper, 30, Items.lead, 15));
            powerProduction = 2f;
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
            consumeLiquids(LiquidStack.with(TantrosLiquids.steam, 6f/60f));

            size = 1;
        }};
        //endregion

        //region crafters



        metaglassAnnealer = new GenericCrafter("metaglass-annealer"){{
            requirements(Category.crafting, with(Items.copper, 80, Items.lead, 40));
            craftEffect = Fx.bubble;
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
            outputLiquid = new LiquidStack(Liquids.hydrogen, 1f / 60f);
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

        atmosphereIntake = new GenericCrafter("atmosphere-intake"){{
            requirements(Category.production, with(Items.copper, 40, Items.lead, 15, Items.metaglass, 50, Items.graphite, 30));
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
            fogRadius = 3;
            ambientSound = Sounds.smelter;
            ambientSoundVolume = 0.12f;

            consumeItems(with(Items.coal, 1, Items.sand, 3));
            consumeLiquids(LiquidStack.with(Liquids.nitrogen, 3.5f/60f));
            consumePower(120f/60f);
        }};

        combustionBoiler = new Boiler("combustion-boiler"){
            {
                requirements(Category.crafting, with(Items.copper, 20, Items.lead, 10, Items.metaglass, 30));
                craftEffect = Fx.none;

                size = 2;
                drawer = new DrawMulti(
                        new DrawRegion("-bottom"),
                        new DrawLiquidTile(Liquids.water, 2f),
                        new DrawBubbles(),
                        new DrawDefault(),
                        new DrawWarmupRegion()
                );
                craftTime = 120f;

                hasItems = true;
                hasLiquids = true;
                itemCapacity = 30;
                liquidCapacity = 50;
                envDisabled |= Env.oxygen;
                consumeLiquids(LiquidStack.with(Liquids.water, 6f/60f, Liquids.ozone, 1f/60f));
                consumeItems(with(Items.coal, 1));

                outputLiquids = LiquidStack.with(TantrosLiquids.steam, 12f/60f);
                craftTime = 60f;
            }
        };
        //endregion

        //region defense

        copperBulkhead = new Wall("copper-bulkhead"){{
            requirements(Category.defense, with(Items.copper, 6));
            health = 320;
            researchCostMultiplier = 0.1f;
            envEnabled |= Env.underwater;
        }};

        largeCopperBulkhead = new Wall("copper-bulkhead-large"){{
            requirements(Category.defense, ItemStack.mult(copperBulkhead.requirements, 4));
            health = copperBulkhead.health * 4;
            size = 2;
            envEnabled |= Env.underwater;
        }};

        //endregion

        //region turrets

        bident = new ItemTurret("bident"){{
            requirements(Category.turret, with(Items.copper, 35));
            ammo(
                    Items.copper,  new BasicBulletType(2f, 9){{
                        width = 7f;
                        height = 9f;
                        lifetime = 60f;
                        ammoMultiplier = 2;

                        hitEffect = despawnEffect = Fx.hitBulletColor;
                        hitColor = backColor = trailColor = Pal.copperAmmoBack;
                        frontColor = Pal.copperAmmoFront;
                    }},
                    Items.metaglass, new BasicBulletType(3.5f, 18){{
                        width = 9f;
                        height = 12f;
                        ammoMultiplier = 4;
                        lifetime = 60f;
                        rangeChange = 80f;

                        hitEffect = despawnEffect = Fx.hitBulletColor;
                        hitColor = backColor = trailColor = Pal.glassAmmoBack;
                        frontColor = Pal.glassAmmoFront;
                    }},
                    Items.silicon, new BasicBulletType(3f, 12){{
                        width = 7f;
                        height = 9f;
                        homingPower = 0.2f;
                        reloadMultiplier = 1.5f;
                        ammoMultiplier = 5;
                        lifetime = 60f;

                        trailLength = 5;
                        trailWidth = 1.5f;

                        hitEffect = despawnEffect = Fx.hitBulletColor;
                        hitColor = backColor = trailColor = Pal.siliconAmmoBack;
                        frontColor = Pal.siliconAmmoFront;
                    }}
            );

            shoot = new ShootAlternate(3.5f);

            recoils = 2;
            drawer = new DrawTurret(){{
                for(int i = 0; i < 2; i ++){
                    int f = i;
                    parts.add(new RegionPart("-barrel-" + (i == 0 ? "l" : "r")){{
                        progress = PartProgress.recoil;
                        recoilIndex = f;
                        under = true;
                        moveY = -1.5f;
                    }});
                }
            }};

            recoil = 0.5f;
            shootY = 3f;
            reload = 20f;
            range = 80;
            shootCone = 15f;
            ammoUseEffect = Fx.casing1;
            health = 250;
            inaccuracy = 2f;
            rotateSpeed = 10f;
            coolant = consume(new ConsumeCoolant(6f/60f){{
                filter = liquid -> liquid != Liquids.water && liquid.coolant && (this.allowLiquid && !liquid.gas || this.allowGas && liquid.gas) && liquid.temperature <= maxTemp && liquid.flammability < maxFlammability;
            }});
            researchCostMultiplier = 0.05f;

            limitRange(5f);
        }};

        //endregion

        //region effect

        deepSonar = new GroundPenetratingRadar("deep-sonar"){{
            requirements(Category.effect, with( Items.copper, 10, Items.lead, 10, Items.metaglass, 30, Items.silicon, 20));
            glowColor = outlineColor = Color.valueOf("00ffb2");
            fogRadius = 12;
            envEnabled |= Env.underwater;
            consumePower(6f/60f);
        }};

        // endregion
    }
}
