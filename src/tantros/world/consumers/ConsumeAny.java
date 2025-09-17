package tantros.world.consumers;

import arc.scene.ui.layout.Table;
import mindustry.gen.Building;
import mindustry.world.Block;
import mindustry.world.consumers.Consume;
import mindustry.world.meta.Stats;
import tantros.ui.RotatingElementStack;

import java.util.Arrays;

public class ConsumeAny extends Consume {

    public Consume[] options;

    public int chosenIndex = 0;

    public ConsumeAny(Consume... options){
        this.options = options;
    }

    public Consume getBestConsumer(Building build){
        Consume best = null;
        float bestEfficiency = 0f;
        for(Consume option: options){
            if(best == null){
                best = option;
                bestEfficiency = best.efficiency(build);
            } else{
                float nextEfficiency = option.efficiency(build);
                if(nextEfficiency > bestEfficiency){
                    best = option;
                    bestEfficiency = nextEfficiency;
                }
            }
        }
        return best;
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
        RotatingElementStack display = new RotatingElementStack();

        for(Consume option: options){
            Table childRoot = new Table((t)-> option.build(build, t));
            display.add(childRoot);
        }
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
