package tantros.ai.types;

import mindustry.ai.*;
import mindustry.ai.types.CommandAI;
import mindustry.entities.units.*;
import mindustry.gen.Bullet;
import tantros.gen.BurrowerUnit;
import tantros.gen.Burrowerc;

import static tantros.type.units.BurrowerUnitType.canDislodge;

//not meant to be used outside RTS-AI-controlled units
public class BurrowAI extends AIController{

    public int unburrowCooldown = 60;

    @Override
    public void updateUnit(){

        boolean shouldBurrow = true;

        if(unit.controller() instanceof CommandAI ai){
            ai.defaultBehavior();
            //auto land when near target
            if(ai.attackTarget != null && unit.within(ai.attackTarget, unit.range())){
                shouldBurrow = false;
                unit.command().command(UnitCommand.moveCommand);
            }
        }
        if(unit instanceof Burrowerc burrower){
            burrower.burrowed(shouldBurrow);
        }
    }

    @Override
    public boolean shouldShoot() {
        return !(unit instanceof Burrowerc burrower && burrower.burrowed());
    }


    public static void hit(BurrowerUnit unit, Bullet bullet) {
        if(canDislodge(bullet)){
            unit.triggerCooldown();
        }
    }
}