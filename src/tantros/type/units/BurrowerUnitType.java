package tantros.type.units;

import static mindustry.Vars.*;

import arc.graphics.g2d.Draw;
import arc.struct.Seq;
import mindustry.entities.bullet.BulletType;
import mindustry.entities.units.AIController;
import mindustry.gen.Bullet;
import mindustry.gen.Player;
import mindustry.gen.Unit;
import mindustry.type.UnitType;
import mindustry.world.blocks.defense.turrets.*;
import tantros.ai.types.BurrowAI;
import static tantros.ai.TantrosUnitCommands.*;
import tantros.gen.Burrowerc;
import tantros.graphics.TantrosLayers;

public class BurrowerUnitType extends UnitType {
    public BurrowerUnitType(String name) {
        super(name);
    }

    private static Seq<BulletType> tempBullSeq = new Seq<>();

    @Override
    public void init() {
        super.init();
        commands.add(burrowCommand);
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
            } else {
                Draw.draw(TantrosLayers.radarObjectLayer,()->{
                    Draw.rect(region, unit.x, unit.y, unit.rotation - 90);
                });
            }
        } else {
            super.draw(unit);
        }

    }

    public static boolean canDislodge(Bullet bullet){
        return canDislodge(bullet.type());
    }

    public static boolean canDislodge(BulletType bullet){
        return bullet.splashDamage > 0;
    }

    public static boolean canTurretTargetBurrowed(Turret turret){
        tempBullSeq.clear();
        if(turret instanceof ItemTurret itemTurret){
            return itemTurret.ammoTypes.values().toSeq(tempBullSeq).find(BurrowerUnitType::canDislodge) != null;
        }
        if(turret instanceof LiquidTurret liquidTurret){
            return liquidTurret.ammoTypes.values().toSeq(tempBullSeq).find(BurrowerUnitType::canDislodge) != null;
        }
        if(turret instanceof PowerTurret powerTurret){
            return BurrowerUnitType.canDislodge(powerTurret.shootType);
        }
        if(turret instanceof PayloadAmmoTurret payloadTurret){
            return payloadTurret.ammoTypes.values().toSeq(tempBullSeq).find(BurrowerUnitType::canDislodge) != null;
        }
        if(turret instanceof ContinuousLiquidTurret liquidTurret){
            return liquidTurret.ammoTypes.values().toSeq(tempBullSeq).find(BurrowerUnitType::canDislodge) != null;
        }
        return false;
    }

}
