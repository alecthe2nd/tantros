package tantros.entities.comp;

import ent.anno.Annotations.*;
import mindustry.game.Team;
import mindustry.gen.*;
import tantros.TantrosVars;
import tantros.gen.Burrowerc;
import tantros.type.units.BurrowerUnitType;

import static tantros.type.units.BurrowerUnitType.canDislodge;

@EntityComponent
abstract public class BurrowerComp implements Legsc {

    public boolean burrowed = false;

    public int burrowCooldown;

    public void burrowed(boolean burrow){
        if(burrowCooldown <= 0){
            burrowed = burrow;
        }
    }

    public void triggerCooldown(){
        triggerCooldown(60);
    }

    public void triggerCooldown(int cooldown){
        if(cooldown > 0) burrowed = false;
        if(cooldown > burrowCooldown) burrowCooldown = cooldown;
    }

    public void dislodge(Bullet bullet){
        if(canDislodge(bullet)){
            if(this.type() instanceof BurrowerUnitType btype){
                if(this.burrowed) this.apply(btype.dislodgeEffect, btype.dislodgeStatusDuration);
            }
            this.triggerCooldown(90);
        }
    }

    @Replace(1)
    @Override
    public boolean collides(Hitboxc other) {
        return hittable() && (!burrowed || (other instanceof Burrowerc burrower && burrower.burrowed()) || ((other instanceof Bullet bullet) && (canDislodge(bullet) || TantrosVars.sonarTracking.get(bullet.team, this.x(), this.y()))));
    }
}
