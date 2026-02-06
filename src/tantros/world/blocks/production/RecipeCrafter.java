package tantros.world.blocks.production;

import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.math.geom.Geometry;
import arc.scene.style.Drawable;
import arc.scene.style.TextureRegionDrawable;
import arc.scene.ui.ButtonGroup;
import arc.scene.ui.ImageButton;
import arc.scene.ui.layout.Table;
import arc.struct.EnumSet;
import arc.struct.IntSet;
import arc.struct.Seq;
import arc.util.Eachable;
import arc.util.Time;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.Vars;
import mindustry.content.Fx;
import mindustry.ctype.ContentType;
import mindustry.entities.Effect;
import mindustry.entities.units.BuildPlan;
import mindustry.gen.Building;
import mindustry.gen.Icon;
import mindustry.gen.Player;
import mindustry.gen.Sounds;
import mindustry.logic.LAccess;
import mindustry.type.ItemStack;
import mindustry.type.LiquidStack;
import mindustry.type.PayloadStack;
import mindustry.ui.Styles;
import mindustry.world.Block;
import mindustry.world.Tile;
import mindustry.world.blocks.heat.HeatBlock;
import mindustry.world.blocks.heat.HeatConductor;
import mindustry.world.blocks.heat.HeatConsumer;
import mindustry.world.blocks.liquid.Conduit;
import mindustry.world.draw.DrawBlock;
import mindustry.world.draw.DrawDefault;
import mindustry.world.meta.BlockFlag;
import tantros.content.recipes.TantrosRecipes;
import tantros.type.Recipe;
import tantros.type.production.ProduceRecipeDynamic;
import tantros.world.consumers.ConsumeRecipesDynamic;

import java.util.Arrays;

import static mindustry.Vars.tilesize;
import static mindustry.Vars.world;

public class RecipeCrafter extends ProductionBlock {

    public Seq<Recipe> recipes = new Seq<>();

    /** Liquid output directions, specified in the same order as outputLiquids. Use -1 to dump in every direction. Rotations are relative to block. */
    public int[] liquidOutputDirections = {-1};

    /** if true, crafters with multiple liquid outputs will dump excess when there's still space for at least one liquid type */
    public boolean dumpExtraLiquid = true;
    public boolean ignoreLiquidFullness = false;

    //public float craftTime = 80;
    public Effect updateEffect = Fx.none;
    public float updateEffectChance = 0.04f;
    public float updateEffectSpread = 4f;
    public float warmupSpeed = 0.019f;
    public float heatWarmupRate = 0.15f;

    public DrawBlock drawer = new DrawDefault();

    public RecipeCrafter(String name) {
        super(name);
        update = true;
        solid = true;
        hasItems = true;
        ambientSound = Sounds.loopMachine;
        sync = true;
        ambientSoundVolume = 0.03f;
        flags = EnumSet.of(BlockFlag.factory);
        drawArrow = false;
        configurable = true;
        config(Recipe.class, (RecipeCrafter.RecipeCrafterBuild build, Recipe recipe) -> build.currentRecipe = recipe);
        configClear((RecipeCrafter.RecipeCrafterBuild build) -> {
            Recipe next = recipes.find(build::isValid);
            if(next != null){
                build.currentRecipe = next;
                build.progress = 0;
            }
        });
    }

    @Override
    public void load(){
        super.load();

        drawer.load(this);
    }

    @Override
    public void init(){

        this.produce(new ProduceRecipeDynamic((ProductionBuild b) -> {
            if (b instanceof RecipeCrafterBuild r) return r.currentRecipe();
            return TantrosRecipes.nothing;
        }, this::recipes));
        var cons = this.consume(new ConsumeRecipesDynamic((Building b) -> {
                    if (b instanceof RecipeCrafterBuild r) return r.currentRecipe();
                    return TantrosRecipes.nothing;
        }, this::recipes, this));
        //for(Recipe recipe: cons.recipes){
        //    initRecipe(recipe);
        //}

        super.init();
    }

