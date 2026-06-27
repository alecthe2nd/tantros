package tantros.world.consumers;

import arc.func.Func;
import arc.func.Prov;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.Time;
import mindustry.Vars;
import mindustry.gen.Building;
import mindustry.type.ItemStack;
import mindustry.type.LiquidStack;
import mindustry.type.PayloadStack;
import mindustry.ui.Bar;
import mindustry.ui.ReqImage;
import mindustry.world.Block;
import mindustry.world.consumers.Consume;
import mindustry.world.consumers.ConsumePowerDynamic;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatValues;
import mindustry.world.meta.Stats;
import tantros.type.Recipe;
import tantros.ui.UIUtil;
import tantros.world.blocks.BlockExtended;
import tantros.world.blocks.production.ProductionBlock;
import tantros.world.blocks.production.RecipeCrafter;
import tantros.world.meta.TantrosStats;

import static tantros.world.meta.TantrosStats.recipes;

public class ConsumeRecipesDynamic extends ExtendedConsume {

    public Prov<Seq<Recipe>> recipes;

    public Func<Building, Recipe> recipe;

    public ConsumePowerDynamic powerCons;

    public ConsumeRecipesDynamic(Func<Building, Recipe> recipe, Prov<Seq<Recipe>> recipes, Block block){
        this.recipe = recipe;
        this.recipes = recipes;
        powerCons = getPowerConsumer();
        block.consume(powerCons);
    }

    public ConsumePowerDynamic getPowerConsumer(){
        return new ConsumePowerDynamic((build)->{
            Recipe current = recipe.get(build);
            return (current != null)? current.cost.power: 0f;
        }){
            @Override
            public float efficiency(Building build) {
                Recipe current = recipe.get(build);
                if(current == null) return 0;
                return (current.cost.power > 0)? super.efficiency(build): 1f;
            }
        };
    }

    @Override
    public void apply(Block block) {

        for(Recipe recipe : recipes.get()){
            for(ItemStack stack: recipe.cost.items){
                block.hasItems = true;
                block.acceptsItems = true;
                block.itemFilter[stack.item.id] = true;
            }

            for(LiquidStack stack: recipe.cost.liquids){
                block.hasLiquids = true;
                block.liquidFilter[stack.liquid.id] = true;
            }

            if(recipe.cost.power > 0){
                block.hasPower = true;
                Log.info("Assigned power to " + block.localizedName);
            }
        }
    }

    @Override
    public void trigger(Building build){

        Recipe current = this.recipe.get(build);
        float mult = multiplier.get(build);

        if(current != null){

            for (ItemStack stack : current.cost.items) {
                build.items.remove(stack.item, Math.round(stack.amount * mult));
            }

            for(PayloadStack stack : current.cost.payloads){
                build.getPayloads().remove(stack.item, Math.round(stack.amount * mult));
            }
        }
    }

    @Override
    public float efficiency(Building build){
        return efficiency(build, this.recipe.get(build));
    }

    public float efficiency(Building building, Recipe current, float efficiency){

        float mult = multiplier.get(building);
        float ed = efficiency * Time.delta * building.efficiencyScale();


        float eff = 1f;

        boolean trigger = building.consumeTriggerValid();
        if(current != null) {

            for(ItemStack stack: current.cost.items) {

                eff = Math.min((trigger || building.items.has(stack.item, Math.round(stack.amount * multiplier.get(building)))) ? 1f : 0f, eff);

            }

            if(ed <= 0.00000001f) return 0f;

            for(var stack : current.cost.liquids){
                eff = Math.min(building.liquids.get(stack.liquid) / (stack.amount * ed * mult), eff);
            }

            for(PayloadStack stack : current.cost.payloads){
                if(!building.getPayloads().contains(stack.item, Math.round(stack.amount * mult))){
                    eff = 0;
                }
            }

            return eff;
        }

        return 0;
    }

    public float efficiency(Building building, Recipe current){
        return efficiency(building, current, building.efficiency);
    }

    @Override
    public void update(Building build){
        float mult = multiplier.get(build);

        Recipe current = recipe.get(build);
        if(current != null) {
            for (var stack : current.cost.liquids) {
                build.liquids.remove(stack.liquid, stack.amount * build.edelta() * mult);
            }
        }
    }

    @Override
    public void display(Stats stats){
        stats.add(booster ? Stat.booster : TantrosStats.recipes, (table)->{
            table.row();
            recipes(recipes.get()).display(table);
        });
    }

    @Override
    public void build(Building build, Table table) {
        Recipe current = this.recipe.get(build);
        table.table(c -> {
            int i = 0;
            for(var stack : current.cost.items){
                c.add(new ReqImage(StatValues.stack(stack.item, Math.round(stack.amount * multiplier.get(build))),
                        () -> build.items.has(stack.item, Math.round(stack.amount * multiplier.get(build))))).padRight(8);
                if(++i % 4 == 0) c.row();
            }

            int j = 0;
            for(var stack : current.cost.liquids){
                c.add(new ReqImage(stack.liquid.uiIcon,
                        () -> build.liquids.get(stack.liquid) > 0)).size(Vars.iconMed).padRight(8);
                if(++j % 4 == 0) c.row();
            }
        }).left();
    }

    @Override
    public void displayBars(BlockExtended.BuildExtended owner, Table table){
        Recipe current = recipe.get(owner);
        //set up liquid bars for liquid outputs
        if(current.cost.liquids != null && current.cost.liquids.size > 0){

            //then display output buffer
            for(var stack : current.cost.liquids){
                UIUtil.addBar(table, new Bar(
                        () -> stack.liquid.localizedName,
                        stack.liquid::barColor,
                        () -> owner.liquids.get(stack.liquid) / owner.block.liquidCapacity
                ));
            }
        }
    }
}
