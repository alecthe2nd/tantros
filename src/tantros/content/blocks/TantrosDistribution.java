package tantros.content.blocks;

import arc.Core;
import mindustry.content.Items;
import mindustry.type.Category;
import mindustry.world.Block;
import mindustry.world.blocks.distribution.*;
import mindustry.world.blocks.heat.HeatConductor;
import mindustry.world.blocks.liquid.Conduit;
import mindustry.world.draw.DrawDefault;
import mindustry.world.draw.DrawHeatInput;
import mindustry.world.draw.DrawHeatOutput;
import mindustry.world.draw.DrawMulti;
import mindustry.world.meta.BlockGroup;
import tantros.world.blocks.distribution.BoostDuct;
import tantros.world.blocks.distribution.BoostDuctBridge;
import tantros.world.blocks.distribution.BoostDuctRouter;
import tantros.world.blocks.distribution.liquidTransport.*;

import static mindustry.type.ItemStack.with;

public class TantrosDistribution {

    public static Block

            copperDuct,
            copperDuctRouter,
            copperDuctBridge,
            copperDuctUnloader,
            copperOverflowDuct,
            copperUnderflowDuct,

            pneumaticDuct,
            pneumaticDuctRouter,
            pneumaticDuctBridge,

            copperPipeline,
            copperPipelineRouter,
            copperPipelineBridge,
            copperLiquidContainer,
            copperLiquidTank,
            copperLiquidSilo,

            metaglassPipeline,
            metaglassPipelineRouter,
            metaglassPipelineBridge,
            metaglassLiquidContainer,
            metaglassLiquidTank,
            metaglassLiquidSilo,

            pressureReleaseVent,

            sealedHeatRedirector
                    ;

