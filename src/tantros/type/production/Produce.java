package tantros.type.production;

import arc.func.Boolp;
import arc.scene.ui.layout.Table;
import mindustry.game.Team;
import mindustry.world.Tile;
import mindustry.world.meta.Stats;
import tantros.type.Resource;
import tantros.world.blocks.production.ProductionBlock;

public abstract class Produce {

    public boolean ignoreLiquidFullness;

    public boolean dumpExcessItems = false;

    public boolean dumpExcessLiquids = false;

    public boolean dumpExcessPayload = false;

    public Boolp isActive = ()->true;

    public Produce ignoreLiquidFullness(boolean value) {
        this.ignoreLiquidFullness = value;
        return this;
    }

    public Produce dumpExcess(boolean items, boolean liquids, boolean payload) {
        dumpExcessItems = items;
        dumpExcessLiquids = liquids;
        dumpExcessPayload = payload;
        return this;
    }

    public void applyToBuild(ProductionBlock block, ProductionBlock.ProductionBuild build){}

    public boolean outputsItems(){
        return false;
    }

    public abstract Resource output(ProductionBlock.ProductionBuild build);

    public abstract void apply(ProductionBlock block);

    public abstract void build(ProductionBlock.ProductionBuild build, Table table);

    public abstract void trigger(ProductionBlock.ProductionBuild build);

    public abstract void update(ProductionBlock.ProductionBuild build);

    public abstract boolean canCraft(ProductionBlock.ProductionBuild build);

    public abstract float progressLimit(ProductionBlock.ProductionBuild build);

    public abstract void display(Stats stats, ProductionBlock block);

    /** @return multiplier for efficiency - this can be above 1. Will not influence a building's base efficiency value. */
    public float productionTimeMultiplier(ProductionBlock.ProductionBuild build){
        return 1f;
    }

    public void setBars(ProductionBlock block){

    }

    public boolean placementAllowed(ProductionBlock block, Tile tile, Team team, int rotation){
        return true;
    }

}
