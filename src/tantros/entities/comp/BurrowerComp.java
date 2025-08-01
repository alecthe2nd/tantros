package tantros.entities.comp;

import ent.anno.Annotations.*;
import mindustry.entities.units.AIController;
import mindustry.game.Team;
import mindustry.gen.*;
import tantros.ai.types.BurrowAI;
import tantros.gen.Burrowerc;

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
        burrowed = false;
        burrowCooldown = cooldown;
    }

    @Replace(1)
    @Override
    public boolean canShoot() {
        return !burrowed;
    }

    @Replace(1)
    @Override
    public boolean collides(Hitboxc other) {
        return hittable() && (!burrowed || (other instanceof Burrowerc burrower && burrower.burrowed()) || ((other instanceof Bullet bullet) && bullet.type.splashDamage > 0)) ;
    }


}
