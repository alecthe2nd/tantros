package tantros.content.blocks;

import arc.Core;
import mindustry.content.Items;
import mindustry.type.Category;
import mindustry.world.Block;
import mindustry.world.blocks.distribution.*;
import mindustry.world.blocks.heat.HeatConductor;
import mindustry.world.draw.DrawDefault;
import mindustry.world.draw.DrawHeatInput;
import mindustry.world.draw.DrawHeatOutput;
import mindustry.world.draw.DrawMulti;
import mindustry.world.meta.BlockGroup;
import tantros.world.blocks.distribution.BoostDuct;
import tantros.world.blocks.distribution.BoostDuctBridge;
import tantros.world.blocks.distribution.BoostDuctRouter;
import tantros.world.blocks.distribution.liquidTransport.Pipeline;
import tantros.world.blocks.distribution.liquidTransport.PipelineRouter;
import tantros.world.blocks.distribution.liquidTransport.PipelineTank;
import tantros.world.blocks.distribution.liquidTransport.PipelineVent;

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
            copperLiquidContainer,
            copperLiquidTank,
            copperLiquidSilo,

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
            researchCost = with(Items.copper, 5);
        }};

        copperDuctBridge = new DuctBridge("copper-duct-bridge"){{
            requirements(Category.distribution, with(Items.copper, 3));
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
            speed = 10f;
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
            health = 180;
            speed = 80f;
            leaks = true;
        }};

        copperPipelineRouter = new PipelineRouter("copper-pipeline-router"){{
            requirements(Category.liquid, with(Items.copper, 3, Items.metaglass, 2));
            health = 180;
            speed = 80f;
        }};

        copperLiquidContainer = new PipelineTank("copper-liquid-container"){{
            requirements(Category.liquid, with(Items.copper, 30, Items.metaglass, 20));
            health = 180;
            speed = 80f;
            liquidCapacity = 600;
            size = 2;
            squareSprite = false;
        }};

        copperLiquidTank = new PipelineTank("copper-liquid-tank"){{
            requirements(Category.liquid, with(Items.copper, 90, Items.metaglass, 70));
            health = 180;
            speed = 80f;
            liquidCapacity = 1600;
            size = 3;
            squareSprite = false;
        }};

        copperLiquidSilo = new PipelineTank("copper-liquid-silo"){{
            requirements(Category.liquid, with(Items.copper, 150, Items.metaglass, 90, Items.titanium, 60));
            health = 180;
            speed = 80f;
            liquidCapacity = 4000;
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
