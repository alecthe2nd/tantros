package tantros.world.consumers;

import arc.scene.ui.layout.Table;
import arc.struct.ObjectMap;
import arc.struct.OrderedMap;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.Reflect;
import mindustry.type.Category;
import mindustry.world.consumers.Consume;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatCat;
import mindustry.world.meta.StatValue;
import mindustry.world.meta.Stats;
import tantros.world.blocks.BlockExtended;
import tantros.world.meta.TantrosStats;

public class ConsumeBoostWrapper extends ExtendedConsume{

    public Consume wrapped;

    public float efficiency;

    public ConsumeBoostWrapper(Consume wrapped, float efficiency){
        this.wrapped = wrapped;
        this.optional = true;
        this.booster = true;
        this.efficiency = efficiency;
    }

    @Override
    public float efficiencyMultiplier(BlockExtended.BuildExtended build) {
        build.inOptionalSection = true;
        float out = wrapped.efficiency(build) * efficiency;
        build.inOptionalSection = false;
        out = Math.max(out, 1f);
        return out;
    }

    @Override
    public float efficiency(BlockExtended.BuildExtended build) {
        build.inOptionalSection = true;
        float out = wrapped.efficiency(build);
        build.inOptionalSection = false;
        return out;
    }

    @Override
    public void update(BlockExtended.BuildExtended build) {
        build.inOptionalSection = true;
        wrapped.update(build);
        build.inOptionalSection = false;
    }

    @Override
    public void trigger(BlockExtended.BuildExtended build) {
        build.inOptionalSection = true;
        wrapped.trigger(build);
        build.inOptionalSection = false;
    }

    @Override
    public void build(BlockExtended.BuildExtended build, Table table) {
        wrapped.build(build, table);
    }

    @Override
    public void apply(BlockExtended block) {
        wrapped.optional(true, true);
        wrapped.apply(block);
    }

    @Override
    public void displayBars(BlockExtended.BuildExtended build, Table table) {
        if(wrapped instanceof ExtendedConsume ext) ext.displayBars(build, table);
    }

    @Override
    public void display(Stats stats) {
        Stats fauxStats = new Stats();
        wrapped.display(fauxStats);
        OrderedMap<StatCat, OrderedMap<Stat, Seq<StatValue>>> fauxMap = null;
        OrderedMap<Stat, Seq<StatValue>> innerMap = null;
        Seq<StatValue> values = null;
        try{
            fauxMap = Reflect.get(fauxStats, "map");
        }catch(Exception e){
            Log.err(e);
        }
        if(fauxMap != null) innerMap = fauxMap.get(Stat.booster.category);
        if(innerMap != null) values = innerMap.get(Stat.booster);
        if(values != null){
            for(StatValue value : values){
                stats.add(Stat.booster, TantrosStats.withEfficiencyMultiplier(efficiency, value));
            }
            Log.info("Added boosters.");
        }
    }
}
