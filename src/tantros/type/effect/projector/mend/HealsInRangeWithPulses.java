package tantros.type.effect.projector.mend;

import arc.math.Mathf;
import arc.util.Log;
import mindustry.content.Fx;
import tantros.type.buildConfig.ProgressTimerConfig;
import tantros.type.buildingState.ProgressTimerState;
import tantros.type.effect.BlockEffect;
import tantros.type.effect.projector.range.RangeConfig;
import tantros.type.effect.projector.range.RangeState;
import tantros.world.blocks.BlockExtended;

import static mindustry.Vars.indexer;

public class HealsInRangeWithPulses implements BlockEffect {

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
    public void update(BlockExtended.BuildExtended build) {

        RangeState rangeState = build.getState(RangeState.class,rangeName);
        ProgressTimerState progressState = build.getState(ProgressTimerState.class,progressName);
        if(rangeState == null || progressState == null) return;

        if(build.efficiency > 0){
            if(!build.isHealSuppressed() && progressState.progress > 1 /* TODO && DAMAGED TARGET TRACKING FINDS SOMETHING*/){

                any = false;

                indexer.eachBlock(build, rangeState.range(), b -> b.damaged() && !b.isHealSuppressed(), other -> {
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
