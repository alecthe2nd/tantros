package tantros.type.effect.projector.mend;

import arc.math.Mathf;
import arc.struct.IntFloatMap;
import arc.struct.ObjectFloatMap;
import arc.util.Log;
import arc.util.Reflect;
import mindustry.content.Fx;
import mindustry.world.blocks.defense.RegenProjector;
import tantros.TantrosVars;
import tantros.type.buildConfig.ProgressTimerConfig;
import tantros.type.buildingState.ProgressTimerState;
import tantros.type.effect.BlockEffect;
import tantros.type.effect.projector.range.RangeConfig;
import tantros.type.effect.projector.range.RangeState;
import tantros.world.blocks.BlockExtended;

import static mindustry.Vars.*;
import static mindustry.Vars.state;
import static mindustry.Vars.world;

public class HealsInRangeContinuously implements BlockEffect {

    RangeConfig rangeConfig;
    MendConfig mendConfig;

    String rangeName = "";

    public boolean any = false;

    public HealsInRangeContinuously(MendConfig mendConfig, RangeConfig rangeConfig){
        this.rangeConfig = rangeConfig;
        this.mendConfig = mendConfig;
    }

    @Override
    public void apply(BlockExtended block) {
        block.putBlockConfig(rangeConfig);
        block.putBlockConfig(mendConfig);
    }

    @Override
    public void initBuildStates(BlockExtended.BuildExtended build) {
        RangeState rangeState = new RangeState();
        rangeState.config = this.rangeConfig;
        rangeName = build.putState(rangeState, "HealContinuousRange");
    }

    @Override
    public void update(BlockExtended.BuildExtended build) {

        RangeState rangeState = build.getState(RangeState.class,rangeName);
        if(rangeState == null) return;

        if(build.efficiency > 0){
            if(!build.checkSuppression() /* TODO && DAMAGED TARGET TRACKING FINDS SOMETHING*/){

                any = false;

                indexer.eachBlock(build, rangeState.range(), b -> b.damaged() && !b.isHealSuppressed(), other -> {
                    int pos = other.pos();
                    float value = TantrosVars.healMap.mendMap.get(pos);
                    float healAmount =Math.min(
                            Math.max(
                                    value,
                                    (
                                            (mendConfig.mendType == MendConfig.MendType.ABSOLUTE)?
                                                    mendConfig.heal:
                                                    other.maxHealth() * mendConfig.heal / 100
                                    ) * build.edelta()),
                            other.block.health - other.health
                    );

                    TantrosVars.healMap.mendMap.put(pos, healAmount );
                    if(Mathf.chanceDelta(0.003f * other.block.size * other.block.size)){
                        Fx.regenParticle.at(other.x + Mathf.range(other.block.size * tilesize/2f - 1f), other.y + Mathf.range(other.block.size * tilesize/2f - 1f));
                    }
                    any = true;
                });

                if(any){
                    mendConfig.mendSound.at(build, 1f + Mathf.range(0.1f), mendConfig.mendSoundVolume);
                }
            }
        }

        TantrosVars.healMap.tryHealUpdate();
    }
}
