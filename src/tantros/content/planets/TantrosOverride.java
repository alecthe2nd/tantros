package tantros.content.planets;

import arc.func.Prov;
import mindustry.Vars;
import mindustry.content.Planets;
import mindustry.game.Team;
import mindustry.ui.Fonts;
import mindustry.world.blocks.defense.turrets.Turret;
import tantros.content.TantrosLoadouts;
import tantros.content.TantrosTechTree;
import tantros.content.blocks.TantrosBlocks;
import tantros.entities.abilities.burrow.NonBurrowedFilter;
import tantros.maps.planet.TantrosPlanetGenerator;
import tantros.type.units.BurrowerUnitType;
import tantros.world.meta.UnitFilter;

public class TantrosOverride {

    public static void override(){
        Planets.tantros.accessible = true;
        Planets.tantros.visible = true;
        Planets.tantros.alwaysUnlocked = true;
        Planets.tantros.defaultCore = TantrosBlocks.coreShell;
        Planets.tantros.allowLaunchToNumbered = false;
        Planets.tantros.updateLighting = true;
        Planets.tantros.clearSectorOnLose = true;


        Planets.tantros.ruleSetter = r -> {
            r.waveTeam = Team.blue;
            r.coreDestroyClear = true;
        };
        TantrosSectorPresets.load();
        TantrosTechTree.load();
        TantrosLoadouts.load();
        Planets.tantros.generator = new TantrosPlanetGenerator();
        Planets.tantros.generator.defaultLoadout = TantrosLoadouts.basicShell;
        applyUnitFilterToTurrets(NonBurrowedFilter::new);
    }

    public static void applyUnitFilterToTurrets( Prov<UnitFilter> filterSource){
        Vars.content.each(content -> {
            if(content instanceof Turret turret/* && !BurrowerUnitType.canTurretTargetBurrowed(turret)*/){
                turret.unitFilter = filterSource.get().withChain(turret.unitFilter);
            }
        });
    }

}
