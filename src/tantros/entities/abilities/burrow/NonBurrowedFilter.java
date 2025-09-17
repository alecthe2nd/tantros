package tantros.entities.abilities.burrow;

import mindustry.gen.Unit;
import tantros.gen.Burrowerc;
import tantros.world.meta.UnitFilter;

public class NonBurrowedFilter extends UnitFilter {
    @Override
    protected boolean filter(Unit unit) {
        return (!(unit instanceof Burrowerc burrower) || !burrower.burrowed());
    }
}
