package tantros.world.consumers;

import arc.struct.Seq;
import mindustry.gen.Building;
import mindustry.type.ItemStack;
import mindustry.type.LiquidStack;
import mindustry.type.PayloadStack;
import mindustry.world.Block;
import mindustry.world.consumers.Consume;
import mindustry.world.consumers.ConsumePower;
import mindustry.world.consumers.ConsumePowerDynamic;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatValues;
import mindustry.world.meta.Stats;
import tantros.type.Recipe;
import tantros.type.Resource;
import tantros.world.blocks.production.RecipeCrafter;
import tantros.world.meta.TantrosStats;

import static tantros.world.meta.TantrosStats.recipes;

public class ConsumeRecipes extends Consume {

    public Seq<Resource> resources;

    public Seq<Recipe> recipes;

    public ConsumePowerDynamic powerCons;

    public ConsumeRecipes(Seq<Recipe> recipes){
        this.recipes = recipes;
        powerCons = getPowerConsumer();
    }

    public ConsumePowerDynamic getPowerConsumer(){
        return new ConsumePowerDynamic((build)->{
            if(! (build instanceof RecipeCrafter.RecipeCrafterBuild crafter)) throw new ClassCastException("ConsumeRecipes consumers only function for RecipeCrafter blocks.");
            return (crafter.currentRecipe != null)? crafter.currentRecipe.cost.power: 0f;
        });
    }

    @Override
    public void apply(Block block) {

        if(! (block instanceof RecipeCrafter crafter)) throw new ClassCastException("ConsumeRecipes consumers only function for RecipeCrafter blocks.");

        for(Recipe recipe : recipes){
            for(ItemStack stack: recipe.cost.items){
                crafter.hasItems = true;
                crafter.acceptsItems = true;
                crafter.itemFilter[stack.item.id] = true;
            }

            for(LiquidStack stack: recipe.cost.liquids){
                crafter.hasLiquids = true;
                crafter.liquidFilter[stack.liquid.id] = true;
            }

            if(recipe.cost.power > 0){
                crafter.hasPower = true;
                crafter.consPower = powerCons;
            }
        }
    }

    @Override
    public void trigger(Building build){
        if(! (build instanceof RecipeCrafter.RecipeCrafterBuild crafter)) throw new ClassCastException("ConsumeRecipes consumers only function for RecipeCrafter blocks.");

        Recipe current = crafter.currentRecipe;
        float mult = multiplier.get(build);

        if(current != null){

            for (ItemStack stack : current.cost.items) {
                build.items.remove(stack.item, Math.round(stack.amount * multiplier.get(build)));
            }

            for(PayloadStack stack : current.cost.payloads){
                build.getPayloads().remove(stack.item, Math.round(stack.amount * mult));
            }
        }
    }

    @Override
    public float efficiency(Building build){
        if(! (build instanceof RecipeCrafter.RecipeCrafterBuild crafter)) throw new ClassCastException("ConsumeRecipes consumers only function for RecipeCrafter blocks.");
        return efficiency(crafter, crafter.currentRecipe);
    }

    public float efficiency(RecipeCrafter.RecipeCrafterBuild crafter, Recipe current){

        float mult = multiplier.get(crafter);
        float ed = crafter.edelta() * crafter.efficiencyScale();


        float eff = (!crafter.consumeTriggerValid())?0f:1f;
        if(current != null) {

            for(ItemStack stack: current.cost.items) {
                eff = crafter.items.has(stack.item, Math.round(stack.amount * multiplier.get(crafter))) ? 1f : 0f;
            }

            if(ed <= 0.00000001f){
                eff = 0;
            }

            for(var stack : current.cost.liquids){
                eff = Math.min(crafter.liquids.get(stack.liquid) / (stack.amount * ed * mult), eff);
            }

            for(PayloadStack stack : current.cost.payloads){
                if(!crafter.getPayloads().contains(stack.item, Math.round(stack.amount * mult))){
                    eff = 0;
                }
            }

            return eff;
        }

        return 0;
    }

    @Override
    public void update(Building build){
        if(! (build instanceof RecipeCrafter.RecipeCrafterBuild crafter)) throw new ClassCastException("ConsumeRecipes consumers only function for RecipeCrafter blocks.");
        float mult = multiplier.get(build);

        Recipe current = crafter.currentRecipe;
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
