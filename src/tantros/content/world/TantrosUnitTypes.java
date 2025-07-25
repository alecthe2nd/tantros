package tantros.content.world;

import arc.graphics.Color;
import arc.graphics.g2d.Lines;
import arc.math.Interp;
import mindustry.ai.types.SuicideAI;
import mindustry.content.Fx;
import mindustry.entities.Effect;
import mindustry.entities.abilities.MoveEffectAbility;
import mindustry.entities.bullet.ArtilleryBulletType;
import mindustry.entities.bullet.BasicBulletType;
import mindustry.entities.bullet.BulletType;
import mindustry.entities.effect.MultiEffect;
import mindustry.entities.effect.SeqEffect;
import mindustry.entities.effect.WaveEffect;
import mindustry.gen.LegsUnit;
import mindustry.gen.MechUnit;
import mindustry.gen.Sounds;
import mindustry.gen.UnitEntity;
import mindustry.graphics.Drawf;
import mindustry.graphics.Layer;
import mindustry.graphics.Pal;
import mindustry.type.UnitType;
import mindustry.type.Weapon;
import mindustry.world.meta.BlockFlag;

import static arc.graphics.g2d.Draw.color;
import static arc.graphics.g2d.Lines.stroke;

public class TantrosUnitTypes {

    static UnitType

    testBoat,
    aquas,
    roach;

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

        aquas = new UnitType("aquas"){{
            constructor = MechUnit::create;
            researchCostMultiplier = 0.5f;
            speed = 0.5f;
            hitSize = 8f;
            health = 150;
            weapons.add(new Weapon("tantros-aquas-weapon"){{
                reload = 13f;
                x = 3f;
                y = 1f;
                top = false;
                ejectEffect = Fx.casing1;
                bullet = new BasicBulletType(2.5f, 9){{
                    width = 7f;
                    height = 9f;
                    lifetime = 60f;

                    //trailLength = 5;
                    //trailWidth = 1.5f;
                    trailEffect = Fx.airBubble;
                    trailChance = 0.05f;
                    //trailColor = backColor;
                }};
            }});
        }};
        roach = new UnitType("roach"){{
            constructor = LegsUnit::create;
            aiController = SuicideAI::new;
            speed = 0.72f;
            drag = 0.11f;
            hitSize = 9f;
            rotateSpeed = 3f;
            health = 680;
            armor = 4f;
            legStraightness = 0.3f;
            stepShake = 0f;

            legCount = 8;
            legLength = 12f;
            lockLegBase = true;
            legContinuousMove = true;
            legExtension = -2f;
            legBaseOffset = 3f;
            legMaxLength = 1.1f;
            legMinLength = 0.2f;
            legLengthScl = 0.96f;
            legForwardScl = 1.1f;
            legGroupSize = 4;
            rippleScale = 0.2f;

            legMoveSpace = 1f;
            allowLegStep = true;
            hovering = true;
            legPhysicsLayer = false;

            shadowElevation = 0.1f;
            groundLayer = Layer.legUnit - 1f;
            targetAir = false;
            researchCostMultiplier = 0f;

            weapons.add(new Weapon(){{
                shootOnDeath = true;
                targetUnderBlocks = false;
                reload = 24f;
                shootCone = 180f;
                ejectEffect = Fx.none;
                shootSound = Sounds.explosion;
                x = shootY = 0f;
                mirror = false;
                bullet = new BulletType(){{
                    collidesTiles = false;
                    collides = false;
                    hitSound = Sounds.explosion;

                    rangeOverride = 25f;
                    hitEffect = new MultiEffect(
                            Fx.airBubble,
                            Fx.airBubble,
                            Fx.airBubble,
                            Fx.airBubble,
                            Fx.airBubble,
                            Fx.airBubble);
                    speed = 0f;
                    splashDamageRadius = 44f;
                    instantDisappear = true;
                    splashDamage = 80f;
                    killShooter = true;
                    hittable = false;
                    collidesAir = false;
                }};
            }});
        }};
    }
}
