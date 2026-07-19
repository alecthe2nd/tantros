package tantros.world.consumers;

import arc.Core;
import arc.math.Mathf;
import arc.scene.ui.layout.Table;
import mindustry.content.Blocks;
import mindustry.graphics.Pal;
import mindustry.ui.Bar;
import mindustry.world.meta.Stat;
import mindustry.world.meta.Stats;
import tantros.type.blockConfig.AttributeConfig;
import tantros.type.buildingState.AttributeState;
import tantros.ui.UIUtil;
import tantros.world.blocks.BlockExtended;

public class ConsumeAttributeTile extends ExtendedConsume{

    public AttributeConfig config;

    public ConsumeAttributeTile(AttributeConfig config){
        this.config = config;
    }

    @Override
    public void apply(BlockExtended block) {
        config = new AttributeConfig();
        config.attributeStateName = block.postStateRequest(()->new AttributeState(config),"AttributeState");
    }

    @Override
    public void display(Stats stats) {

        stats.add(Stat.tiles, config.attribute, config.floating, config.size * config.size * config.displayEfficiencyScale, false);
    }

    @Override
    public void displayBars(BlockExtended.BuildExtended build, Table table) {
        UIUtil.addBar(
                table,
                new Bar(
                        () -> Core.bundle.format("bar.efficiency", (int)(build.efficiencyScale() * 100)),
                        () -> Pal.lightOrange,
                        build::efficiencyScale)
        );
    }

    @Override
    public float efficiency(BlockExtended.BuildExtended build) {
        AttributeState attributeState = build.getState(AttributeState.class, config.attributeStateName);
        return (config.minEfficiency > attributeState.sum)? 0:1;
    }

    @Override
    public float efficiencyMultiplier(BlockExtended.BuildExtended build) {
        AttributeState attributeState = build.getState(AttributeState.class, config.attributeStateName);
        return attributeState.sum * config.efficiencyScale;
    }
}
