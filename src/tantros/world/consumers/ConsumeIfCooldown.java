package tantros.world.consumers;

import arc.func.Boolf;
import tantros.type.buildingState.CooldownState;
import tantros.world.blocks.BlockExtended;

public class ConsumeIfCooldown extends ConsumeIf{

    public ConsumeIfCooldown() {
        super(ConsumeIfCooldown::onCooldown);
    }

    @Override
    public void apply(BlockExtended block) {
    }

    public static boolean onCooldown(BlockExtended.BuildExtended build){

        CooldownState cooldownState = build.getState(CooldownState.class);
        return cooldownState == null || cooldownState.elapsed();
    }
}
