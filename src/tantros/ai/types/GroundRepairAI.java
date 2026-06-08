package tantros.ai.types;

import arc.util.Nullable;
import arc.util.Time;
import mindustry.ai.UnitCommand;
import mindustry.ai.UnitStance;
import mindustry.ai.types.CommandAI;
import mindustry.entities.Units;
import mindustry.gen.Building;
import mindustry.ai.types.RepairAI;
import mindustry.gen.Teamc;
import mindustry.world.blocks.ConstructBlock.*;
import tantros.ai.TantrosUnitStances;

public class GroundRepairAI extends BaseGroundAI {

    public static float
            retreatDst = 160f,
            fleeRange = 310f,
            retreatDelay = Time.toSeconds * 3f;

    @Nullable
    Teamc avoid;
    float retreatTimer;
    Building damagedTarget;

    @Override
    public void updateUnit() {
        if(unit.controller() instanceof CommandAI ai && ai.hasStance(TantrosUnitStances.repairSentry)){
            ai.defaultBehavior();
        }else{
            super.updateUnit();
        }
    }

    @Override
    public void updateMovement(){
        if(target instanceof Building){
            boolean shoot = false;

            if(target.within(unit, unit.type.range)){
                unit.aim(target);
                shoot = true;
            }

            unit.controlWeapons(shoot);
        }else if(target == null){
            unit.controlWeapons(false);
        }

        boolean hold = hasStance(UnitStance.holdPosition);

        if(target != null && target instanceof Building b && b.team == unit.team){
            if(!hold){
                if(unit.type.circleTarget){
                    circleAttack(unit.type.circleTargetRadius);
                }else if(!target.within(unit, unit.type.range * 0.65f)){
                    moveTo(target, unit.type.range * 0.65f);
                }
            }

            if(!unit.type.circleTarget){
                unit.lookAt(target);
            }
        }

        //not repairing
        if(!(target instanceof Building) && !hold){
            if(timer.get(timerTarget4, 40)){
                avoid = target(unit.x, unit.y, fleeRange, true, true);
            }

            if((retreatTimer += Time.delta) >= retreatDelay){
                //retreat away from enemy when not doing anything
                if(avoid != null){
                    var core = unit.closestCore();
                    if(core != null && !unit.within(core, retreatDst)){
                        pathTo(core.tileX(), core.tileY());
                    }
                }
            }
        }else{
            retreatTimer = 0f;
        }
    }

    @Override
    public void updateTargeting(){
        if(timer.get(timerTarget, 15)){
            damagedTarget = Units.findDamagedTile(unit.team, unit.x, unit.y);
            if(damagedTarget instanceof ConstructBuild) damagedTarget = null;
        }

        if(damagedTarget == null){
            this.target = null;
            super.updateTargeting();
        }else{
            this.target = damagedTarget;
        }
    }
}
