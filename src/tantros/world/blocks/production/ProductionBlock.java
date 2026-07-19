package tantros.world.blocks.production;

import arc.math.Mathf;
import arc.scene.ui.layout.Table;
import arc.struct.EnumSet;
import arc.struct.Seq;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.content.Fx;
import mindustry.entities.Effect;
import mindustry.game.Team;
import mindustry.gen.Sounds;
import mindustry.logic.LAccess;
import mindustry.type.Item;
import mindustry.type.ItemStack;
import mindustry.type.LiquidStack;
import mindustry.world.Tile;
import mindustry.world.blocks.heat.HeatBlock;
import mindustry.world.meta.BlockFlag;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatUnit;
import tantros.type.buildingState.BuildingState;
import tantros.type.production.Produce;
import tantros.world.blocks.BlockExtended;

public class ProductionBlock extends BlockExtended {

    public boolean rotatedOutput;

    public float productionTime = 1;

    public float warmupSpeed = 0.019f;

    /** Whether warmup is applied to total progress i.e. to visuals.*/
    public boolean warmupEnabled = false;

    /** Whether warmup is applied to production times as a limiter.*/
    public boolean warmupEffectsProduction = false;

    public Seq<Produce> producers = new Seq<>();

    public Produce powerProduction;

    public Effect craftEffect = Fx.none;

    public ProductionBlock(String name) {
        super(name);
        update = true;
        solid = true;
        sync = true;
        ambientSound = Sounds.loopMachine;
        ambientSoundVolume = 0.03f;
        flags = EnumSet.of(BlockFlag.factory);
        drawArrow = false;
        rotatedOutput = false;
    }

    @Override
    public void init() {

        super.init();

        for(Produce producer: producers){
            producer.apply(this);
        }
        if(consPower == null){
            this.consumesPower = false;
        }

    }


    @Override
    public void setStats(){
        super.setStats();

        for(Produce producer: producers){
            producer.display(stats, this);
        }

        stats.timePeriod = productionTime;
        if((hasItems && itemCapacity > 0)){
            stats.add(Stat.productionTime, productionTime / 60f, StatUnit.seconds);
        }
    }

    @Override
    public void setBars(){
        super.setBars();

        removeBar("liquid");
        for(Produce produce: producers){
            produce.setBars(this);
        }
    }

    @Override
    public boolean outputsItems(){
        return producers.contains(Produce::outputsItems);
    }

    @Override
    public boolean rotatedOutput(int x, int y){
        return rotatedOutput;
    }

    public Produce produce(Produce produce){
        producers.add(produce);
        return produce;
    }

    @Override
    public boolean canPlaceOn(Tile tile, Team team, int rotation){
        boolean allowed = super.canPlaceOn(tile, team, rotation);
        for(Produce producer : producers){
            allowed &= producer.placementAllowed(this, tile, team, rotation);
        }
        return allowed;
    }

    public class ProductionBuild extends BuildExtended implements HeatBlock {

        public float progress;
        public float totalProgress = 0;

        public float lastProgressLimit = 1f;

        public float warmup;
        public float currentProductionTime = productionTime;
        public float progressThisTick = 0;

        public float powerProductionEfficiency = 0.0f;

        @Override
        public void displayBars(Table table) {
            super.displayBars(table);
            for(Produce producer: producers){
                producer.displayBars(this, table);
            }
        }

        @Override
        public void created() {
            for(Produce producer: producers){
                producer.applyToBuild((ProductionBlock) block, this);
            }
            super.created();
        }

        @Override
        public void onProximityUpdate() {
            super.onProximityUpdate();

            for(BuildingState state : this.statesAsSerialized.values()){
                state.onProximity((ProductionBlock) this.block, this);
            }
        }

        @Override
        public boolean shouldConsume(){
            boolean should = enabled;
            if(!producers.isEmpty() && should){
                for(Produce producer: producers){
                    should &= producer.canCraft(this);
                }
            }
            return should;
        }

        public void updateProductionTime(){
            float multiplier = 1f;
            for(Produce producer: producers){
                multiplier = multiplier * producer.productionTimeMultiplier(this);
            }
            currentProductionTime = productionTime * multiplier;
        }