    public void initRecipe(Recipe recipe){
        if(recipe.cost.items.size > 0) {
            this.hasItems = true;
            this.acceptsItems = true;
        }

        if(recipe.cost.liquids.size > 0){
            this.hasLiquids = true;
        }

        if(recipe.cost.power > 0){
            if (!this.hasPower) {
                //consume(cons.powerCons);
            }
            this.hasPower = true;
        }

        if(recipe.output.items.size > 0) {
            this.hasItems = true;
        }

        if(recipe.output.liquids.size > 0){
            this.hasLiquids = true;
            this.outputsLiquid = true;
        }

        if(recipe.output.power > 0){
            this.hasPower = true;
        }
    }

    @Override
    public boolean rotatedOutput(int fromX, int fromY, Tile destination){
        if(!(destination.build instanceof Conduit.ConduitBuild)) return false;

        Building crafter = world.build(fromX, fromY);
        if(crafter == null) return false;
        int relative = Mathf.mod(crafter.relativeTo(destination) - crafter.rotation, 4);
        for(int dir : liquidOutputDirections){
            if(dir == -1 || dir == relative) return false;
        }

        return true;
    }

    @Override
    public void drawPlanRegion(BuildPlan plan, Eachable<BuildPlan> list){
        drawer.drawPlan(this, plan, list);
    }

    @Override
    public TextureRegion[] icons(){
        return drawer.finalIcons(this);
    }

    @Override
    public boolean outputsItems(){
        return recipes.contains((recipe)->!recipe.output.items.isEmpty());
    }

    @Override
    public void getRegionsToOutline(Seq<TextureRegion> out){
        drawer.getRegionsToOutline(this, out);
    }

    @Override
    public void drawOverlay(float x, float y, int rotation){
        Seq<LiquidStack> outputLiquids = null;

        Building build = Vars.world.buildWorld(x,y);
        if(build instanceof RecipeCrafterBuild crafter){
            Recipe current = crafter.currentRecipe;
            if(current != null){
                outputLiquids = current.output.liquids;
            }
        }

        for(int i = 0; i < liquidOutputDirections.length; i++){
            int dir = liquidOutputDirections[i];
            TextureRegion icon = (outputLiquids != null && outputLiquids.size > i)? outputLiquids.get(i).liquid.fullIcon: Icon.info.getRegion();

            if(dir != -1){
                Draw.rect(
                        icon,
                        x + Geometry.d4x(dir + rotation) * (size * tilesize / 2f + 4),
                        y + Geometry.d4y(dir + rotation) * (size * tilesize / 2f + 4),
                        8f, 8f
                );
            }
        }
    }

    public Seq<Recipe> recipes(){
        return this.recipes;
    }

    public void addRecipe(Recipe recipe){
        this.recipes.add(recipe);
    }

    public class RecipeCrafterBuild extends ProductionBuild implements HeatBlock, HeatConsumer {

        public Recipe currentRecipe = null;

        public float progress;
        public float warmup;

        //TODO sideHeat could be smooth
        public float[] sideHeat = new float[4];
        public float inputHeat = 0f;
        public float outputHeat = 0f;

        public Recipe currentRecipe(){
            return currentRecipe != null ? currentRecipe : TantrosRecipes.nothing;
        }

        @Override
        public void draw(){
            drawer.draw(this);
        }

        @Override
        public void drawLight(){
            super.drawLight();
            drawer.drawLight(this);
        }

        @Override
        public void updateTile(){
            inputHeat = calculateHeat(sideHeat);

            //heat approaches target at the same speed regardless of efficiency
            outputHeat = Mathf.approachDelta(outputHeat, ((currentRecipe != null)? currentRecipe.output.heat: 0f) * efficiency, heatWarmupRate * delta());

            if(currentRecipe == null && !recipes.isEmpty()){
                currentRecipe = recipes.first();
            }

            if(efficiency > 0){

                if(wasVisible && Mathf.chanceDelta(updateEffectChance)){
                    updateEffect.at(x + Mathf.range(size * updateEffectSpread), y + Mathf.range(size * updateEffectSpread));
                }
            }else{
                //Recipe next = cons.recipes.find(this::isValid);
                //if(next != null){
                //    currentRecipe = next;
                //    progress = 0;
                //}
            }
            super.updateTile();
        }

