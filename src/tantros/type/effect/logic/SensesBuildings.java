package tantros.type.effect.logic;

import arc.graphics.Color;
import mindustry.content.Fx;
import mindustry.gen.Building;
import tantros.type.blockConfig.BeamLinkConfig;
import tantros.type.buildingState.logic.signalling.SignalOutputState;
import tantros.type.effect.BlockEffect;
import tantros.world.blocks.BlockExtended;

public class SensesBuildings implements BlockEffect {

    public BeamLinkConfig beamLinkConfig;

    public SendsSignals signalSender;

    public SensesBuildings(BeamLinkConfig beamLinkConfig){
        this.beamLinkConfig = beamLinkConfig;
        this.signalSender = new SendsSignals(beamLinkConfig);
    }

    @Override
    public void applySubEffects(BlockExtended block) {
        block.effect(this.signalSender);
    }

    @Override
    public void apply(BlockExtended block) {

    }

    @Override
    public void update(BlockExtended.BuildExtended build) {

    }

    @Override
    public void updateAlways(BlockExtended.BuildExtended build) {

        Building subject = build.front();

        SignalOutputState outputState = build.getState(SignalOutputState.class, signalSender.outputStateName);
        if(outputState == null) return;

        if(subject != null){
            if(outputState.signals.isEmpty() || outputState.signals.find((s)->s.getData() == subject) == null) {
                outputState.toggleSignal(build, subject);
                //Fx.attackCommand.create(build.x, build.y, 0, Color.white, null);
            }
        } else {
            outputState.clearSignals();
            //Fx.freezing.create(build.x, build.y, 0, Color.white, null);
        }
    }
}
