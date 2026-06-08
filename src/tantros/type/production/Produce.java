package tantros.type.production;

import arc.func.Boolp;
import arc.scene.ui.layout.Table;
import mindustry.game.Team;
import mindustry.world.Tile;
import mindustry.world.meta.Stats;
import tantros.type.Resource;
import tantros.type.buildingState.BuildingState;
import tantros.world.blocks.production.ProductionBlock;

public abstract class Produce {

    private static final Resource outputCache = new Resource();

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

    public Resource output(ProductionBlock.ProductionBuild build){
        return outputCache;
    }

    public abstract void apply(ProductionBlock block);

    public void build(ProductionBlock.ProductionBuild build, Table table){};

    public abstract void trigger(ProductionBlock.ProductionBuild build);

    public abstract void update(ProductionBlock.ProductionBuild build);

    public void updateAlways(ProductionBlock.ProductionBuild build){}

    public abstract boolean canCraft(ProductionBlock.ProductionBuild build);

    public float progressLimit(ProductionBlock.ProductionBuild build) {
        return 1;
    }

    public abstract void display(Stats stats, ProductionBlock block);

    /** @return multiplier for efficiency - this can be above 1. Will not influence a building's base efficiency value. */
    public float productionTimeMultiplier(ProductionBlock.ProductionBuild build){
        return 1f;
    }

    /** Sets the static bars of the owner block. (Static bars are not added or removed dynamically for some reason.)*/
    public void setBars(ProductionBlock block){

    }

    /** Sets the dynamic bars of the owner building. (Dynamic bars are added and removed dynamically, for when the interface needs to be able to adapt to state.)*/
    public void displayBars(ProductionBlock.ProductionBuild owner, Table table) {

    }

    public boolean placementAllowed(ProductionBlock block, Tile tile, Team team, int rotation){
        return true;
    }

}
