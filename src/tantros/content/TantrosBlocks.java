package tantros.content;

import arc.struct.Seq;
import mindustry.content.Items;
import mindustry.content.Liquids;
import mindustry.type.Category;
import mindustry.world.Block;
import mindustry.world.blocks.distribution.Duct;
import mindustry.world.blocks.environment.OreBlock;
import mindustry.world.blocks.power.PowerGenerator;
import mindustry.world.blocks.power.PowerNode;
import mindustry.world.blocks.production.BeamDrill;
import mindustry.world.blocks.production.Drill;
import mindustry.world.draw.DrawBlurSpin;
import mindustry.world.draw.DrawDefault;
import mindustry.world.draw.DrawMulti;
import mindustry.world.meta.Env;
import tantros.content.world.blocks.power.PassiveGenerator;
import tantros.content.world.blocks.production.Sifter;
import tantros.content.world.draw.DrawSpin;

import static mindustry.type.ItemStack.with;

public class TantrosBlocks {

    public static Block
            copperDuct, pressurizedDuct,

    //drills
    copperBore, siltSifter,
    //ore
    wallOreCopper, wallOreLead, wallOreCoal,

    //power
    sealed_node, tidal_turbine;

    public static void load(){

        //region distribution
        copperDuct = new Duct("copper-duct"){{
            requirements(Category.distribution, with(Items.copper, 1));
            health = 90;
            speed = 12.28f;
            researchCost = with(Items.copper, 5);
        }};
        pressurizedDuct = new Block("pressurized-duct");
        //endregion

        //region production
        copperBore = new BeamDrill("copper-bore"){{
            requirements(Category.production, with(Items.copper, 40));

            drillTime = 360f;
            tier = 2;
            size = 2;
            range = 2;
            researchCost = with(Items.copper, 10);

            consumeLiquid(Liquids.hydrogen, 0.25f / 60f).boost();
        }};



        siltSifter = new Sifter("silt-sifter"){{
            requirements(Category.production, with(Items.copper, 12));
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
    }
}
