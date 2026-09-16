package tantros.type.effect.logic;

import arc.struct.Seq;
import mindustry.ctype.Content;
import mindustry.gen.Building;
import tantros.content.world.TantrosLiquids;
import tantros.type.buildConfig.UpdateSignal;
import tantros.type.buildingState.logic.signalling.SignalOutputState;
import tantros.type.effect.BlockEffect;
import tantros.world.blocks.BlockExtended;

public class SensesBlockContents implements BlockEffect {

    public String outputStateName = "";

    @Override
    public void apply(BlockExtended block) {
        outputStateName = block.postStateRequest(SignalOutputState::new, "SensesBlockContentsOutput", SignalOutputState.class, Seq.with(UpdateSignal.class));
    }

    @Override
    public void update(BlockExtended.BuildExtended build) {
    }

    @Override
    public void updateAlways(BlockExtended.BuildExtended build) {

        Building subject = build.front();

        Content content = TantrosLiquids.steam;

        SignalOutputState outputState = build.getState(SignalOutputState.class, outputStateName);
        if(outputState == null) return;

        if(subject != null && subject.sense(content) > 0){
            outputState.toggleSignal(build, true);
            build.enabled = true;
        } else {
            outputState.toggleSignal(build, false);
            build.enabled = false;
        }
    }

    @Override
    public void removeFromProximity(BlockExtended.BuildExtended build) {
        //TODO send empty UpdateSignals to connected logic chips
    }
}