        @Override
        public void updateTile() {
            super.updateTile();
            updateProductionTime();

            progressThisTick = getProgressIncrease(currentProductionTime);


            if(efficiency > 0){

                warmup = Mathf.approachDelta(warmup, 1f, warmupSpeed);
                progress += progressThisTick;

                for(Produce producer: producers){
                    producer.update(this);
                }
            }else{
                warmup = Mathf.approachDelta(warmup, 0f, warmupSpeed);
            }

            for(Produce producer: producers){
                producer.updateAlways(this);
            }

            totalProgress += (warmupEnabled)? (warmup * delta()) : progressThisTick;

            if(progress >= 1f){
                craft();
            }
            dumpOutputs();
        }

        public float getProgressIncreaseLimit(){

            float limit = (warmupEffectsProduction)? warmup: 1;

            for(Produce producer: producers){
                limit = Math.min(limit, producer.progressLimit(this));
            }
            return limit;
        }

        @Override
        public float getProgressIncrease(float baseTime){
            lastProgressLimit = getProgressIncreaseLimit();
            //when dumping excess take the maximum value instead of the minimum.
            return super.getProgressIncrease(baseTime) * lastProgressLimit;

        }

        @Override
        public float warmup(){
            return warmup;
        }

        @Override
        public float totalProgress(){
            return totalProgress;
        }

        public void craft(){
            consume();

            produce();

            if(wasVisible){
                craftEffect.at(x, y);
            }
            progress %= 1f;
        }

        public void produce(){

            for(Produce producer : producers){
                producer.trigger(this);
            }
        }

        public void dumpOutputs(){
            if(timer(timerDump, dumpTime / timeScale)){
                for( Produce producer: producers) {
                    for (ItemStack output : producer.output(this).items) {
                        dump(output.item);
                    }
                }
            }
            for( Produce producer: producers) {
                Seq<LiquidStack> outputLiquids = producer.output(this).liquids;
                for(int i = 0; i < outputLiquids.size; i++){
                    int dir = /*liquidOutputDirections.length > i ? liquidOutputDirections[i] :*/ -1;

                    dumpLiquid(outputLiquids.get(i).liquid, 2f, dir);
                }
            }

        }

        @Override
        public double sense(LAccess sensor){
            if(sensor == LAccess.progress) return progress();
            //attempt to prevent wild total liquid fluctuation, at least for crafters

            if(sensor == LAccess.totalLiquids && producers.contains((p)->!p.output(this).liquids.isEmpty())) return liquids.get(producers.find((p)->!p.output(this).liquids.isEmpty()).output(this).liquids.first().liquid);
            return super.sense(sensor);
        }

        @Override
        public float progress(){
            return Mathf.clamp(progress);
        }

        @Override
        public float getPowerProduction(){
            if(!enabled) return 0f;
            float powerOutput = 0f;
            for(Produce produce : producers){
                powerOutput += produce.output(this).power;
            }
            return enabled ? powerOutput * powerProductionEfficiency : 0f;
        }

        @Override
        public int getMaximumAccepted(Item item){
            return itemCapacity;
        }

        @Override
        public boolean shouldAmbientSound(){
            return efficiency > 0;
        }

        @Override
        public void write(Writes write){
            super.write(write);
            write.f(progress);
            write.f(warmup);
            write.f(powerProductionEfficiency);
            //write.f(outputHeat);
        }

        @Override
        public void read(Reads read, byte revision){
            super.read(read, revision);
            progress = read.f();
            warmup = read.f();
            powerProductionEfficiency = read.f();
            //outputHeat = read.f();
        }

        public ProductionBlock getBlock(){
            return (ProductionBlock) block;
        }

        @Override
        public float heat() {
            if(!enabled) return 0f;
            float heat = 0f;
            for(Produce produce : producers){
                heat += produce.output(this).heat;
            }
            return enabled ? heat : 0f;
        }

        @Override
        public float heatFrac() {
            return enabled && heat() > 0 ? Mathf.clamp(efficiency) : 0f;
        }
    }
}
