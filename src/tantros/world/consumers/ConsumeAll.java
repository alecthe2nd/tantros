package tantros.world.consumers;

import arc.math.Mathf;
import arc.scene.ui.layout.Table;
import mindustry.gen.Building;
import mindustry.world.Block;
import mindustry.world.consumers.Consume;
import mindustry.world.meta.Stats;

public class ConsumeAll extends Consume {

    public Consume[] options;

    public ConsumeAll(Consume... options){
        this.options = options;
    }

    @Override
    public void apply(Block block) {
        super.apply(block);
    }

    @Override
    public boolean ignore() {
        boolean shouldIgnore = true;
        for(Consume option: options){
            shouldIgnore &= option.ignore();
        }
        return shouldIgnore;
    }

    @Override
    public Consume update(boolean update) {
        for(Consume option: options){
            option.update(update);
        }
        return super.update(update);
    }

    @Override
    public Consume optional(boolean optional, boolean boost) {
        for(Consume option: options){
            option.optional(optional, boost);
        }
        return super.optional(optional, boost);
    }

    @Override
    public Consume boost() {
        for(Consume option: options){
            option.boost();
        }
        return super.boost();
    }

    @Override
    public void build(Building build, Table root) {
        root.table((table)->{
            for(Consume option: options){
                option.build(build, table);
            }
        });
    }

    @Override
    public void trigger(Building build) {
        for(Consume option: options){
            if(!option.ignore()) {
                option.trigger(build);
            }
        }
    }

    @Override
    public void update(Building build) {
        for(Consume option: options) {
            if(!option.ignore()) {
                option.update(build);
            }
        }
    }

    @Override
    public float efficiency(Building build) {
        float efficiency = 1.0f;
        for(Consume option: options){
            if(!option.ignore()) {
                efficiency = Math.min(efficiency, option.efficiency(build));
            }
        }
        return efficiency;
    }

    @Override
    public float efficiencyMultiplier(Building build) {
        float efficiency = 1.0f;
        for(Consume option: options){
            if(!option.ignore()) {
                efficiency = Math.min(efficiency, option.efficiencyMultiplier(build));
            }
        }
        return efficiency;
    }

    @Override
    public void display(Stats stats) {
        for(Consume option: options){
            option.display(stats);
        }
    }
}
