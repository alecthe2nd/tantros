package tantros.type.production;

import arc.Core;
import arc.func.Func;
import arc.util.Strings;
import mindustry.graphics.Pal;
import mindustry.ui.Bar;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatUnit;
import mindustry.world.meta.Stats;
import tantros.type.Resource;
import tantros.world.blocks.production.ProductionBlock;

public class ProducePower extends Produce{

    public Resource outputCache = new Resource();

    public float maxPower;

    public ProducePower(float maxPower){
        this.maxPower = maxPower;
    }

    public Func<ProductionBlock.ProductionBuild, Float> powerOutput = (build)-> maxPower;

    @Override
    public Resource output(ProductionBlock.ProductionBuild build) {
        outputCache.clear();
        return outputCache.withPower(powerOutput.get(build));
    }

    @Override
    public void apply(ProductionBlock block) {
        block.hasPower = true;
        block.outputsPower = true;
    }

    @Override
    public void trigger(ProductionBlock.ProductionBuild build) {

    }

    @Override
    public void update(ProductionBlock.ProductionBuild build) {

    }

    @Override
    public void updateAlways(ProductionBlock.ProductionBuild build) {
        build.powerProductionEfficiency = build.efficiency;
    }

    @Override
    public boolean canCraft(ProductionBlock.ProductionBuild build) {
        return true;
    }

    @Override
    public void display(Stats stats, ProductionBlock block) {
        stats.add(Stat.basePowerGeneration, maxPower * 60.0f, StatUnit.powerSecond);
    }

    @Override
    public void setBars(ProductionBlock block) {
        super.setBars(block);
        block.addBar("power", (ProductionBlock.ProductionBuild entity) -> new Bar(() ->
                Core.bundle.format("bar.poweroutput",
                        Strings.fixed(entity.getPowerProduction() * 60 * entity.timeScale(), 1)),
                () -> Pal.powerBar,
                () -> entity.powerProductionEfficiency));
    }
}
