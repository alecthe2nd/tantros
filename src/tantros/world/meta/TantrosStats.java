package tantros.world.meta;

import arc.Core;
import arc.scene.Element;
import arc.scene.ui.Image;
import arc.scene.ui.Label;
import arc.scene.ui.layout.Cell;
import arc.scene.ui.layout.Stack;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import arc.util.Scaling;
import mindustry.gen.Icon;
import mindustry.graphics.Pal;
import mindustry.type.ItemStack;
import mindustry.type.Liquid;
import mindustry.type.LiquidStack;
import mindustry.ui.Styles;
import mindustry.world.meta.*;
import tantros.type.Recipe;
import tantros.type.Resource;
import tantros.world.environment.LocalEnv;

import java.util.Locale;

import static mindustry.Vars.*;
import static mindustry.world.meta.StatValues.displayItem;
import static mindustry.world.meta.StatValues.fixValue;
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

            requiredEnvironments = new Stat("requiredenvironments", TantrosStats.environment),

            boreRange = new Stat("borerange", StatCat.crafting);



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
                if(recipe.unlockedNow()) {
                    stack.add(displayNamedRecipe(recipe)).left().fill().pad(5).margin(5);
                } else{
                    stack.image(Icon.lock).color(Pal.darkerGray).size(40);
                }
                stack.row();
            }
            table.add(stack).width(600f);
        };
    }

    public static StatValue recipe(Recipe recipe){
        return table -> {
            table.add(displayRecipe(recipe));
        };
    }

    public static void addRecipeTable(Table t, Recipe recipe){
        t.table((t_in)-> {
            t_in.image().pad(5).padLeft(0).padRight(0).height(3).color(Pal.darkerGray).growX();
            t_in.image().pad(5).padLeft(0).padRight(0).height(3).color(Pal.darkerGray).growX();
            t_in.row();

            t_in.add(Core.bundle.get("recipe.generic.cost.title")).left().fill().padLeft(5);
            displayResource(t_in, recipe.cost, recipe.craftTime);
            t_in.row();

            t_in.image().pad(5).padLeft(0).padRight(0).height(3).color(Pal.darkerGray).growX();
            t_in.image().pad(5).padLeft(0).padRight(0).height(3).color(Pal.darkerGray).growX();
            t_in.row();

            t_in.add(Core.bundle.get("recipe.generic.output.title")).left().padLeft(5);
            displayResource(t_in, recipe.output, recipe.craftTime);
            t_in.row();

            t_in.image().pad(5).padLeft(0).padRight(0).height(3).color(Pal.darkerGray).growX();
            t_in.image().pad(5).padLeft(0).padRight(0).height(3).color(Pal.darkerGray).growX();
        }).left().growX();
        if(recipe.overheat > 0) {
            t.row();
            recipe.displayOverheat(t);
        }
        t.row();

        t.add(
                Core.bundle.get("recipe.generic.production-time.format")
                        .replace("{0}", fixValue(recipe.craftTime /60))
                        .replace("{1}", StatUnit.seconds.localized())
        ).left();


    }

    public static Table displayRecipe(Recipe recipe){
        Table t = new Table(Styles.grayPanel);
        addRecipeTable(t, recipe);
        return t;
    }

    public static Table displayNamedRecipe(Recipe recipe){
        Table t = new Table(Styles.grayPanel);
        Label name = new Label(recipe.localizedName);
        t.table((ta)->{
            ta.add(name);
        });
        withTooltip(name, recipe, true);
        t.row();
        addRecipeTable(t, recipe);
        return t;
    }

    public static void displayResource(Table t, Resource resource, float craftTime){
        t.table((all)->{
            all.table((matter)-> {
                int count = 0;
                for(ItemStack stack : resource.items){
                    matter.add(displayItem(stack.item, stack.amount, craftTime, true)).padRight(5).left();
                    if(count % 2 == 1){
                        matter.row();
                    }
                    count++;
                }
                matter.row();
                count = 0;
                for(LiquidStack stack: resource.liquids){
                    matter.add(StatValues.displayLiquid(stack.liquid, stack.amount * 60, true)).padRight(5).left();
                    if(count % 2 == 1){
                        matter.row();
                    }
                    count++;
                }
            });

            all.table((energy)-> {
                if(resource.heat > 0) {
                    StatValues.number(resource.heat, StatUnit.heatUnits).display(energy);
                }
                energy.row();
                if(resource.power > 0) {
                    StatValues.number(resource.power * 60, StatUnit.powerUnits).display(energy);
                }
            });
        }).growX().padTop(5).padBottom(5);
    }



    public static StatValue withEfficiencyMultiplier(float efficiencyMultiplier, StatValue value){
        return table -> {
            if(table.getCells().size > 0) table.getCells().peek().growX(); //Expand the spacer on the row above to push everything to the left
            table.row();
            table.table(Styles.grayPanel,c -> {
                value.display(c);
                c.add(Core.bundle.format("stat.efficiency", fixValue(efficiencyMultiplier * 100f))).right().pad(10f).padRight(15f);
            }).growX().colspan(table.getColumns()).row();
        };
    }

    public static void displayStat(Table table, Stat stat, float value, StatUnit unit){
        displayStat(table, stat, StatValues.number(value, unit));
    }

    public static void displayStat(Table table, Stat stat, StatValue value){
        table.table(inset -> {
            inset.left();
            Cell<Label> cell = inset.add("[lightgray]" + stat.localized() + ":[] ").left().top();
            if(cell != null) {
                String key = "stat." + stat.name.toLowerCase(Locale.ROOT);
                if (Core.bundle.has(key + ".info")) {
                    cell.tooltip("@" + key + ".info");
                }
            }
            value.display(inset);
            inset.add().size(10f);
        }).fillX().padLeft(10).row();
    }
}
