package tantros.content;

import arc.graphics.Color;
import arc.math.Interp;
import arc.struct.Seq;
import mindustry.content.Fx;
import mindustry.content.Items;
import mindustry.content.Liquids;
import mindustry.gen.Sounds;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.type.LiquidStack;
import mindustry.world.Block;
import mindustry.world.blocks.distribution.Duct;
import mindustry.world.blocks.distribution.DuctBridge;
import mindustry.world.blocks.distribution.DuctRouter;
import mindustry.world.blocks.environment.OreBlock;
import mindustry.world.blocks.power.PowerNode;
import mindustry.world.blocks.production.BeamDrill;
import mindustry.world.blocks.production.GenericCrafter;
import mindustry.world.draw.*;
import mindustry.world.meta.Env;
import tantros.content.world.blocks.power.PassiveGenerator;
import tantros.content.world.blocks.production.Sifter;
import tantros.content.world.draw.DrawSpin;

import static mindustry.type.ItemStack.with;

public class TantrosBlocks {

    public static Block
            copperDuct, copperDuctRouter, copperDuctBridge,
            pressurizedDuct,

            //drills
            copperBore, siltSifter,

            //crafter
            metaglassAnnealer, graphiticDecomposer,

            //ore
            wallOreCopper, wallOreLead, wallOreCoal,

            //power
            sealed_node, tidal_turbine;

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

            drillTime = 360f;
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
        //endregion
        //region ore
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
        //endregion
    }
}
