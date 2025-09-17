package tantros.content.blocks;

import mindustry.content.*;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.world.Block;
import mindustry.world.blocks.defense.Wall;
import mindustry.world.blocks.environment.OreBlock;
import mindustry.world.blocks.power.PowerNode;
import mindustry.world.meta.BuildVisibility;
import mindustry.world.meta.Env;
import tantros.content.world.TantrosUnitTypes;
import tantros.world.blocks.environment.DeepOreBlock;
import tantros.world.blocks.storage.CustomCoreBlock;

import static mindustry.type.ItemStack.with;

public class TantrosBlocks {

    public static Block

            //ore
            wallOreCopper, wallOreLead, wallOreCoal,
            deepOreCopper,
            deepOreLead,
            deepOreCoal,
            deepOreTitanium,
            deepOreBeryllium,
            deepOreTungsten,
            deepOreThorium,

            //storage
            coreShell,

            //power
            sealed_node,

            //defense
            copperBulkhead, largeCopperBulkhead
                    ;
    public static void load(){


        Blocks.redmat.asFloor().liquidDrop = Liquids.ozone;
        Blocks.bluemat.asFloor().liquidDrop = Liquids.hydrogen;

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

        //region distribution
        //endregion
        //region storage

        coreShell = new CustomCoreBlock("core-shell"){{
            requirements(Category.effect, BuildVisibility.coreZoneOnly, with(Items.copper, 800, Items.lead, 800, Items.metaglass, 1000));
            alwaysUnlocked = true;
            squareSprite = false;
            isFirstTier = true;
            unitType = TantrosUnitTypes.enact;
            health = 1100;
            hasItems = true;
            itemCapacity = 4000;
            size = 4;
            buildCostMultiplier = 2f;

            unitCapModifier = 8;
        }};

        //endregion

        //region power
        sealed_node = new PowerNode("sealed-node"){{
            requirements(Category.power, with(Items.copper, 2, Items.lead, 6));
            maxNodes = 10;
            laserRange = 6;
            buildCostMultiplier = 2.5f;
        }};
        //endregion

        //region crafters

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

        TantrosEnvironment.load();
        TantrosDistribution.load();
        TantrosSource.load();
        TantrosProduction.load();
        TantrosEffect.load();
        TantrosPower.load();
        TantrosPayload.load();
        TantrosTurret.load();
    }
}
