package tantros.type.effect.logic;

import arc.graphics.Color;
import arc.struct.Seq;
import arc.util.Log;
import mindustry.content.Fx;
import tantros.type.blockConfig.BeamLinkConfig;
import tantros.type.blockConfig.OperationConfig;
import tantros.type.buildConfig.SetOperation;
import tantros.type.buildConfig.UpdateSignal;
import tantros.type.buildingState.OperationSettingState;
import tantros.type.buildingState.logic.SignalParserState;
import tantros.type.buildingState.logic.signalling.Signal;
import tantros.type.buildingState.logic.signalling.SignalInputState;
import tantros.type.buildingState.logic.signalling.SignalOutputState;
import tantros.type.effect.BlockEffect;
import tantros.world.blocks.BlockExtended;

public class OperatesOnSignals implements BlockEffect {

    public String inputStateName = "";

    public SendsSignals sendsSignals;

    public OperationConfig ops;

    public static Seq<Object> params = new Seq<>();

    public OperatesOnSignals(BeamLinkConfig links, OperationConfig ops) {
        this.ops = ops;
        this.sendsSignals = new SendsSignals(links);
    }

    @Override
    public void applySubEffects(BlockExtended block) {
        block.effect(sendsSignals);
    }

    @Override
    public void apply(BlockExtended block) {
        //timer = block.timers++;
        inputStateName = block.postStateRequest(SignalInputState::new, "OperationInput", SignalInputState.class, Seq.with(UpdateSignal.class));
        ops.operationStateName = block.postStateRequest(()->new OperationSettingState(ops), "OperationSetting", OperationSettingState.class, Seq.with(SetOperation.class));
    }

    @Override
    public void update(BlockExtended.BuildExtended build) {
        SignalInputState input = build.getState(SignalInputState.class, inputStateName);
        SignalOutputState outputState = build.getState(SignalOutputState.class, sendsSignals.outputStateName);
        OperationSettingState state = build.getState(OperationSettingState.class, ops.operationStateName);
        if(input == null || outputState == null || state == null ) return;

        if(input.dirty || state.dirty){
            params.clear();
            boolean failed = false;
            for(int i = 0; i < state.currentOperation.params; i++){
                Signal param = (state.currentOperation.params <= 1 || i >= state.paramPriority.length)? input.get(i): input.get(build, state.paramPriority[i]);
                //Log.info(i + " | " + param);
                if(param == null){
                    failed = true;
                    break;
                }else{
                    params.add(param.getData());
                }
            }

            if(failed){
                outputState.clearSignals();
                //Fx.freezing.create(build.x, build.y, 0, Color.white, null);
            } else{
                Object prev = (outputState.signals.isEmpty()? null: outputState.signals.first());
                Object output = null;
                //Log.info(params);
                if(state.currentOperation.acceptsParams(params)){
                    output = state.currentOperation.operate(params);
                }
                if(output == null){
                    outputState.clearSignals();
                    //Fx.despawn.create(build.x, build.y, 0, Color.white, null);
                } else if(!output.equals(prev)) {
                    outputState.toggleSignal(build, output);
                    //Fx.attackCommand.create(build.x, build.y, 0, Color.white, null);
                }
            }
            input.dirty = false;
            state.dirty = false;
        }
    }
}
