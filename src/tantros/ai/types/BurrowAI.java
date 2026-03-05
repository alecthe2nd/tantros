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
        }
        if(unit instanceof Burrowerc burrower) {
            burrower.burrowed(true);
        }
    }


    public static void hit(BurrowerUnit unit, Bullet bullet) {
        if(canDislodge(bullet)){
            unit.triggerCooldown();
        }
    }
}