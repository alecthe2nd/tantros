package tantros.type.effect.projector.mend;

import arc.math.Mathf;
import arc.scene.ui.layout.Table;
import mindustry.content.Fx;
import mindustry.gen.Building;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatUnit;
import mindustry.world.meta.StatValues;
import tantros.TantrosVars;
import tantros.type.blockConfig.BuildingTargetsConfig;
import tantros.type.buildingState.BuildingTargetsState;
import tantros.type.effect.BlockEffect;
import tantros.type.effect.StatDisplayEffect;
import tantros.type.effect.projector.range.RangeConfig;
import tantros.type.effect.projector.range.RangeState;
import tantros.world.blocks.BlockExtended;
import tantros.world.meta.TantrosStats;

import static mindustry.Vars.*;

public class HealsInRangeContinuously extends StatDisplayEffect implements BlockEffect {

    RangeConfig rangeConfig;
    MendConfig mendConfig;
    BuildingTargetsConfig targetsConfig;

    String rangeName = "";

    String targetsName = "";

    public boolean any = false;

    public HealsInRangeContinuously(MendConfig mendConfig, RangeConfig rangeConfig){
        super();
        this.rangeConfig = rangeConfig;
        this.mendConfig = mendConfig;
        this.targetsConfig = new BuildingTargetsConfig();
        this.targetsConfig.refreshEachTick = false;
        this.targetsConfig.refreshTime = 10;
    }

    public HealsInRangeContinuously(MendConfig mendConfig, RangeConfig rangeConfig, BuildingTargetsConfig targetsConfig){
        super();
        this.rangeConfig = rangeConfig;
        this.mendConfig = mendConfig;
        this.targetsConfig = targetsConfig;
    }

    @Override
    public void apply(BlockExtended block) {
        block.putBlockConfig(rangeConfig);
        block.putBlockConfig(mendConfig);
        rangeName = block.postStateRequest(()-> new RangeState(rangeConfig), "HealContinuousRange");
        targetsName = block.postStateRequest(()->new BuildingTargetsState(targetsConfig, rangeConfig), "HealContinuousTargets");
        rangeConfig.rangeStateName = rangeName;
    }

    @Override
    public void addStats(Table t) {
        super.addStats(t);

        if(mendConfig.mendType == MendConfig.MendType.ABSOLUTE) {
            TantrosStats.displayStat(t, Stat.repairSpeed,
                    StatValues.number(
                            (int)(mendConfig.heal * 60), StatUnit.perSecond
                    )
            );
        } else {
            TantrosStats.displayStat(t, Stat.repairTime,
                    StatValues.number(
                            (int)(1f / (mendConfig.heal / 100f) / 60f), StatUnit.seconds
                    )
            );
        }
        TantrosStats.displayStat(t, Stat.range, rangeConfig.maxScale * tilesize, StatUnit.blocks);
    }

    @Override
    public void update(BlockExtended.BuildExtended build) {

        RangeState rangeState = build.getState(RangeState.class,rangeName);
        if(rangeState == null) return;

        BuildingTargetsState targetsState = build.getState(BuildingTargetsState.class,targetsName);
        if(targetsState == null) return;

        if(build.efficiency > 0){
            if(!build.checkSuppression() && targetsState.targets.size > 0){

                any = false;

                for(int i = 0; i < targetsState.targets.size; i++){
                    Building other = targetsState.targets.get(i);
                    if(other.damaged() && !other.isHealSuppressed()) {
                        int pos = other.pos();
                        float value = TantrosVars.healMap.mendMap.get(pos);
                        float healAmount = Math.min(
                                Math.max(
                                        value,
                                        (
                                                (mendConfig.mendType == MendConfig.MendType.ABSOLUTE) ?
                                                        mendConfig.heal :
                                                        other.maxHealth() * mendConfig.heal / 100
                                        ) * build.edelta()),
                                other.block.health - other.health
                        );

                        TantrosVars.healMap.mendMap.put(pos, healAmount);
                        if (Mathf.chanceDelta(0.003f * other.block.size * other.block.size)) {
                            Fx.regenParticle.at(other.x + Mathf.range(other.block.size * tilesize / 2f - 1f), other.y + Mathf.range(other.block.size * tilesize / 2f - 1f));
                        }
                        any = true;
                    }
                }

                if(any){
                    mendConfig.mendSound.at(build, 1f + Mathf.range(0.1f), mendConfig.mendSoundVolume);
                }
            }
        }

        TantrosVars.healMap.tryHealUpdate();
    }
}
