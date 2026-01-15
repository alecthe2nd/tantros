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
            sealed_node

                    ;
    public static void load(){


        Blocks.redmat.asFloor().liquidDrop = Liquids.ozone;
        Blocks.bluemat.asFloor().liquidDrop = Liquids.hydrogen;

        //endregion

        //region ore
        deepOreCopper = new DeepOreBlock(Items.copper);

        deepOreLead = new DeepOreBlock(Items.lead);

        deepOreCoal = new DeepOreBlock(Items.coal);

        deepOreTitanium = new DeepOreBlock(Items.titanium);

        deepOreBeryllium = new DeepOreBlock(Items.beryllium);

        deepOreTungsten = new DeepOreBlock(Items.tungsten);

        deepOreThorium = new DeepOreBlock(Items.thorium);

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


        TantrosDefense.load();
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
