package tantros.world.draw.extended;

import arc.func.FloatFloatf;
import arc.graphics.g2d.Draw;
import arc.math.Angles;
import arc.math.Mathf;
import arc.util.Log;
import mindustry.gen.Building;
import mindustry.graphics.Layer;
import mindustry.type.Weapon;
import tantros.world.blocks.BlockExtended;

public class DrawWeapon extends DrawBlockExtended{

    public Weapon weapon;

    public DrawWeapon(Weapon weapon){
        this.weapon = weapon;
    }

    @Override
    public void draw(BlockExtended.BuildExtended build) {
        float z = Draw.z();

        if(z >= Layer.blockOver){
            Draw.z(Layer.blockOver - weapon.layerOffset);
        } else {
            Draw.z(z + weapon.layerOffset);
        }
        //Log.info("Drew: " + Draw.z() + " | " + z + ";");
        float
                rotation = build.rotdeg() - 90,
                weaponRotation  = rotation + weapon.baseRotation,
                wx = build.x + Angles.trnsx(rotation, weapon.x, weapon.y),
                wy = build.y + Angles.trnsy(rotation, weapon.x, weapon.y);


        float prev = Draw.xscl;

        Draw.xscl *= -Mathf.sign(weapon.flipSprite);

        if(weapon.outlineRegion.found()) Draw.rect(weapon.outlineRegion, wx, wy, weaponRotation);
        if(weapon.region.found()) Draw.rect(weapon.region, wx, wy, weaponRotation);

        Draw.xscl = prev;

        Draw.z(z);
    }
}
