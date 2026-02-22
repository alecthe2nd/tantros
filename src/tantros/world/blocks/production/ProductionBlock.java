package tantros.world.blocks.production;

import arc.math.Mathf;
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
import mindustry.world.meta.BlockFlag;
import tantros.type.buildingState.BuildingState;
import tantros.type.production.Produce;
import tantros.world.blocks.BlockExtended;

public class ProductionBlock extends BlockExtended {

    public boolean rotatedOutput;

    public float productionTime = 1;

    public boolean outputsHeat = false;

    public boolean outputsPayload = false;

    public float warmupSpeed = 0.019f;

    public boolean warmupEffectsProduction = false;

    public Seq<Produce> producers = new Seq<>();

    public Produce powerProduction;

    public Effect craftEffect = Fx.none;

    public ProductionBlock(String name) {
        super(name);
        update = true;
        solid = true;
        sync = true;
        hasItems = true;
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

        /*if(liquidBoostIntensity != 1 && findConsumer(f -> f instanceof ConsumeLiquidBase && f.booster) instanceof ConsumeLiquidBase consBase){
            stats.remove(Stat.booster);
            stats.add(Stat.booster,
                    StatValues.speedBoosters("{0}" + StatUnit.timesSpeed.localized(),
                            consBase.amount,
                            liquidBoostIntensity * liquidBoostIntensity, false, consBase::consumes)
            );
        }*/
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
        boolean allowed = true;
        for(Produce producer : producers){
            allowed &= producer.placementAllowed(this, tile, team, rotation);
        }
        return allowed;
    }

    public class ProductionBuild extends BuildExtended {

        public float progress;
        public float totalProgress = 0;
        public float warmup;
        public float currentProductionTime = productionTime;
        public float progressThisTick = 0;

        public float powerProductionEfficiency = 0.0f;


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

            for(Class<? extends BuildingState> type : this.states.keys()){
                getState(type).onProximity((ProductionBlock) this.block, this);
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
            float mult = 1f;
            for(Produce producer: producers){
                mult = mult * producer.productionTimeMultiplier(this);
            }
            currentProductionTime = productionTime * mult;
        }

        @Override
        public void updateTile() {
            super.updateTile();
            updateProductionTime();

            for(Class<? extends BuildingState> type : this.states.keys()){
                getState(type).update((ProductionBlock) this.block, this);
            }

            progressThisTick = getProgressIncrease(currentProductionTime);
            if(efficiency > 0){

                warmup = Mathf.approachDelta(warmup, 1f/*warmupTarget()*/, warmupSpeed);

                progress += progressThisTick;

                for(Produce producer: producers){
                    producer.update(this);
                }

                //if(wasVisible && Mathf.chanceDelta(updateEffectChance)){
                //    updateEffect.at(x + Mathf.range(size * updateEffectSpread), y + Mathf.range(size * updateEffectSpread));
                //}
            }else{
                warmup = Mathf.approachDelta(warmup, 0f, warmupSpeed);
            }

            //TODO may look bad, revert to edelta() if so
            totalProgress += progressThisTick;

            if(progress >= 1f){
                craft();
            }
            dumpOutputs();
        }

        @Override
        public float getProgressIncrease(float baseTime){

            float limit = (warmupEffectsProduction)? warmup: 1;

            for(Produce producer: producers){
                limit = Math.min(limit, producer.progressLimit(this));
            }

            //when dumping excess take the maximum value instead of the minimum.
            return super.getProgressIncrease(baseTime) * limit;
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
            return enabled ? powerProduction.output(this).power * powerProductionEfficiency : 0f;
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
    }
}