    public static void load() {

        copperDuctRouter = new DuctRouter("copper-duct-router"){{
            requirements(Category.distribution, with(Items.copper, 3));
            health = 90;
            speed = 10f;
            regionRotated1 = 1;
            solid = false;
            squareSprite = false;
            researchCost = with(Items.copper, 5);
        }};

        copperDuctBridge = new DuctBridge("copper-duct-bridge"){{
            requirements(Category.distribution, with(Items.copper, 6));
            health = 90;
            speed = 10f;
            researchCost = with(Items.copper, 5);
        }};

        copperDuct = new Duct("copper-duct"){{
            requirements(Category.distribution, with(Items.copper, 1));
            health = 90;
            speed = 10f;
            researchCost = with(Items.copper, 5);
            bridgeReplacement = copperDuctBridge;
        }};

        copperOverflowDuct = new OverflowDuct("copper-overflow-duct"){{
            requirements(Category.distribution, with(Items.copper, 5, Items.oxide, 2));
            health = 90;
            speed = 10f;
            solid = false;
            squareSprite = false;
            //researchCostMultiplier = 1.5f;
        }};

        copperUnderflowDuct = new OverflowDuct("copper-underflow-duct"){{
            requirements(Category.distribution, with(Items.copper, 5, Items.oxide, 2));
            health = 90;
            speed = 10f;
            solid = false;
            squareSprite = false;
            //researchCostMultiplier = 1.5f;
            invert = true;
        }};

        copperDuctUnloader = new DirectionalUnloader("copper-duct-unloader") {{
            requirements(Category.distribution, with(Items.copper, 30, Items.oxide, 20, Items.graphite, 10));
            health = 120;
            speed = 10f;
            solid = false;
            underBullets = true;
            regionRotated1 = 1;
        }};

        pneumaticDuctRouter = new BoostDuctRouter("pneumatic-duct-router"){{
            requirements(Category.distribution, with(Items.metaglass, 3, Items.graphite, 2));
            health = 90;
            speed = 7.5f;
            regionRotated1 = 1;
            solid = false;
            researchCost = with(Items.copper, 5);
            max_pressure = 20;
        }};

        pneumaticDuctBridge = new BoostDuctBridge("pneumatic-duct-bridge"){{
            requirements(Category.distribution, with(Items.metaglass, 5, Items.graphite, 4));
            health = 90;
            speed = 7.5f;
            researchCost = with(Items.copper, 5, Items.lead, 5);
            max_pressure = 20;
        }};

        pneumaticDuct = new BoostDuct("pneumatic-duct"){{
            requirements(Category.distribution, with(Items.metaglass, 2, Items.graphite, 1));
            health = 180;
            speed = 7.5f;
            bridgeReplacement = copperDuctBridge;
            max_pressure = 20;
        }};

        copperPipeline = new Pipeline("copper-pipeline"){{
            requirements(Category.liquid, with(Items.copper, 1, Items.metaglass, 1));
            health = 35;
            speed = 80f;
            leaks = true;
            liquidCapacity = 10;
        }};

        copperPipelineBridge = new PipelineBridge("copper-pipeline-bridge"){{
            requirements(Category.liquid, with(Items.copper, 3, Items.metaglass, 2, Items.oxide, 3));
            range = 4;
            hasPower = false;
            liquidCapacity = copperPipeline.liquidCapacity*2;
            researchCostMultiplier = 1;
            underBullets = true;
            health = 50;
            speed = 80f;

            ((Pipeline)copperPipeline).rotBridgeReplacement = this;
        }};

        copperPipelineRouter = new PipelineRouter("copper-pipeline-router"){{
            requirements(Category.liquid, with(Items.copper, 3, Items.metaglass, 2));
            health = 50;
            speed = 80f;
            liquidCapacity=copperPipeline.liquidCapacity;
            squareSprite = false;
        }};

        copperLiquidContainer = new PipelineTank("copper-liquid-container"){{
            requirements(Category.liquid, with(Items.copper, 30, Items.metaglass, 20));
            health = 45 * 4;
            speed = 80f;
            liquidCapacity = copperPipeline.liquidCapacity*4*10;
            size = 2;
            squareSprite = false;
        }};

        copperLiquidTank = new PipelineTank("copper-liquid-tank"){{
            requirements(Category.liquid, with(Items.copper, 90, Items.metaglass, 70));
            health = 40 * 9;
            speed = 80f;
            liquidCapacity = copperPipeline.liquidCapacity*9*15;
            size = 3;
            squareSprite = false;
        }};

        copperLiquidSilo = new PipelineTank("copper-liquid-silo"){{
            requirements(Category.liquid, with(Items.copper, 150, Items.metaglass, 90, Items.titanium, 60));
            health = 35 * 16;
            speed = 80f;
            liquidCapacity = copperPipeline.liquidCapacity*16*20;
            size = 4;
            squareSprite = false;
        }};

        metaglassPipeline = new Pipeline("metaglass-pipeline"){{
            requirements(Category.liquid, with(Items.metaglass, 3, Items.graphite, 1));
            health = 70;
            speed = 160f;
            leaks = false;
            liquidCapacity = 15;
        }};

        metaglassPipelineBridge = new PipelineBridge("metaglass-pipeline-bridge"){{
            requirements(Category.liquid, with(Items.metaglass, 3, Items.graphite, 2, Items.oxide, 3));
            range = 4;
            hasPower = false;
            liquidCapacity = metaglassPipeline.liquidCapacity*2;
            researchCostMultiplier = 1;
            underBullets = true;
            health = 85;
            speed = 160f;

            ((Pipeline)metaglassPipeline).rotBridgeReplacement = this;
        }};

        metaglassPipelineRouter = new PipelineRouter("metaglass-pipeline-router"){{
            requirements(Category.liquid, with(Items.metaglass, 3, Items.graphite, 2));
            health = 85;
            speed = 160f;
            liquidCapacity=metaglassPipeline.liquidCapacity;
            squareSprite = false;
        }};

        metaglassLiquidContainer = new PipelineTank("metaglass-liquid-container"){{
            requirements(Category.liquid, with(Items.metaglass, 30, Items.graphite, 20, Items.oxide, 16));
            health = 60 * 4;
            speed = 160f;
            liquidCapacity = metaglassPipeline.liquidCapacity*4*10;
            size = 2;
            squareSprite = false;
        }};

        metaglassLiquidTank = new PipelineTank("metaglass-liquid-tank"){{
            requirements(Category.liquid, with(Items.metaglass, 90, Items.graphite, 70, Items.oxide, 36));
            health = 55 * 9;
            speed = 160f;
            liquidCapacity = metaglassPipeline.liquidCapacity*9*15;
            size = 3;
            squareSprite = false;
        }};

        metaglassLiquidSilo = new PipelineTank("metaglass-liquid-silo"){{
            requirements(Category.liquid, with(Items.metaglass, 150, Items.graphite, 90, Items.titanium, 60, Items.oxide, 64));
            health = 50 * 16;
            speed = 160f;
            liquidCapacity = metaglassPipeline.liquidCapacity*16*20;
            size = 4;
            squareSprite = false;
        }};

        sealedHeatRedirector = new HeatConductor("sealed-heat-redirector"){{
            requirements(Category.crafting, with(Items.copper, 8, Items.oxide, 3));

            researchCostMultiplier = 10f;

            group = BlockGroup.heat;
            size = 2;
            drawer = new DrawMulti(
                    new DrawDefault(),
                    new DrawHeatOutput(),
                    new DrawHeatInput("-heat")
            );
            regionRotated1 = 1;
        }};

        pressureReleaseVent = new PipelineVent("copper-pipeline-vent"){{
            requirements(Category.liquid, with(Items.copper, 8, Items.metaglass, 8, Items.lead, 3));
            speed = 80f;
            leaks = true;
        }};
    }
}
