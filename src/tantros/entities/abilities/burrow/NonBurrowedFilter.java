package tantros.entities.abilities.burrow;

import mindustry.Vars;
import mindustry.gen.Unit;
import tantros.TantrosVars;
import tantros.gen.Burrowerc;
import tantros.world.blocks.effect.GroundPenetratingRadar;
import tantros.world.meta.UnitFilter;

public class NonBurrowedFilter extends UnitFilter {
    @Override
    protected boolean filter(Unit unit) {
        GroundPenetratingRadar.GroundPenetratingRadarBuild build = TantrosVars.sonarIndexer.get((b)-> unit.dst(b) < b.fogRadius() * Vars.tilesize && unit.team != b.team);

        return (!(unit instanceof Burrowerc burrower) || !burrower.burrowed() || build != null);
    }
}
