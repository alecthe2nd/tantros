package tantros.world.meta;

import arc.Core;
import arc.scene.ui.Image;
import arc.scene.ui.Label;
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

            recipe = new Stat("recipe", StatCat.crafting),

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
            Table stack = new Table();
            for(Recipe recipe : recipes){
                stack.add(displayNamedRecipe(recipe));
                stack.row();
            }
            table.add(stack);
        };
    }

    public static StatValue recipe(Recipe recipe){
        return table -> {
            table.add(displayRecipe(recipe));
        };
    }

    public static void addRecipeTable(Table t, Recipe recipe){
        t.table((t_in)-> {
            t_in.add(Core.bundle.get("recipe.generic.cost.title")).left();
            displayResource(t_in, recipe.cost, recipe.craftTime);
        }).left();
        t.row();
        t.table((t_out)-> {
            t_out.add(Core.bundle.get("recipe.generic.output.title")).left();
            displayResource(t_out, recipe.output, recipe.craftTime);
        }).left();
        if(recipe.overheat > 0) {
            t.row();
            recipe.displayOverheat(t);
            /*t.table((t_extra) -> {
                t_extra.table(Styles.grayPanel, (t_overheat) -> {
                    t_overheat.add(Core.bundle.get("recipe.generic.overheat.title")).left();
                    t_overheat.row();
                    StatValues.number(recipe.overheat, StatUnit.heatUnits, true).display(t_overheat);
                }).left().padRight(15);
                t_extra.table(Styles.grayPanel, (t_eboost) -> {
                    t_eboost.add(Core.bundle.get("recipe.generic.efficiency-boost.title")).left();
                    t_eboost.row();
                    t_eboost.add(
                            Core.bundle.get("recipe.generic.efficiency-boost.format").replace("{0}", StatValues.fixValue(recipe.overheatScale * 100))
                                    + Core.bundle.get("recipe.generic.max-efficiency.format").replace("{0}", StatValues.fixValue(recipe.maxEfficiency * 100))
                    ).left();
                }).left();
            }).left();*/
        }
    }

    public static Table displayRecipe(Recipe recipe){
        Table t = new Table(Styles.grayPanel);
        addRecipeTable(t, recipe);
        return t;
    }

    public static Table displayNamedRecipe(Recipe recipe){
        Table t = new Table(Styles.grayPanel);
        Label name = new Label(recipe.localizedName);
        t.add(name);
        withTooltip(name, recipe, true);
        t.row();
        addRecipeTable(t, recipe);
        return t;
    }

    public static void displayResource(Table t, Resource resource, float craftTime){

        t.table((matter)-> {
            int count = 0;
            for(ItemStack stack : resource.items){
                matter.add(displayItem(stack.item, stack.amount, craftTime, true)).padRight(5);
                if(count % 3 == 2){
                    matter.row();
                }
                count++;
            }
            matter.row();
            count = 0;
            for(LiquidStack stack: resource.liquids){
                matter.add(StatValues.displayLiquid(stack.liquid, stack.amount * 60, true));
                if(count % 3 == 2){
                    matter.row();
                }
                count++;
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
    }
}
