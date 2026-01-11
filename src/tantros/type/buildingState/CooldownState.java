package tantros.type.buildingState;

import arc.math.Mathf;
import tantros.world.blocks.production.ProductionBlock;

public class CooldownState implements BuildingState{

    public float cooldownDuration = 5;
    public float cooldown = 0;

    public CooldownState(){

    }

    public CooldownState(float duration){
        this.cooldownDuration = duration;
    }

    @Override
    public void initState(ProductionBlock ownerType, ProductionBlock.ProductionBuild owner) {

    }

    @Override
    public void update(ProductionBlock ownerType, ProductionBlock.ProductionBuild owner) {

        if(owner.efficiency > 1.0E-7F){
            cooldown = Mathf.approachDelta(cooldown, 1f, 1/cooldownDuration);
        } else {
            cooldown = Mathf.approachDelta(cooldown, 0f, 1/cooldownDuration);
        }
    }

    @Override
    public void onProximity(ProductionBlock ownerType, ProductionBlock.ProductionBuild owner) {

    }
}
