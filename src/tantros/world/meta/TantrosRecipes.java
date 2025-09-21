package tantros.world.meta;

import mindustry.content.Items;
import mindustry.type.ItemStack;
import tantros.type.Recipe;
import tantros.type.Resource;

public class TantrosRecipes {

    public static Recipe
        metaglassAnnealing
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
    }
}
