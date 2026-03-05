package tantros.content.recipes;

import mindustry.content.Items;
import mindustry.content.Liquids;
import mindustry.type.ItemStack;
import mindustry.type.LiquidStack;
import tantros.content.world.TantrosItems;
import tantros.content.world.TantrosLiquids;
import tantros.type.Recipe;
import tantros.type.Resource;

public class TantrosRecipes {

    public static Recipe
        nothing,
        metaglassAnnealing,
        surgeAnnealing,
        slagSurgeAnnealing,
        bluecystDecomposition,
        siliconPressureSmelting,
        hydrogenCombustion,
        electricHeating,
        berylliumOxidization,
        leadOxidization,
        carbonOxidization,
        siliconOxidization,
        siliconWeaving,
        phaseWeaving,
        graphitePressing,
        plastaniumCompressing,
        bluecystOilification,
        redcystOilification
        ;

    public static void load(){
        nothing = new Recipe("nothing");
        metaglassAnnealing = new Recipe("metaglass-annealing"){{
            cost = new Resource()
                   .withItems(ItemStack.with(Items.lead, 2, Items.sand, 3))
                   .withPower(1.5f);

            output = new Resource()
                   .withItems(new ItemStack(Items.metaglass, 4))
            ;
            overheat = 5;
            craftTime = 120f;
        }};

        surgeAnnealing = new Recipe("surge-annealing"){{
            cost = new Resource()
                    .withItems(ItemStack.with(Items.copper, 3, Items.lead, 4, Items.titanium, 2, Items.silicon, 3))
                    .withPower(4f);

            output = new Resource()
                    .withItems(new ItemStack(Items.surgeAlloy, 1))
            ;
            overheat = 5;
            craftTime = 120f;
        }};


        slagSurgeAnnealing = new Recipe("slag-surge-annealing"){{
            cost = new Resource()
                    .withItems(ItemStack.with(Items.silicon, 3))
                    .withLiquids(LiquidStack.with(Liquids.slag, 2f/3f))
                    .withPower(4f);

            output = new Resource()
                    .withItems(new ItemStack(Items.surgeAlloy, 1))
            ;
            overheat = 5;
            craftTime = 120f;
        }};
        bluecystDecomposition = new Recipe("bluecyst-decomposition"){{
            cost = new Resource()
                    .withItems(ItemStack.with(TantrosItems.bluecyst, 2))
                    .withPower(0.5f);

            output = new Resource()
                    .withItems(new ItemStack(Items.graphite, 1))
                    .withLiquids(new LiquidStack(Liquids.hydrogen, 0.5f / 60f))
            ;
            overheat = 5;
            craftTime = 120f;
            ignoreLiquidFullness = true;
        }};

        siliconPressureSmelting = new Recipe("silicon-pressure-smelting"){{
            cost = new Resource()
                    .withItems(ItemStack.with(TantrosItems.bluecyst, 1, Items.sand, 3))
                    .withPower(120f/60f);

            output = new Resource()
                    .withItems(new ItemStack(Items.silicon, 3))
            ;
            craftTime = 60f;
        }};

        siliconOxidization = new Recipe("silicon-oxidization"){{
            cost = new Resource()
                    .withItems(ItemStack.with(Items.silicon, 3))
                    .withLiquids(LiquidStack.with(Liquids.ozone, 3.6f/60f))
            ;
            output = new Resource()
                    .withHeat(12f).withItems(ItemStack.with(Items.sand, 3))
            ;
            craftTime = 100f;
        }};

        carbonOxidization = new Recipe("carbon-oxidization"){{
            cost = new Resource()
                    .withItems(ItemStack.with(Items.graphite, 3))
                    .withLiquids(LiquidStack.with(Liquids.ozone, 3.6f/60f))
            ;
            output = new Resource()
                    .withHeat(15f)
            ;
            craftTime = 100f;
        }};

        hydrogenCombustion = new Recipe("hydrogen-combustion"){{
            cost = new Resource()
                    .withLiquids(LiquidStack.with(Liquids.ozone, 2f / 60f, Liquids.hydrogen, 3f / 60f))
            ;
            output = new Resource()
                    .withLiquids(LiquidStack.with(Liquids.water, 5f/60f))
                    .withHeat(5f)
            ;
            craftTime = 60f;
        }};

        electricHeating = new Recipe("electric-heating"){{
            cost = new Resource()
                    .withPower(100f/60f)
            ;
            output = new Resource()
                    .withHeat(3f)
            ;
            craftTime = 10f;
        }};

        berylliumOxidization = new Recipe("beryllium-oxidization"){{
            cost = new Resource()
                    .withItems(ItemStack.with(Items.beryllium, 2))
                    .withLiquids(LiquidStack.with(Liquids.ozone, 4f/60f))
            ;
            output = new Resource()
                    .withHeat(15f).withItems(ItemStack.with(Items.oxide, 2))
            ;
            craftTime = 120f;
        }};

        leadOxidization = new Recipe("lead-oxidization"){{
            cost = new Resource()
                    .withItems(ItemStack.with(Items.lead, 3))
                    .withLiquids(LiquidStack.with(Liquids.ozone, 6f/60f))
            ;
            output = new Resource()
                    .withHeat(10f)//.withItems(ItemStack.with(Items.sand, 3))
            ;
            craftTime = 100f;
        }};

        siliconWeaving = new Recipe("silicon-weaving"){{
            cost = new Resource()
                    .withItems(ItemStack.with(
                            Items.sand, 8
                    ))
                    .withLiquids(LiquidStack.with(
                            Liquids.hydrogen, 8/60f
                    ))
                    .withHeat(15f)
                    .withPower(2)
                    ;
            overheat = 10;

            maxEfficiency = 2;

            output = new Resource()
                    .withItems(ItemStack.with(
                            Items.silicon, 8
                    ))
                    .withLiquids(LiquidStack.with(
                            TantrosLiquids.steam, 8/60f
                    ))
                    ;
            craftTime = 120f;

        }};

        phaseWeaving = new Recipe("phase-weaving"){{
            cost = new Resource()
                    .withItems(ItemStack.with(
                            Items.sand, 8,
                            Items.thorium, 3
                    ))
                    .withLiquids(LiquidStack.with(
                            Liquids.ozone, 6/60f
                    ))
                    .withHeat(15)
                    .withPower(4);

            overheat = 10;

            maxEfficiency = 2;

            output = new Resource()
                    .withItems(ItemStack.with(
                            Items.phaseFabric, 1
                    ))
            ;
            craftTime = 120f;
        }};

        graphitePressing = new Recipe("graphite-pressing"){{
            cost = new Resource()
                    .withItems(ItemStack.with(
                            Items.coal, 2
                    ))
                    .withPower(15/60f);

            output = new Resource()
                    .withItems(
                            ItemStack.with(
                                    Items.graphite, 1f
                            )
                    )
            ;
            craftTime = 60f;
        }};

        plastaniumCompressing = new Recipe("plastanium-compressing"){{
            cost = new Resource()
                    .withItems(ItemStack.with(
                            Items.titanium, 1
                    ))
                    .withLiquids(LiquidStack.with(
                            Liquids.oil, 15f/60f
                    ))
                    .withPower(15/60f);

            output = new Resource()
                    .withItems(
                            ItemStack.with(
                                    Items.plastanium, 1f
                            )
                    )
            ;
            craftTime = 60f;
        }};

        bluecystOilification = new Recipe("bluecyst-oilification"){{
            cost = new Resource()
                    .withItems(ItemStack.with(
                            TantrosItems.bluecyst, 1
                    ))
                    .withPower(1);

            output = new Resource()
                    .withLiquids(
                            LiquidStack.with(
                                    Liquids.oil, 18/60f
                            )
                    )
            ;
            craftTime = 60f;
        }};

        redcystOilification = new Recipe("redcyst-oilification"){{
            cost = new Resource()
                    .withItems(ItemStack.with(
                            TantrosItems.redcyst, 1
                    ))
                    .withPower(1);

            output = new Resource()
                    .withLiquids(
                            LiquidStack.with(
                                    Liquids.oil, 18/60f
                            )
                    )
            ;
            craftTime = 60f;
        }};
    }
}
