package tantros.world.meta;

import mindustry.content.StatusEffects;
import mindustry.entities.bullet.LaserBulletType;

public class DamageTypes {

    public static DamageType
        energy = new DamageType((b)->
            b.type.status == StatusEffects.electrified ||
                    b.type.status == StatusEffects.shocked ||
                    b.type instanceof LaserBulletType
    )
    ;


}
