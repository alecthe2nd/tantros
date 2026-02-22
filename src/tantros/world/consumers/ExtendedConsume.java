package tantros.world.consumers;

import arc.scene.ui.layout.Table;
import mindustry.gen.Building;
import mindustry.world.Block;
import mindustry.world.consumers.Consume;
import tantros.world.blocks.BlockExtended;

public class ExtendedConsume extends Consume {
    @Override
    public float efficiencyMultiplier(Building build) {
        if(build instanceof BlockExtended.BuildExtended ext) {
            return this.efficiencyMultiplier(ext);
        } else {
            throw new ClassCastException("Cannot run method 'efficiencyMultiplier' in extended consumer class '" + this.getClass().getTypeName() +"' because the passed in building is not an instance of BuildExtended.");
        }
    }

    @Override
    public float efficiency(Building build) {
        if(build instanceof BlockExtended.BuildExtended ext) {
            return this.efficiency(ext);
        } else {
            throw new ClassCastException("Cannot run method 'efficiency' in extended consumer class '" + this.getClass().getTypeName() +"' because the passed in building is not an instance of BuildExtended.");
        }
    }

    @Override
    public void update(Building build) {
        if(build instanceof BlockExtended.BuildExtended ext) {
            this.update(ext);
        } else {
            throw new ClassCastException("Cannot run method 'update' in extended consumer class '" + this.getClass().getTypeName() +"' because the passed in building is not an instance of BuildExtended.");
        }
    }

    @Override
    public void trigger(Building build) {
        if(build instanceof BlockExtended.BuildExtended ext) {
            this.trigger(ext);
        } else {
            throw new ClassCastException("Cannot run method 'trigger' in extended consumer class '" + this.getClass().getTypeName() +"' because the passed in building is not an instance of BuildExtended.");
        }
    }

    @Override
    public void build(Building build, Table table) {
        if(build instanceof BlockExtended.BuildExtended ext) {
            this.build(ext, table);
        } else {
            throw new ClassCastException("Cannot run method 'build' in extended consumer class '" + this.getClass().getTypeName() +"' because the passed in building is not an instance of BuildExtended.");
        }
    }

    @Override
    public void apply(Block block) {
        if(block instanceof BlockExtended ext) {
            this.apply(ext);
        } else {
            throw new ClassCastException("Cannot run method 'apply' in extended consumer class '" + this.getClass().getTypeName() +"' because the passed in building is not an instance of BlockExtended.");
        }
    }

    public float efficiencyMultiplier(BlockExtended.BuildExtended build) {
        return super.efficiencyMultiplier(build);
    }


    public float efficiency(BlockExtended.BuildExtended build) {
        return super.efficiency(build);
    }

    public void update(BlockExtended.BuildExtended build) {
        super.update(build);
    }

    public void trigger(BlockExtended.BuildExtended build) {
        super.trigger(build);
    }

    public void build(BlockExtended.BuildExtended build, Table table) {
        super.build(build, table);
    }

    public void apply(BlockExtended block) {
        super.apply(block);
    }

    public void displayBars(BlockExtended.BuildExtended build, Table table){

    }
}
