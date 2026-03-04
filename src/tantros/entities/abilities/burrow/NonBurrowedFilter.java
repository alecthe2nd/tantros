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
       // GroundPenetratingRadar.GroundPenetratingRadarBuild build = TantrosVars.sonarIndexer.get((b)-> unit.dst(b) < b.fogRadius() * Vars.tilesize && unit.team != b.team);
        boolean revealed = TantrosVars.sonarTracking.get((unit.team == Vars.player.team())? Vars.state.rules.waveTeam :Vars.player.team(),unit.x, unit.y );
        return (!(unit instanceof Burrowerc burrower) || !burrower.burrowed() || revealed);
    }
}
