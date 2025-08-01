package tantros.type.units;

import static mindustry.Vars.*;

import arc.math.Mathf;
import mindustry.entities.units.AIController;
import mindustry.gen.Player;
import mindustry.gen.Unit;
import mindustry.type.UnitType;
import tantros.ai.types.BurrowAI;
import tantros.content.world.TantrosUnitTypes;
import tantros.gen.Burrowerc;

public class BurrowerUnitType extends UnitType {
    public BurrowerUnitType(String name) {
        super(name);
    }

    @Override
    public void init() {
        super.init();
        commands.add(TantrosUnitTypes.burrowCommand);
    }

    @Override
    public void update(Unit unit) {
        if(unit instanceof Burrowerc burrower){

            if(unit.controller() instanceof Player player && burrower.burrowCooldown() <= 0) {
                burrower.burrowed(player.boosting());
            }
            if(!net.client() && unit.controller() instanceof AIController && !(unit.controller() instanceof BurrowAI)){
                burrower.burrowed(false);
            }
            burrower.burrowCooldown(Math.max(burrower.burrowCooldown() - 1, 0));
        }
        super.update(unit);
    }

    @Override
    public void draw(Unit unit){
        if(unit instanceof Burrowerc burrower){
            if (!burrower.burrowed()){
                super.draw(unit);
            }
        } else {
            super.draw(unit);
        }

    }
}
