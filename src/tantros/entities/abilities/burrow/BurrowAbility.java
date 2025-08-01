package tantros.entities.abilities.burrow;

import mindustry.Vars;
import mindustry.entities.abilities.Ability;
import mindustry.entities.units.UnitController;
import mindustry.gen.Player;
import mindustry.gen.Unit;

public class BurrowAbility extends Ability implements Hideable {

    public boolean burrowed = false;

    @Override
    public void hide() {
        burrowed = true;
    }

    @Override
    public void unhide() {
        burrowed = false;
    }

    @Override
    public boolean isHidden() {
        return burrowed;
    }

    @Override
    public void update(Unit unit) {
        super.update(unit);
        UnitController controller = unit.controller();
        if(controller == Vars.player && controller instanceof Player player){
            this.burrowed = player.boosting();
        }
    }
}
