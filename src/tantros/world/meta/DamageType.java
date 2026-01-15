package tantros.world.meta;

import arc.audio.Sound;
import arc.func.Boolf;
import arc.graphics.Color;
import mindustry.gen.Bullet;
import mindustry.gen.Sounds;
import mindustry.graphics.Pal;

public class DamageType {

    public Boolf<Bullet> of;

    public DamageType(Boolf<Bullet> isType){
        this.of = isType;
    }

    public boolean of(Bullet t){
        return of.get(t);
    }

}
