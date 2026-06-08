package tantros.type.production;

import arc.func.Func;
import arc.func.Prov;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import mindustry.type.ItemStack;
import mindustry.type.LiquidStack;
import mindustry.ui.Bar;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatUnit;
import mindustry.world.meta.Stats;
import tantros.type.Recipe;
import tantros.type.Resource;
import tantros.ui.UIUtil;
import tantros.world.blocks.production.ProductionBlock;

public class ProduceRecipeDynamic extends Produce{

    public Func<ProductionBlock.ProductionBuild, Recipe> recipe;
    public Prov<Seq<Recipe>> recipes;

    public ProduceRecipeDynamic(Func<ProductionBlock.ProductionBuild, Recipe> recipe, Prov<Seq<Recipe>> recipes){
        this.recipe = recipe;
        this.recipes = recipes;
    }

    @Override
    public Resource output(ProductionBlock.ProductionBuild build) {
        return recipe.get(build).output;
    }

    @Override
    public void apply(ProductionBlock block) {

        for(Recipe recipe : recipes.get()){
            for(ItemStack stack: recipe.output.items){
                block.hasItems = true;
                block.acceptsItems = true;
                block.itemFilter[stack.item.id] = true;
            }

            for(LiquidStack stack: recipe.output.liquids){
                block.hasLiquids = true;
                block.liquidFilter[stack.liquid.id] = true;
            }

            if(recipe.output.power > 0){
                block.hasPower = true;
            }

            if(recipe.output.heat > 0){
                block.rotate = true;
                block.rotateDraw = false;
                block.drawArrow = true;
            }
        }
    }

    @Override
    public void build(ProductionBlock.ProductionBuild build, Table table) {

    }

    @Override
    public void trigger(ProductionBlock.ProductionBuild build) {
        if(!isActive.get()) return;
        Recipe current = recipe.get(build);
        if(current != null){
            for(var output : current.output.items){
                for(int i = 0; i < output.amount; i++){
                    build.offload(output.item);
                }
            }
        }
    }

    @Override
    public void update(ProductionBlock.ProductionBuild build) {
        if(!isActive.get()) return;
        Recipe current = recipe.get(build);
        //continuously output based on efficiency
        float inc = build.getProgressIncrease(1f);
        for(var output : current.output.liquids){
            build.handleLiquid(build, output.liquid, Math.min(output.amount * inc, build.block.liquidCapacity - build.liquids.get(output.liquid)));
        }
        build.powerProductionEfficiency = build.efficiency;
    }

    @Override
    public boolean canCraft(ProductionBlock.ProductionBuild build) {
        Recipe current = recipe.get(build);
        for(var output : current.output.items){
            if(!dumpExcessItems && build.items.get(output.item) + output.amount > build.block.itemCapacity){
                return false;
            }
        }

        if(!current.ignoreLiquidFullness){
            boolean allFull = true;
            for(var output : current.output.liquids){
                if(build.liquids.get(output.liquid) >= build.block.liquidCapacity - 0.001f){
                    if(current.dumpExcessLiquids){
                        return false;
                    }
                }else{
                    //if there's still space left, it's not full for all liquids
                    allFull = false;
                }
            }

            //if there is no space left for any liquid, it can't reproduce
            //only relevant if liquids are being outputted
            if(allFull && !current.output.liquids.isEmpty()){
                return false;
            }
        }

        return true;
    }

    @Override
    public float progressLimit(ProductionBlock.ProductionBuild build) {
        Recipe current = recipe.get(build);
        if(current.ignoreLiquidFullness){
            return 1f;
        }

        //limit progress increase by maximum amount of liquid it can produce
        float scaling = 1f, max = 1f;
        if(!current.output.liquids.isEmpty()){
            max = 0f;
            for(var s : current.output.liquids){
                float value = (build.block.liquidCapacity - build.liquids.get(s.liquid)) / (s.amount * build.edelta());
                scaling = Math.min(scaling, value);
                max = Math.max(max, value);
            }
        }

        //when dumping excess take the maximum value instead of the minimum.
        return (current.dumpExcessLiquids ? Math.min(max, 1f) : scaling);
    }

    @Override
    public void display(Stats stats, ProductionBlock block) {
    }

    @Override
    public float productionTimeMultiplier(ProductionBlock.ProductionBuild build){
        return this.recipe.get(build).craftTime;
    }

    @Override
    public void displayBars(ProductionBlock.ProductionBuild owner, Table table) {
        Recipe current = recipe.get(owner);
        //set up liquid bars for liquid outputs
        if(current.output.liquids != null && current.output.liquids.size > 0){

            //then display output buffer
            for(var stack : current.output.liquids){
                UIUtil.addBar(table, new Bar(
                    () -> stack.liquid.localizedName,
                        stack.liquid::barColor,
                    () -> owner.liquids.get(stack.liquid) / owner.block.liquidCapacity
                ));
            }
        }
    }
}
