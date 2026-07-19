package tantros.type.effect.logic;

import arc.util.Log;
import mindustry.ai.types.CommandAI;
import mindustry.gen.Unit;
import tantros.type.blockConfig.ConfigApplier;
import tantros.type.blockInput.UnitCommandInput;
import tantros.type.buildConfig.AddUnitConfig;
import tantros.type.buildConfig.ClearQueueConfig;
import tantros.type.buildConfig.ClearUnitsConfig;
import tantros.type.buildingState.logic.UnitCommandQueueState;
import tantros.type.buildingState.logic.UnitLinks;
import tantros.type.effect.BlockEffect;
import tantros.world.blocks.BlockExtended;

import static mindustry.Vars.net;

public class UnitCommandAssigner implements BlockEffect {

    public int updateTimer;

    public float checkInterval = 20f;

    @Override
    public void update(BlockExtended.BuildExtended build) {
        UnitLinks links = build.getState(UnitLinks.class);
        UnitCommandQueueState queue = build.getState(UnitCommandQueueState.class);
        if(build.timer(updateTimer, checkInterval) && !net.client()){

            for (Unit link : links.unitLinks) {
                Log.info("linked to "+ link);
                if(canControl(build, link)){
                    CommandAI ai = link.command();
                    ai.clearCommands();
                    queue.commandQueue.forEach(ai::commandQueue);
                }
            }
        }
    }

    @Override
    public void apply(BlockExtended block) {
        updateTimer = block.timers++;

        block.postStateRequest(UnitLinks::new);
        block.postStateRequest(UnitCommandQueueState::new);
        block.configAppliers.add(new ConfigApplier<>(UnitLinks.class, AddUnitConfig.class));
        block.configAppliers.add(new ConfigApplier<>(UnitLinks.class, ClearUnitsConfig.class));
        block.configAppliers.add(new ConfigApplier<>(UnitCommandQueueState.class, Integer.class));
        block.configAppliers.add(new ConfigApplier<>(UnitCommandQueueState.class, ClearQueueConfig.class));
        block.inputs.add(new UnitCommandInput());
    }

    public boolean canControl(BlockExtended.BuildExtended owner, Unit unit){
        return unit.isValid() && (unit.team == owner.team || owner.block.privileged) && unit.controller().isLogicControllable();
    }
}
