package tantros.type.effect.logic;

import arc.Core;
import arc.struct.IntMap;
import arc.struct.ObjectSet;
import arc.struct.Seq;
import arc.util.Log;
import tantros.type.buildConfig.UpdateSignal;
import tantros.type.buildingState.logic.MessageState;
import tantros.type.buildingState.logic.signalling.Signal;
import tantros.type.buildingState.logic.signalling.SignalInputState;
import tantros.type.effect.BlockEffect;
import tantros.world.blocks.BlockExtended;

import static mindustry.Vars.net;

public class DisplaysSignals implements BlockEffect {

    public String messageStateName = "";

    public String signalInputName = "";

    @Override
    public void apply(BlockExtended block) {
        messageStateName = block.postStateRequest(MessageState::new, "SignalMessage", MessageState.class, Seq.with(
                String.class
        ));
        signalInputName = block.postStateRequest(SignalInputState::new, "SignalInput", SignalInputState.class, Seq.with(
                UpdateSignal.class
        ));
    }

    @Override
    public void update(BlockExtended.BuildExtended build) {

    }

    @Override
    public void updateAlways(BlockExtended.BuildExtended build) {

        SignalInputState input = build.getState(SignalInputState.class, signalInputName);
        if(input == null) return;

        if(input.dirty){
            Signal signal = input.get();
            if(net.server() || !net.active()) {
                if (signal != null) {
                    build.configureAny(Core.bundle.format(
                                    "block-ability.displays-signals.signal",
                                    String.valueOf(signal.getSource()),
                                    String.valueOf(signal.getData())
                            )
                    );
                } else {
                    build.configureAny("");
                }
            }

            input.dirty = false;
        }

    }
}
