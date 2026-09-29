package tantros.type.effect.logic;

import arc.util.Log;
import mindustry.Vars;
import mindustry.gen.Building;
import tantros.type.blockConfig.BeamLinkConfig;
import tantros.type.buildConfig.UpdateSignal;
import tantros.type.buildingState.BuildingState;
import tantros.type.buildingState.OutputHeatState;
import tantros.type.buildingState.logic.signalling.BeamLinkState;
import tantros.type.buildingState.logic.signalling.SignalOutputState;
import tantros.type.effect.BlockEffect;
import tantros.world.blocks.BlockExtended;

import static mindustry.Vars.net;

public class SendsSignals implements BlockEffect {

    String outputStateName = "";

    BeamLinkConfig beamConfig;

    public SendsSignals(BeamLinkConfig beamConfig){
        this.beamConfig = beamConfig;
    }

    @Override
    public void apply(BlockExtended block) {
        outputStateName = block.postStateRequest(SignalOutputState::new, "SignalOutput");
        beamConfig.beamStateName = block.postStateRequest(()->new BeamLinkState(beamConfig), "SignalBeams");
    }

    @Override
    public void update(BlockExtended.BuildExtended build) {

        SignalOutputState signalOutput = build.getState(SignalOutputState.class, outputStateName);
        BeamLinkState linked = build.getState(BeamLinkState.class, beamConfig.beamStateName);
        if(signalOutput == null || linked == null) return;
        boolean needsUpdate = signalOutput.dirty || !linked.dirtyFlags.isEmpty();
        boolean updateAll = signalOutput.dirty;
        if(needsUpdate && (net.server() || !net.active())){
            for(int i = 0; i < linked.links.length; i++){
                Building link = linked.links[i];
                if(link != null && (updateAll || linked.dirtyFlags.get(i))) {
                    if (link instanceof BlockExtended.BuildExtended recipient) {
                        UpdateSignal signal = UpdateSignal.getEmpty(build);
                        signal.signals.addAll(signalOutput.signals);
                        recipient.configureAny(signal);
                    }
                }
                linked.dirtyFlags.set(i, false);
            }
            signalOutput.dirty = false;
        }
        if(!linked.removed.isEmpty()){
            for(Building building: linked.removed){
                if(building.isValid() && building instanceof BlockExtended.BuildExtended other){
                    other.configureAny(UpdateSignal.getEmpty(build));
                }
            }
            linked.removed.clear();
        }
    }

    @Override
    public void onRemoved(BlockExtended.BuildExtended build) {
        if(net.server() || !net.active()) {
            BeamLinkState linked = build.getState(BeamLinkState.class, beamConfig.beamStateName);
            for (Building other : linked.links) {
                if (!(other instanceof BlockExtended.BuildExtended recipient)) continue;
                if (!other.isValid()) continue;
                recipient.configureAny(UpdateSignal.getEmpty(build));
            }
            for(Building other: linked.removed){
                if (!(other instanceof BlockExtended.BuildExtended recipient)) continue;
                if (!other.isValid()) continue;
                recipient.configureAny(UpdateSignal.getEmpty(build));
            }
        }
    }
}
