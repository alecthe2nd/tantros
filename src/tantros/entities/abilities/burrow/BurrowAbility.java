package tantros.entities.abilities.burrow;

import arc.util.Log;
import arc.util.Time;
import mindustry.Vars;
import mindustry.ai.types.CommandAI;
import mindustry.ai.types.GroundAI;
import mindustry.entities.Units;
import mindustry.entities.abilities.Ability;
import mindustry.entities.units.AIController;
import mindustry.entities.units.UnitController;
import mindustry.game.Team;
import mindustry.gen.Player;
import mindustry.gen.Teamc;
import mindustry.gen.Unit;
import mindustry.logic.Ranged;
import tantros.gen.Burrowerc;

import java.lang.reflect.Field;

public class BurrowAbility extends Ability {

    public float checkTimer = 0;
    public static float updateTime = 5;
    public Teamc lastTarget = null;

    @Override
    public void created(Unit unit) {
        super.created(unit);
    }

    @Override
    public void update(Unit unit) {
        super.update(unit);
        UnitController controller = unit.controller();
        if(!(controller instanceof Player) && !(controller instanceof CommandAI) && checkTimer > updateTime){
            Teamc enemy = null;
            try{
                if(controller instanceof AIController aiController){
                    Field field = AIController.class.getDeclaredField("target");
                    field.setAccessible(true);
                    enemy = (Teamc) field.get(aiController);
                }
            } catch(Exception e){
                Log.err(e);
            }
            lastTarget = enemy;
            checkTimer = 0;
        } else if (checkTimer < updateTime){
            checkTimer += Time.delta;
        }
        if(unit instanceof Burrowerc burrowerc){
            burrowerc.burrowed(lastTarget == null);
        }
    }
}
