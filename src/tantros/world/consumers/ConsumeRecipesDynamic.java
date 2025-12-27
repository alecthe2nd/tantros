package tantros.world.consumers;

import arc.func.Prov;
import arc.struct.Seq;
import arc.util.Time;
import mindustry.gen.Building;
import mindustry.type.ItemStack;
import mindustry.type.LiquidStack;
import mindustry.type.PayloadStack;
import mindustry.world.Block;
import mindustry.world.consumers.Consume;
import mindustry.world.consumers.ConsumePowerDynamic;
import mindustry.world.meta.Stat;
import mindustry.world.meta.Stats;
import tantros.type.Recipe;
import tantros.world.blocks.production.RecipeCrafter;
import tantros.world.meta.TantrosStats;

import static tantros.world.meta.TantrosStats.recipes;

public class ConsumeRecipesDynamic extends Consume {

    public Seq<Recipe> recipes;

    public Prov<Recipe> recipe;

    public ConsumePowerDynamic powerCons;

    public ConsumeRecipesDynamic(Prov<Recipe> recipe, Seq<Recipe> recipes){
        this.recipe = recipe;
        powerCons = getPowerConsumer();
    }

    public ConsumePowerDynamic getPowerConsumer(){
        return new ConsumePowerDynamic((build)->{
            if(! (build instanceof RecipeCrafter.RecipeCrafterBuild crafter)) throw new ClassCastException("ConsumeRecipes consumers only function for RecipeCrafter blocks.");
            Recipe current = recipe.get();
            return (current != null)? current.cost.power: 0f;
        }){
            @Override
            public float efficiency(Building build) {
                if(! (build instanceof RecipeCrafter.RecipeCrafterBuild crafter)) throw new ClassCastException("ConsumeRecipes consumers only function for RecipeCrafter blocks.");
                Recipe current = recipe.get();
                if(current == null) return 0;
                return (current.cost.power > 0)? super.efficiency(build): 1f;
            }
        };
    }

    @Override
    public void apply(Block block) {

        for(Recipe recipe : recipes){
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
                block.consPower = powerCons;
            }
        }
    }

    @Override
    public void trigger(Building build){

        Recipe current = this.recipe.get();
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
        return efficiency(build, this.recipe.get());
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

            //if(ed <= 0.00000001f) return 0f;

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

        Recipe current = recipe.get();
        if(current != null) {
            for (var stack : current.cost.liquids) {
                build.liquids.remove(stack.liquid, stack.amount * build.edelta() * mult);
            }
        }
    }

    @Override
    public void display(Stats stats){
        stats.add(booster ? Stat.booster : TantrosStats.recipes, recipes(recipes));
    }
}