        public boolean isValid(Recipe recipe){
            if(recipe == null || recipe == TantrosRecipes.nothing) return false;

            float ed = efficiency * Time.delta * this.efficiencyScale();


            boolean valid = true;

            boolean trigger = this.consumeTriggerValid();

            for (ItemStack stack : recipe.cost.items) {

                valid &= trigger || this.items.has(stack.item, stack.amount);

            }

            for(var stack : recipe.cost.liquids){
                valid &= this.liquids.get(stack.liquid) > (stack.amount * ed);
            }

            for(PayloadStack stack : recipe.cost.payloads){
                if(!this.getPayloads().contains(stack.item, stack.amount)){
                    valid = false;
                }
            }

            return valid;

        }

        @Override
        public float getProgressIncrease(float baseTime){
            if(ignoreLiquidFullness){
                return super.getProgressIncrease(baseTime);
            }

            //limit progress increase by maximum amount of liquid it can produce
            float scaling = 1f, max = 1f;
            if(currentRecipe != null && !currentRecipe.output.liquids.isEmpty()){
                max = 0f;
                for(var s : currentRecipe.output.liquids){
                    float value = (liquidCapacity - liquids.get(s.liquid)) / (s.amount * edelta());
                    scaling = Math.min(scaling, value);
                    max = Math.max(max, value);
                }
            }

            //when dumping excess take the maximum value instead of the minimum.
            return super.getProgressIncrease(baseTime) * (dumpExtraLiquid ? Math.min(max, 1f) : scaling);
        }

        @Override
        public double sense(LAccess sensor){
            if(sensor == LAccess.progress) return progress();
            //attempt to prevent wild total liquid fluctuation, at least for crafters
            if(sensor == LAccess.totalLiquids && currentRecipe != null && !currentRecipe.output.liquids.isEmpty()) return liquids.get(currentRecipe.output.liquids.first().liquid);
            return super.sense(sensor);
        }

        @Override
        public float progress(){
            return Mathf.clamp(progress);
        }

        @Override
        public void write(Writes write){
            super.write(write);
            write.f(progress);
            write.f(warmup);
            write.f(outputHeat);
            write.s(currentRecipe == null ? -1 : currentRecipe.id);
        }

        @Override
        public void read(Reads read, byte revision){
            super.read(read, revision);
            progress = read.f();
            warmup = read.f();
            outputHeat = read.f();
            currentRecipe = Vars.content.getByID(ContentType.error, read.s());
        }

        public float warmupTarget(){
            float target;
            if(currentRecipe == null) return 0f;
            target = (currentRecipe.cost.heat > 0)? Mathf.clamp(inputHeat / currentRecipe.cost.heat): 1f;
            return target;
        }

        @Override
        public float heatRequirement(){
            return (currentRecipe != null)? currentRecipe.cost.heat: 0f;
        }

        @Override
        public float[] sideHeat(){
            return sideHeat;
        }

        @Override
        public float efficiencyScale(){
            if(currentRecipe == null) return 0f;
            float over = Math.max(inputHeat - currentRecipe.cost.heat, 0f);
            return Math.min(Mathf.clamp((currentRecipe.cost.heat > 0f)?(inputHeat / currentRecipe.cost.heat):1f) + ((currentRecipe.overheat > 0f)?(over / currentRecipe.overheat):0f) * currentRecipe.overheatScale, currentRecipe.maxEfficiency);
        }

        @Override
        public float heatFrac(){
            return (currentRecipe != null)? outputHeat / currentRecipe.output.heat: 0f;
        }

        @Override
        public float heat(){
            return outputHeat;
        }

