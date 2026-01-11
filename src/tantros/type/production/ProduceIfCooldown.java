package tantros.type.production;

import tantros.type.buildingState.CooldownState;
import tantros.world.blocks.production.ProductionBlock;

public class ProduceIfCooldown extends ProduceIf{

    public float cooldownDuration = 10;

    public ProduceIfCooldown(Produce produceIfTrue, float cooldownDuration) {
        super(produceIfTrue, null);
        condition = this::condition;
        this.cooldownDuration = cooldownDuration;
    }

    public boolean condition(ProductionBlock.ProductionBuild build){
        CooldownState cooldownState = build.getState(CooldownState.class);
        return cooldownState.cooldown >= 1f;
    }

    @Override
    public void applyToBuild(ProductionBlock block, ProductionBlock.ProductionBuild build) {
        super.applyToBuild(block, build);
        build.putState(new CooldownState(cooldownDuration));
    }
}
