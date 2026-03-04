package tantros.type.units;

import static mindustry.Vars.*;

import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.struct.Seq;
import arc.util.Align;
import mindustry.Vars;
import mindustry.entities.bullet.BulletType;
import mindustry.entities.units.AIController;
import mindustry.game.Team;
import mindustry.gen.Bullet;
import mindustry.gen.Player;
import mindustry.gen.Unit;
import mindustry.graphics.Layer;
import mindustry.graphics.Pal;
import mindustry.type.UnitType;
import mindustry.ui.Fonts;
import mindustry.world.blocks.defense.turrets.*;
import tantros.TantrosVars;
import tantros.ai.types.BurrowAI;
import static tantros.ai.TantrosUnitCommands.*;
import tantros.gen.Burrowerc;
import tantros.graphics.TantrosLayers;
import tantros.graphics.TantrosPal;
import tantros.ui.TantrosFonts;

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
            } else if(unit.team == player.team()){
                Draw.draw(Layer.overlayUI,()->{
                    Lines.stroke(1f);
                    Draw.color(TantrosPal.radarLight);
                    Lines.poly(unit.x, unit.y, 4, (unit.hitSize * 3 / 4) + 0.5f);
                    Draw.color(unit.team.color);
                    if(unit.vel.len2() > 0.01){
                        Lines.poly(unit.x, unit.y, 3, (unit.hitSize / 2) + 0.5f, unit.vel.angle());
                    }
                    //Draw.rect(region, unit.x, unit.y, unit.rotation - 90);
                    //uncomment for a dark border
                    //Draw.color(Pal.gray);
                    //Lines.poly(unit.x, unit.y, sides, rad + 1.5f);
                    Draw.reset();
                });
            } else  {
                Draw.draw(TantrosLayers.radarObjectLayer,()->{

                    Lines.stroke(1f);
                    Draw.color(TantrosPal.radarLight);
                    Lines.poly(unit.x, unit.y, 4, (unit.hitSize * 3 / 4) + 0.5f);
                    Draw.color(unit.team.color);
                    if(unit.vel.len2() > 0.01){
                        Lines.poly(unit.x, unit.y, 3, (unit.hitSize / 2) + 0.5f, unit.vel.angle());
                    }

                    //uncomment for a dark border
                    //Draw.color(Pal.gray);
                    //Lines.poly(unit.x, unit.y, sides, rad + 1.5f);
                    Draw.reset();
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
