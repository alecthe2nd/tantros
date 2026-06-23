package tantros.type.effect.projector.mend;

import arc.math.Mathf;
import arc.scene.ui.layout.Table;
import arc.util.Tmp;
import mindustry.content.Fx;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatUnit;
import mindustry.world.meta.StatValues;
import tantros.type.buildConfig.ProgressTimerConfig;
import tantros.type.buildingState.ProgressTimerState;
import tantros.type.effect.BlockEffect;
import tantros.type.effect.StatDisplayEffect;
import tantros.type.effect.projector.range.RangeConfig;
import tantros.type.effect.projector.range.RangeState;
import tantros.world.blocks.BlockExtended;
import tantros.world.meta.TantrosStats;
import static tantros.world.meta.TantrosStats.displayStat;

import static mindustry.Vars.indexer;
import static mindustry.Vars.tilesize;

public class HealsInRangeWithPulses extends StatDisplayEffect implements BlockEffect {

    RangeConfig rangeConfig;
    ProgressTimerConfig progressConfig;
    MendConfig mendConfig;

    String progressName;
    String rangeName;

    public boolean any = false;

    public HealsInRangeWithPulses(MendConfig mendConfig, RangeConfig rangeConfig, ProgressTimerConfig progressConfig){
        this.rangeConfig = rangeConfig;
        this.progressConfig = progressConfig;
        this.mendConfig = mendConfig;
    }

    @Override
    public void apply(BlockExtended block) {
        block.putBlockConfig(rangeConfig);
        block.putBlockConfig(progressConfig);
        block.putBlockConfig(mendConfig);
    }

    @Override
    public void initBuildStates(BlockExtended.BuildExtended build) {
        RangeState rangeState = new RangeState();
        rangeState.config = this.rangeConfig;
        rangeName = build.putState(rangeState, "PulseRange");
        ProgressTimerState progressState = new ProgressTimerState();
        progressState.config = this.progressConfig;
        progressName = build.putState(progressState, "PulseProgress");
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
        displayStat(t, Stat.range, rangeConfig.maxScale, StatUnit.blocks);
    }

    @Override
    public void update(BlockExtended.BuildExtended build) {

        RangeState rangeState = build.getState(RangeState.class,rangeName);
        ProgressTimerState progressState = build.getState(ProgressTimerState.class,progressName);
        if(rangeState == null || progressState == null) return;

        if(build.efficiency > 0){
            if(!build.isHealSuppressed() && progressState.progress > 1 /* TODO && DAMAGED TARGET TRACKING FINDS SOMETHING*/){

                any = false;

                indexer.eachBlock(build.team, Tmp.r1.setCentered(build.x, build.y, rangeState.range() * tilesize), b -> b.damaged() && !b.isHealSuppressed() && rangeState.inRange(build, b), other -> {
                    other.heal(((mendConfig.mendType == MendConfig.MendType.ABSOLUTE)? mendConfig.heal: other.maxHealth() * mendConfig.heal / 100) * build.efficiency);
                    other.recentlyHealed();
                    Fx.healBlockFull.at(other.x, other.y, other.block.size, mendConfig.mendColor, other.block);
                    any = true;
                });

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
