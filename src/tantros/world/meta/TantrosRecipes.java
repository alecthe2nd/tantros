package tantros.world.meta;

import mindustry.content.Items;
import mindustry.content.Liquids;
import mindustry.type.Item;
import mindustry.type.ItemStack;
import mindustry.type.LiquidStack;
import tantros.type.Recipe;
import tantros.type.Resource;

public class TantrosRecipes {

    public static Recipe
        metaglassAnnealing,
        coalDecomposition,
        siliconPressureSmelting
        ;

    public static void load(){
        metaglassAnnealing = new Recipe("metaglass-annealing"){{
            cost = new Resource()
                   .withItems(ItemStack.with(Items.lead, 2, Items.sand, 3))
                   .withPower(1.5f);
            ;
            output = new Resource()
                   .withItems(new ItemStack(Items.metaglass, 4))
            ;
            overheat = 5;
            craftTime = 120f;
        }};
        coalDecomposition = new Recipe("coal-decomposition"){{
            cost = new Resource()
                    .withItems(ItemStack.with(Items.coal, 2))
                    .withPower(0.5f);
            ;
            output = new Resource()
                    .withItems(new ItemStack(Items.graphite, 1))
                    .withLiquids(new LiquidStack(Liquids.hydrogen, 0.5f / 60f))
            ;
            overheat = 5;
            craftTime = 120f;
        }};

        siliconPressureSmelting = new Recipe("silicon-pressure-smelting"){{
            cost = new Resource()
                    .withItems(ItemStack.with(Items.coal, 1, Items.sand, 3))
                    .withLiquids(LiquidStack.with(Liquids.hydrogen, 0.5f/60f))
                    .withPower(120f/60f);
            ;
            output = new Resource()
                    .withItems(new ItemStack(Items.silicon, 3))
            ;
            craftTime = 60f;
        }};
    }
}
