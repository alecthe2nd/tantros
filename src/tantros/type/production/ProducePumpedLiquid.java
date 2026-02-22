package tantros.type.production;

import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import mindustry.Vars;
import mindustry.game.Team;
import mindustry.type.LiquidStack;
import mindustry.world.Tile;
import mindustry.world.blocks.production.Pump;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatUnit;
import mindustry.world.meta.Stats;
import tantros.TantrosVars;
import tantros.type.Resource;
import tantros.type.blockConfig.PumpConfig;
import tantros.type.buildingState.PumpState;
import tantros.world.blocks.effect.GroundPenetratingRadar;
import tantros.world.blocks.environment.DeepOreBlock;
import tantros.world.blocks.production.ProductionBlock;

public class ProducePumpedLiquid extends Produce{
    public PumpConfig pumpConfig;

    /** Liquid amount pumped per tick at full usage.*/
    public float pumpAmount = 0.2f;

    @Override
    public Resource output(ProductionBlock.ProductionBuild build) {
        PumpState pumpState = build.getState(PumpState.class);
        return pumpState.cachedOutput;
    }

    @Override
    public void apply(ProductionBlock block) {
        pumpConfig = new PumpConfig();
        block.putBlockConfig(pumpConfig);
        block.stateSources.add(PumpState::new);
        block.hasLiquids = true;
        block.outputsLiquid = true;
    }

    @Override
    public void trigger(ProductionBlock.ProductionBuild build) {

    }

    @Override
    public void update(ProductionBlock.ProductionBuild build) {
        PumpState pumpState = build.getState(PumpState.class);
        if(!isActive.get()) return;
        //continuously output based on efficiency
        float inc = build.getProgressIncrease(1f);

        build.handleLiquid(build, pumpState.liquidDrop, pumpState.amount * ((pumpAmount / build.block.size) / build.block.size) * inc);
    }

    @Override
    public boolean canCraft(ProductionBlock.ProductionBuild build) {
        PumpState pumpState = build.getState(PumpState.class);
        if(!ignoreLiquidFullness){
            if(build.liquids.get(pumpState.liquidDrop) >= build.block.liquidCapacity - 0.001f){
                if(!dumpExcessLiquids){
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public float progressLimit(ProductionBlock.ProductionBuild build) {
        if(ignoreLiquidFullness){
            return 1f;
        }
        PumpState pumpState = build.getState(PumpState.class);

        //limit progress increase by maximum amount of liquid it can produce
        float scaling = 1f;
        float value = (build.block.liquidCapacity - build.liquids.get(pumpState.liquidDrop)) / (pumpState.amount * build.edelta());
        scaling = Math.min(scaling, value);

        //when dumping excess take the maximum value instead of the minimum.
        return (dumpExcessLiquids ? 1 : scaling);
    }

    @Override
    public void display(Stats stats, ProductionBlock block) {
        stats.add(Stat.output, 60f * pumpAmount, StatUnit.liquidSecond);
    }

    @Override
    public boolean placementAllowed(ProductionBlock block, Tile tile, Team team, int rotation) {
        if(block.isMultiblock()){
            for(Tile other : tile.getLinkedTilesAs(block, PumpConfig.tempTiles)){
                if(this.canPump(other)) {
                    return true;
                }
            }
            return false;
        }else{
            return this.canPump(tile);
        }
    }

    protected boolean canPump(Tile tile){
        return tile != null && tile.floor().liquidDrop != null;
    }
}
