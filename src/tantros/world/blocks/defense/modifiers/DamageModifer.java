package tantros.world.blocks.defense.modifiers;

import arc.audio.Sound;
import arc.graphics.Color;
import arc.math.Mathf;
import mindustry.entities.Lightning;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.gen.Bullet;
import mindustry.gen.Sounds;
import mindustry.graphics.Pal;
import tantros.world.meta.DamageType;

public class DamageModifer {

    public float multiplier;
    public DamageType type;

    /** Lighting chance. -1 to disable */
    public float lightningChance = -1f;
    public float lightningDamage = 20f;
    public int lightningLength = 17;
    public Color lightningColor = Pal.surge;
    public Sound lightningSound = Sounds.shootArc;

    /** Bullet deflection chance. -1 to disable */
    public float chanceDeflect = -1f;
    public Sound deflectSound = Sounds.none;

    public DamageModifer(DamageType type, float multiplier){
        this.type = type;
        this.multiplier = multiplier;
    }

    public float modify(Bullet bullet, Team source, float damage){
        if(type.of(bullet)){
            return damage * multiplier;
        }
        else return damage;
    }

    public boolean applyOnCollision(Bullet bullet, Building build){
        if(!type.of(bullet)) return true;
        if(lightningChance > 0f){
            if(Mathf.chance(lightningChance)){
                Lightning.create(build.team, lightningColor, lightningDamage, build.x, build.y, bullet.rotation() + 180f, lightningLength);
                lightningSound.at(build.tile, Mathf.random(0.9f, 1.1f));
            }
        }

        if(chanceDeflect > 0f){
            //slow bullets are not deflected
            if(bullet.vel.len() <= 0.1f || !bullet.type.reflectable) return true;

            //bullet reflection chance depends on bullet damage
            if(!Mathf.chance(chanceDeflect / bullet.damage())) return true;

            //make sound
            deflectSound.at(build.tile, Mathf.random(0.9f, 1.1f));

            //translate bullet back to where it was upon collision
            bullet.trns(-bullet.vel.x, -bullet.vel.y);

            float penX = Math.abs(build.x - bullet.x), penY = Math.abs(build.y - bullet.y);

            if(penX > penY){
                bullet.vel.x *= -1;
            }else{
                bullet.vel.y *= -1;
            }

            bullet.owner = build;
            bullet.team = build.team;
            bullet.time += 1f;

            //disable bullet collision by returning false
            return false;
        }
        return true;
    }

}
