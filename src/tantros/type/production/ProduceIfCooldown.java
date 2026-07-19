package tantros.type.production;

import tantros.type.buildingState.CooldownState;
import tantros.world.blocks.production.ProductionBlock;

public class ProduceIfCooldown extends ProduceIf{

    public float cooldownDuration = 10;

    public String cooldownStateName;

    public ProduceIfCooldown(Produce produceIfTrue, float cooldownDuration) {
        super(produceIfTrue, null);
        condition = this::condition;
        this.cooldownDuration = cooldownDuration;
    }

    @Override
    public void apply(ProductionBlock block) {
        super.apply(block);
        cooldownStateName = block.postStateRequest(()->new CooldownState(cooldownDuration), "ProductionCooldown");
    }

    public boolean condition(ProductionBlock.ProductionBuild build){
        CooldownState cooldownState = build.getState(CooldownState.class, cooldownStateName);
        if(cooldownState == null) return false;
        return cooldownState.cooldown >= 1f;
    }
}
