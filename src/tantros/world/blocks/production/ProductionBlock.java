package tantros.world.blocks.production;

import arc.Core;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.struct.EnumSet;
import arc.struct.Seq;
import arc.util.Eachable;
import arc.util.Time;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.content.Fx;
import mindustry.entities.Effect;
import mindustry.entities.units.BuildPlan;
import mindustry.gen.Building;
import mindustry.gen.Sounds;
import mindustry.graphics.Pal;
import mindustry.logic.LAccess;
import mindustry.type.Item;
import mindustry.type.ItemStack;
import mindustry.type.LiquidStack;
import mindustry.ui.Bar;
import mindustry.world.Block;
import mindustry.world.blocks.environment.Floor;
import mindustry.world.consumers.ConsumeLiquidBase;
import mindustry.world.draw.DrawBlock;
import mindustry.world.draw.DrawDefault;
import mindustry.world.meta.BlockFlag;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatUnit;
import mindustry.world.meta.StatValues;
import tantros.type.production.Produce;

import static mindustry.Vars.indexer;
import static mindustry.Vars.state;

public class ProductionBlock extends Block {

    public float productionTime = 1;

    public boolean outputsHeat = false;

    public boolean outputsPayload = false;

    public float warmupSpeed = 0.019f;

    public Seq<Produce> producers = new Seq<>();

    public Produce powerProduction;

    public DrawBlock drawer = new DrawDefault();

    public Effect craftEffect = Fx.none;

    public ProductionBlock(String name) {
        super(name);
        update = true;
        solid = true;
        hasItems = true;
        ambientSound = Sounds.loopMachine;
        sync = true;
        ambientSoundVolume = 0.03f;
        flags = EnumSet.of(BlockFlag.factory);
        drawArrow = false;
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
    public void drawPlanRegion(BuildPlan plan, Eachable<BuildPlan> list){
        drawer.drawPlan(this, plan, list);
    }

    @Override
    public TextureRegion[] icons(){
        return drawer.finalIcons(this);
    }

    @Override
    public void load(){
        super.load();

        drawer.load(this);
    }

    @Override
    public void getRegionsToOutline(Seq<TextureRegion> out){
        drawer.getRegionsToOutline(this, out);
    }

    public Produce produce(Produce produce){
        producers.add(produce);
        return produce;
    }

    public class ProductionBuild extends Building{

        public float progress;
        public float totalProgress;
        public float warmup;
        public float currentProductionTime = productionTime;

        public float powerProductionEfficiency = 0.0f;

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

            if(efficiency > 0){

                progress += getProgressIncrease(currentProductionTime);
                warmup = Mathf.approachDelta(warmup, 1f/*warmupTarget()*/, warmupSpeed);

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
            totalProgress += warmup * Time.delta;

            if(progress >= 1f){
                craft();
            }

            dumpOutputs();
        }

        @Override
        public float getProgressIncrease(float baseTime){

            float limit = 1f;

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
            write.f(warmup);;
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
        public void draw(){
            drawer.draw(this);
        }

        @Override
        public void drawLight(){
            super.drawLight();
            drawer.drawLight(this);
        }

    }
}
