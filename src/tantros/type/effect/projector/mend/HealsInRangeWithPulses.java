package tantros.type.effect.projector.mend;

import arc.math.Mathf;
import arc.scene.ui.layout.Table;
import arc.util.Time;
import arc.util.Tmp;
import mindustry.content.Fx;
import mindustry.gen.Building;
import mindustry.world.Block;
import mindustry.world.consumers.Consume;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatUnit;
import mindustry.world.meta.StatValues;
import tantros.type.blockConfig.BuildingTargetsConfig;
import tantros.type.blockConfig.ProgressTimerConfig;
import tantros.type.buildingState.BuildingTargetsState;
import tantros.type.buildingState.ProgressTimerState;
import tantros.type.effect.BlockEffect;
import tantros.type.effect.StatDisplayEffect;
import tantros.type.effect.projector.range.RangeConfig;
import tantros.type.effect.projector.range.RangeState;
import tantros.world.blocks.BlockExtended;
import tantros.world.consumers.ExtendedConsume;

import static tantros.world.meta.TantrosStats.displayStat;

import static mindustry.Vars.indexer;
import static mindustry.Vars.tilesize;

public class HealsInRangeWithPulses extends StatDisplayEffect implements BlockEffect {

    RangeConfig rangeConfig;
    ProgressTimerConfig progressConfig;
    MendConfig mendConfig;
    BuildingTargetsConfig targetsConfig;

    String progressName = "";
    String rangeName = "";
    String targetsName = "";

    public boolean any = false;

    public HealsInRangeWithPulses(MendConfig mendConfig, RangeConfig rangeConfig, ProgressTimerConfig progressConfig){
        this.rangeConfig = rangeConfig;
        this.progressConfig = progressConfig;
        this.mendConfig = mendConfig;
        this.targetsConfig = new BuildingTargetsConfig();
        this.targetsConfig.refreshEachTick = false;
        this.targetsConfig.refreshTime = 10;
    }

    public HealsInRangeWithPulses(RangeConfig rangeConfig, BuildingTargetsConfig targetsConfig, MendConfig mendConfig, ProgressTimerConfig progressConfig) {
        this.rangeConfig = rangeConfig;
        this.targetsConfig = targetsConfig;
        this.mendConfig = mendConfig;
        this.progressConfig = progressConfig;
    }

    @Override
    public void apply(BlockExtended block) {
        block.putBlockConfig(rangeConfig);
        block.putBlockConfig(progressConfig);
        block.putBlockConfig(mendConfig);
        rangeName = block.postStateRequest(()-> new RangeState(this.rangeConfig), "PulseRange");
        progressName = block.postStateRequest(()-> new ProgressTimerState(this.progressConfig), "PulseProgress");
        targetsName = block.postStateRequest(()->new BuildingTargetsState(targetsConfig, rangeConfig), "HealPulseTargets");
        rangeConfig.rangeStateName = rangeName;
    }

    @Override
    public void addStats(Table t) {
        super.addStats(t);
        if(mendConfig.mendType == MendConfig.MendType.ABSOLUTE) {
            displayStat(t, Stat.healing, mendConfig.heal, StatUnit.none);
        }else{
            displayStat(t, Stat.healing, mendConfig.heal, StatUnit.percent);
        }
        displayStat(t, Stat.productionTime, progressConfig.progressTime / 60f, StatUnit.seconds);
        if(mendConfig.mendType == MendConfig.MendType.ABSOLUTE) {
            displayStat(t, Stat.repairSpeed,
                    StatValues.number(
                            (int)(mendConfig.heal / (progressConfig.progressTime / 60)), StatUnit.perSecond
                    )
            );
        } else {
            displayStat(t, Stat.repairTime,
                    StatValues.number(
                            (int)((100f / mendConfig.heal) * progressConfig.progressTime / 60f), StatUnit.seconds
                    )
            );
        }
        displayStat(t, Stat.range, rangeConfig.maxScale * tilesize, StatUnit.blocks);
    }

    @Override
    public void update(BlockExtended.BuildExtended build) {

        if(build.efficiency > 0){
            RangeState rangeState = build.getState(RangeState.class,rangeName);
            ProgressTimerState progressState = build.getState(ProgressTimerState.class,progressName);
            if(rangeState == null || progressState == null) return;
            BuildingTargetsState targetsState = build.getState(BuildingTargetsState.class,targetsName);
            if(targetsState == null) return;
            if(!build.isHealSuppressed() && progressState.progress > 1 && targetsState.targets.contains(Building::damaged)){

                any = false;

                for(int i = 0; i < targetsState.targets.size; i++){
                    Building target = targetsState.targets.get(i);
                    if(target.damaged() && !target.isHealSuppressed()) {
                        target.heal(((mendConfig.mendType == MendConfig.MendType.ABSOLUTE) ? mendConfig.heal : target.maxHealth() * mendConfig.heal / 100) * build.efficiency);
                        target.recentlyHealed();
                        Fx.healBlockFull.at(target.x, target.y, target.block.size, mendConfig.mendColor, target.block);
                        any = true;
                    }
                }

                if(any){
                    mendConfig.mendSound.at(build, 1f + Mathf.range(0.1f), mendConfig.mendSoundVolume);
                    progressState.progress = 0;
                }
            }
        }
    }

    public HealsInRangeWithPulses asProgressSource(BlockExtended block){
        block.warmupSource = (build)->{
            ProgressTimerState progressState = build.getState(ProgressTimerState.class,this.progressName);
            if(progressState == null) return 0.0f;
            return progressState.warmup;
        };

        block.progressSource = (build)->{
            ProgressTimerState progressState = build.getState(ProgressTimerState.class,this.progressName);
            if(progressState == null) return 0.0f;
            return progressState.progress;
        };

        block.totalProgressSource = (build)->{
            ProgressTimerState progressState = build.getState(ProgressTimerState.class,this.progressName);
            if(progressState == null) return 0.0f;
            return progressState.totalProgress;
        };
        return this;
    }
}
