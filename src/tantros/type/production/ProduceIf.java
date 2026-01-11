package tantros.type.production;

import arc.func.Boolf;
import arc.func.Boolp;
import arc.scene.ui.layout.Table;
import mindustry.game.Team;
import mindustry.world.Build;
import mindustry.world.Tile;
import mindustry.world.meta.Stats;
import tantros.type.Resource;
import tantros.world.blocks.production.ProductionBlock;

public class ProduceIf extends Produce{

    Produce produceIfTrue;

    protected Boolf<ProductionBlock.ProductionBuild> condition;

    public ProduceIf(Produce produceIfTrue, Boolf<ProductionBlock.ProductionBuild> condition){
        this.produceIfTrue = produceIfTrue;
        this.condition = condition;
    }

    @Override
    public Produce ignoreLiquidFullness(boolean value) {
        return this.produceIfTrue.ignoreLiquidFullness(value);
    }

    @Override
    public Produce dumpExcess(boolean items, boolean liquids, boolean payload) {
        return this.produceIfTrue.dumpExcess(items, liquids, payload);
    }

    @Override
    public void applyToBuild(ProductionBlock block, ProductionBlock.ProductionBuild build) {
        this.produceIfTrue.applyToBuild(block, build);
    }

    @Override
    public boolean outputsItems() {
        return this.produceIfTrue.outputsItems();
    }

    @Override
    public Resource output(ProductionBlock.ProductionBuild build) {
        return this.produceIfTrue.output(build);
    }

    @Override
    public void apply(ProductionBlock block) {
        this.produceIfTrue.apply(block);
    }

    @Override
    public void build(ProductionBlock.ProductionBuild build, Table table) {
        this.produceIfTrue.build(build, table);
    }

    @Override
    public void trigger(ProductionBlock.ProductionBuild build) {
        this.produceIfTrue.trigger(build);
    }

    @Override
    public void update(ProductionBlock.ProductionBuild build) {
        this.produceIfTrue.update(build);
    }

    @Override
    public boolean canCraft(ProductionBlock.ProductionBuild build) {
        return produceIfTrue.canCraft(build);
    }

    @Override
    public float progressLimit(ProductionBlock.ProductionBuild build) {
        if(condition != null && condition.get(build)){
            return this.produceIfTrue.progressLimit(build);
        } else {
            return 0;
        }
    }

    @Override
    public void display(Stats stats, ProductionBlock block) {
        this.produceIfTrue.display(stats, block);
    }

    @Override
    public float productionTimeMultiplier(ProductionBlock.ProductionBuild build) {
        return this.produceIfTrue.productionTimeMultiplier(build);
    }

    @Override
    public void setBars(ProductionBlock block) {
        this.produceIfTrue.setBars(block);
    }

    @Override
    public boolean placementAllowed(ProductionBlock block, Tile tile, Team team, int rotation) {
        return this.produceIfTrue.placementAllowed(block, tile, team, rotation);
    }
}
