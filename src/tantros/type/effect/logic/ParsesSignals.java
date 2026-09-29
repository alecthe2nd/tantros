package tantros.type.effect.logic;

import arc.graphics.Color;
import arc.math.Mathf;
import arc.struct.Seq;
import arc.util.Log;
import mindustry.content.Fx;
import mindustry.ctype.Content;
import mindustry.logic.LAccess;
import mindustry.logic.Senseable;
import tantros.type.blockConfig.BeamLinkConfig;
import tantros.type.buildConfig.SetParseType;
import tantros.type.buildConfig.UpdateSignal;
import tantros.type.buildingState.logic.SignalParserState;
import tantros.type.buildingState.logic.signalling.Signal;
import tantros.type.buildingState.logic.signalling.SignalInputState;
import tantros.type.buildingState.logic.signalling.SignalOutputState;
import tantros.type.effect.BlockEffect;
import tantros.world.blocks.BlockExtended;

import java.util.Objects;

public class ParsesSignals implements BlockEffect {

    public String inputStateName = "";

    public String parserStateName = "";

    public SendsSignals sendsSignals;

    public int timer;

    public ParsesSignals(BeamLinkConfig config){
        sendsSignals = new SendsSignals(config);
    }

    @Override
    public void applySubEffects(BlockExtended block) {
        block.effect(sendsSignals);
    }

    @Override
    public void apply(BlockExtended block) {
        timer = block.timers++;
        inputStateName = block.postStateRequest(SignalInputState::new, "SignalParseInput", SignalInputState.class, Seq.with(UpdateSignal.class));
        parserStateName = block.postStateRequest(SignalParserState::new, "SignalParseType", SignalParserState.class, Seq.with(SetParseType.class));
    }

    @Override
    public void update(BlockExtended.BuildExtended build) {
        SignalInputState input = build.getState(SignalInputState.class, inputStateName);
        SignalOutputState output = build.getState(SignalOutputState.class, sendsSignals.outputStateName);
        SignalParserState state = build.getState(SignalParserState.class, parserStateName);
        if(input == null || output == null || state == null ) return;

        boolean timeUp = build.timer(timer, 20);
        if(input.dirty || state.dirty || timeUp) {
            Signal in = input.get();
            if (in != null) {
                Object target = in.getData();

                Object sense = state.getParseType();

                Object out = parse(build, target, sense);

                if(shouldUpdateSignal(out, state.prevValue)) {
                    if (out == null) {
                        output.clearSignals();
                    } else {
                        output.toggleSignal(build, out);
                    }
                    //Fx.freezing.create(build.x, build.y, 0, Color.white, null);
                } else {

                    //Fx.attackCommand.create(build.x, build.y, 0, Color.white, null);
                }
                state.prevValue = out;
            } else {
                output.clearSignals();
            }
            input.dirty = false;
            state.dirty = false;
        }


    }

    public Object parse(BlockExtended.BuildExtended build, Object target, Object sense){
        //note that remote units/buildings can be sensed as well
        if(target instanceof Senseable se){
            if(sense instanceof Content co){
                return se.sense(co);
            }else if(sense instanceof LAccess la){
                Object objOut = se.senseObject(la);

                if(objOut == Senseable.noSensed){
                    //numeric output
                    return se.sense(la);
                }else{
                    //object output
                    return objOut;
                }
            }
        }else{
            if(sense == LAccess.size || sense == LAccess.bufferSize){
                if(target instanceof CharSequence seq){
                    return seq.length();
                }else if(target instanceof Seq<?> seq){
                    return seq.size;
                }
            }
        }
        return null;
    }

    public boolean shouldUpdateSignal(Object value, Object prev){
        if(Objects.equals(value, prev)) return false;
        if(value instanceof Float floatValue && prev instanceof Float floatPrev && Mathf.equal(floatValue, floatPrev)) return false;
        if(value instanceof Double doubleValue && prev instanceof Double doublePrev && Math.abs(doubleValue - doublePrev) <= 0.00001) return false;
        return true;
    }
}
