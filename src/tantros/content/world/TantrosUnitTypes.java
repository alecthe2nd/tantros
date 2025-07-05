package tantros.content.world;

import mindustry.content.Fx;
import mindustry.entities.bullet.BasicBulletType;
import mindustry.gen.Sounds;
import mindustry.gen.UnitEntity;
import mindustry.graphics.Layer;
import mindustry.type.UnitType;
import mindustry.type.Weapon;
import mindustry.world.meta.BlockFlag;

public class TantrosUnitTypes {

    static UnitType

    testBoat;

    public static void load(){
        testBoat = new UnitType("testBoat"){{
            shadowElevationScl = 10.0f;
            constructor = UnitEntity::create;
            flyingLayer = Layer.light + 1f;
            researchCostMultiplier = 0.5f;
            speed = 2.7f;
            accel = 0.08f;
            drag = 0.04f;
            flying = true;
            health = 70;
            engineOffset = 5.75f;
            //TODO balance
            //targetAir = false;
            targetFlags = new BlockFlag[]{BlockFlag.generator, null};
            hitSize = 9;
            itemCapacity = 10;


            weapons.add(new Weapon(){{
                y = 0f;
                x = 2f;
                reload = 20f;
                ejectEffect = Fx.casing1;
                bullet = new BasicBulletType(2.5f, 9){{
                    width = 7f;
                    height = 9f;
                    lifetime = 45f;
                    shootEffect = Fx.shootSmall;
                    smokeEffect = Fx.shootSmallSmoke;
                    ammoMultiplier = 2;
                }};
                shootSound = Sounds.pew;
            }});
        }};
    }
}