        @Override
        public float calculateHeat(float[] sideHeat, IntSet cameFrom) {
            Arrays.fill(sideHeat, 0.0F);
            if (cameFrom != null) {
                cameFrom.clear();
            }

            float heat = 0.0F;

            for(Building build : this.proximity) {
                if (build != null && build.team == this.team && build instanceof HeatBlock) {
                    HeatBlock heater;
                    boolean var10000;
                    label59: {
                        heater = (HeatBlock)build;
                        Block var9 = build.block;
                        if (var9 instanceof HeatConductor cond) {
                            if (cond.splitHeat) {
                                var10000 = true;
                                break label59;
                            }
                        }

                        var10000 = false;
                    }

                    boolean split = var10000;
                    if (!build.block.rotate || !split && (this.relativeTo(build) + 2) % 4 == build.rotation || split && this.relativeTo(build) != build.rotation) {
                        float add;
                        label70: {
                            if (build instanceof HeatConductor.HeatConductorBuild hc) {
                                if (hc.cameFrom.contains(this.id()) && this.outputHeat > 0) {
                                    break label70;
                                }
                            }

                            float diff = Math.min(Math.abs(build.x - this.x), Math.abs(build.y - this.y)) / 8.0F;
                            int contactPoints = Math.min((int)((float)this.block.size / 2.0F + (float)build.block.size / 2.0F - diff), Math.min(build.block.size, this.block.size));
                            add = heater.heat() / (float)build.block.size * (float)contactPoints;
                            if (split) {
                                add /= 3.0F;
                            }

                            int var10001 = Mathf.mod(this.relativeTo(build), 4);
                            sideHeat[var10001] += add;
                            heat += add;
                        }

                        if (cameFrom != null) {
                            cameFrom.add(build.id);
                            if (build instanceof HeatConductor.HeatConductorBuild hc) {
                                cameFrom.addAll(hc.cameFrom);
                            }
                        }

                        if (heater instanceof HeatConductor.HeatConductorBuild) {
                            HeatConductor.HeatConductorBuild cond = (HeatConductor.HeatConductorBuild)heater;
                            cond.updateHeat();
                        }
                    }
                }
            }

            return heat;
        }

        @Override
        public boolean shouldShowConfigure(Player player){
            int count = 0;
            for(Recipe item : recipes){
                if (item.unlockedNow()){
                    count++;
                }
                if (count > 1){
                    return true;
                }
            }
            return false;
        }

        public Drawable recipeIcon(Recipe recipe){
            if(recipe.uiIcon.found()){
                return new TextureRegionDrawable(recipe.uiIcon);
            }
            if(!recipe.output.items.isEmpty()){
                return new TextureRegionDrawable(recipe.output.items.first().item.uiIcon);
            }
            if(!recipe.output.liquids.isEmpty()){
                return new TextureRegionDrawable(recipe.output.liquids.first().liquid.uiIcon);
            }
            if(recipe.output.power > 0){
                return new TextureRegionDrawable(Icon.power);
            }
            if(recipe.output.heat > 0){
                return new TextureRegionDrawable(Icon.waves);
            }
            return new TextureRegionDrawable(Icon.cancel);
        }

        @Override
        public void buildConfiguration(Table table){

            //if(currentRecipe == null){
            //    deselect();
            //    return;
            //}

            var group = new ButtonGroup<ImageButton>();
            group.setMinCheckCount(0);
            int i = 0, columns = 4;

            table.background(Styles.black6);

            var list = recipes;
            for(var item : list){
                if(item.unlockedNow()) {
                    ImageButton button = table.button(this.recipeIcon(item), Styles.clearNoneTogglei, 40f, () -> {
                        configure(item);
                        deselect();
                    }).tooltip(item.localizedName).group(group).get();

                    button.update(() -> button.setChecked(currentRecipe == item));

                    if (++i % columns == 0) {
                        table.row();
                    }
                }
            }
        }
    }
}
