package tantros.world.meta;

import arc.Core;
import arc.scene.ui.Image;
import arc.scene.ui.layout.Stack;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import arc.util.Scaling;
import mindustry.type.ItemStack;
import mindustry.type.Liquid;
import mindustry.type.LiquidStack;
import mindustry.ui.Styles;
import mindustry.world.meta.*;
import tantros.type.Recipe;
import tantros.type.Resource;
import tantros.world.environment.LocalEnv;

import static mindustry.Vars.iconMed;
import static mindustry.world.meta.StatValues.displayItem;
import static mindustry.world.meta.StatValues.withTooltip;

public class TantrosStats {

    public static StatCat

            environment = new StatCat("environment")

            ;

    public static Stat

            pressureRange = new Stat("pressurerange", StatCat.function),

            maxPressure = new Stat("maxpressure", StatCat.function),

            recipes = new Stat("recipes", StatCat.crafting),

            requiredEnvironments = new Stat("requiredenvironments", TantrosStats.environment);



    public static StatValue liquidEnvironmentReq(LocalEnv req){
        return table -> {
            int count = 0;
            for( Liquid liquid: req.liquids){
                table.add(displayLiquid(liquid)).padRight(5);
                if (count != 0){
                    table.add("+");
                }
                count ++;
            }
            /*
            Seq<Liquid> list = content.liquids().select(i -> filter.get(i) && i.unlockedNow() && !i.isHidden());

            for(int i = 0; i < list.size; i++){
                table.add(displayLiquid(list.get(i), amount, perSecond)).padRight(5);

                if(i != list.size - 1){
                    table.add("/");
                }
            }
            */
        };
    }

    public static Table displayLiquid(Liquid liquid){
        Table t = new Table();

        t.add(new Stack(){{
            add(new Image(liquid.uiIcon).setScaling(Scaling.fit));
        }}).size(iconMed).padRight(3).with(s -> withTooltip(s, liquid, false));

        t.add(liquid.localizedName);

        return t;
    }

    public static StatValue recipes(Seq<Recipe> recipes){
        return table -> {
            for(Recipe recipe : recipes){
                table.add(displayRecipe(recipe)).padRight(5);
            }
        };
    }

    public static Table displayRecipe(Recipe recipe){
        Table t = new Table(Styles.grayPanel);
        t.add(Core.bundle.get("recipe." + recipe.name + ".name"));
        t.row();
        t.add(displayResource(recipe.cost, recipe.craftTime)).padRight(5);
        t.row();
        t.add(displayResource(recipe.output, recipe.craftTime)).padRight(5);

        return t;
    }

    public static Table displayResource(Resource resource, float craftTime){
        Table t = new Table();

        t.table((matter)-> {
            for(ItemStack stack : resource.items){
                matter.add(displayItem(stack.item, stack.amount, craftTime, true)).padRight(5);
            }
            matter.row();
            for(LiquidStack stack: resource.liquids){
                matter.add(StatValues.displayLiquid(stack.liquid, stack.amount, true));
            }
        });

        t.table((energy)-> {
            if(resource.heat > 0) {
                StatValues.number(resource.heat, StatUnit.heatUnits).display(energy);
            }
            energy.row();
            if(resource.power > 0) {
                StatValues.number(resource.power * 60, StatUnit.powerSecond).display(energy);
            }
        });
        return t;
    }
}
