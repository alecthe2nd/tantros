package tantros.content.world;

import arc.graphics.Color;
import arc.struct.ObjectSet;
import arc.struct.Seq;

import static mindustry.Vars.indexer;
import static mindustry.ai.UnitCommand.*;

import mindustry.Vars;
import mindustry.ai.ItemUnitStance;
import mindustry.ai.UnitStance;
import mindustry.ai.types.*;
import mindustry.content.*;
import mindustry.entities.abilities.*;
import mindustry.entities.bullet.*;
import mindustry.entities.effect.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.type.ammo.ItemAmmoType;
import mindustry.type.weapons.*;
import mindustry.world.meta.*;
import static tantros.ai.TantrosUnitCommands.*;

import tantros.content.TantrosFx;
import tantros.entities.abilities.burrow.BurrowAbility;
import tantros.gen.*;
import tantros.type.units.*;

public class TantrosUnitTypes {

    public static UnitType

    testBoat,
    aquas,
    flak,
    sherd,
    fractoid,
    roach,
    infest,
    invade,
    enact,
    delegate,
    largeFisk
    ;

    public static void load(){

        testBoat = new UnitType("testBoat"){{
            envEnabled = Env.underwater;
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
                shootSound = Sounds.shoot;
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

                    trailEffect = TantrosFx.parallaxBubble;
                    trailChance = 0.05f;
                }};
            }});
        }};

        flak = new UnitType("flak"){{
            constructor = MechUnit::create;
            researchCostMultiplier = 0.5f;
            speed = 1.9f;
            hitSize = 7f;
            health = 150;
            maxRange = 35;

            weapons.add(new Weapon("tantros-flak-weapon"){{
                reload = 15f;
                x = 3f;
                y = 1f;
                top = false;
                layerOffset = -0.01f;
                ejectEffect = Fx.casing1;
                bullet = new BasicBulletType(3f, 9){{
                    width = 7f;
                    height = 9f;
                    lifetime = 60f;

                    trailChance = 0.05f;
                }};
            }});

            abilities = Seq.with(
                    new MoveEffectAbility(0,0,Liquids.water.color, new MultiEffect(TantrosFx.parallaxBubble), 10)
            );
        }};

        sherd = new UnitType("sherd"){{
            constructor = MechUnit::create;
            researchCostMultiplier = 0.5f;
            speed = 0.5f;
            hitSize = 10f;
            health = 550;
            armor = 4f;

            weapons.add(new Weapon("tantros-artillery-blue"){{
                reload = 13f;
                x = 5.5f;
                y = 1f;
                top = false;
                layerOffset = -0.01f;
                ejectEffect = new MultiEffect(Fx.casing1, TantrosFx.parallaxBubble);
                bullet = new BasicBulletType(3f, 12){{
                    width = 7f;
                    height = 9f;
                    lifetime = 60f;

                    drag = 0.01f;

                    trailChance = 0.05f;

                    fragBullets = 6;
                    fragRandomSpread = 30f;
                    fragSpread = 60f;
                    fragVelocityMin = 1f;

                    fragBullet = new BasicBulletType(5f, 3f){{
                        lifetime = 4f;
                        width = 11f;
                        height = 14f;
                        hitSize = 7f;
                        shootEffect = new MultiEffect(Fx.shootBigColor, Fx.colorSparkBig);
                        ammoMultiplier = 1;
                        reloadMultiplier = 1f;
                        hitColor = backColor = trailColor = Color.valueOf("ab8ec5");
                        frontColor = Color.white;
                        trailWidth = 1.8f;
                        trailLength = 11;
                        hitEffect = despawnEffect = Fx.hitBulletColor;
                    }};
                }};
            }});
        }};

        fractoid = new UnitType("fractoid"){{
            constructor = LegsUnit::create;
            speed = 0.6f;
            drag = 0.4f;
            hitSize = 13f;
            rotateSpeed = 3f;
            health = 600;

            legCount = 4;
            legLength = 9f;
            legForwardScl = 0.6f;
            legMoveSpace = 1.4f;
            lockLegBase = true;
            //hovering = true;
            armor = 3f;

            shadowElevation = 0.2f;
            groundLayer = Layer.legUnit - 1f;

            weapons.add(new Weapon("tantros-fractoid-weapon"){{
                top = false;
                layerOffset = -0.01f;
                shootY = 3f;
                reload = 18f;
                ejectEffect = new MultiEffect(Fx.casing1, TantrosFx.parallaxBubble);
                recoil = 1f;
                x = 9f;
                y = 1f;
                //shootSound = Sounds.flame;

                bullet = new ArtilleryBulletType(2f, 20, "shell"){{
                    hitEffect = new MultiEffect(Fx.blastExplosion, TantrosFx.parallaxBubble, TantrosFx.parallaxBubble, TantrosFx.parallaxBubble);

                    trailEffect = new MultiEffect(trailEffect, TantrosFx.parallaxBubble);

                    knockback = 0.8f;
                    lifetime = 120f;
                    width = height = 14f;
                    collides = true;
                    collidesTiles = true;
                    splashDamageRadius = 35f;
                    splashDamage = 80f;
                    backColor = Pal.bulletYellowBack;
                    frontColor = Pal.bulletYellow;
                }};
            }});
        }};

        roach = EntityRegistry.content("roach", BurrowerUnit.class, name -> new BurrowerUnitType(name){{
            //constructor = LegsUnit::create;
            aiController = SuicideAI::new;
            speed = 0.5f;
            drag = 0.11f;
            hitSize = 7f;
            rotateSpeed = 6f;
            health = 360;
            armor = 4f;
            legStraightness = 0.3f;
            stepShake = 0f;
            mechStepParticles = false;

            legCount = 6;
            legLength = 6f;
            lockLegBase = true;
            legContinuousMove = true;
            legExtension = -2f;
            legBaseOffset = 2f;
            legMaxLength = 1.0f;
            legMinLength = 0.7f;
            legLengthScl = 0.98f;
            legForwardScl = 1.5f;
            legGroupSize = 3;
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
                            TantrosFx.parallaxBubble,
                            TantrosFx.parallaxBubble,
                            TantrosFx.parallaxBubble,
                            TantrosFx.parallaxBubble,
                            TantrosFx.parallaxBubble,
                            TantrosFx.parallaxBubble
                    );
                    speed = 0f;
                    splashDamageRadius = 44f;
                    instantDisappear = true;
                    splashDamage = 160f;
                    killShooter = true;
                    hittable = false;
                    collidesAir = false;
                }};
            }});
            abilities.add(new BurrowAbility());
        }});

        infest = EntityRegistry.content("infest", BurrowerUnit.class, name -> new BurrowerUnitType(name){{
            //constructor = LegsUnit::create;
            speed = 0.5f;
            drag = 0.11f;
            hitSize = 10f;
            rotateSpeed = 6f;
            health = 680;
            armor = 4f;
            legStraightness = 0.3f;
            stepShake = 0f;
            mechStepParticles = false;

            legCount = 6;
            legLength = 6f;
            lockLegBase = true;
            legContinuousMove = true;
            legExtension = -2f;
            legBaseOffset = 2f;
            legMaxLength = 1.0f;
            legMinLength = 0.7f;
            legLengthScl = 0.98f;
            legForwardScl = 1.5f;
            legGroupSize = 3;
            rippleScale = 0.2f;

            legMoveSpace = 1f;
            allowLegStep = true;
            hovering = true;
            legPhysicsLayer = false;

            shadowElevation = 0.1f;
            groundLayer = Layer.legUnit - 1f;
            targetAir = false;
            researchCostMultiplier = 0f;

            weapons.add(new Weapon("infest-weapon"){{
                            mirror = false;
                            top = false;
                            shake = 4f;
                            shootY = 7f;
                            x = y = 0f;

                            alwaysContinuous = true;

                            parentizeEffects = true;

                            recoil = 0f;
                            shootSound = Sounds.shootSublimate;
                            continuous = true;


                            bullet = new ContinuousFlameBulletType(){{
                                damage = 35f;
                                length = 8f;
                                width = 1.7f;
                                hitEffect = Fx.hitMeltHeal;
                                drawSize = 420f;
                                lifetime = 160f;
                                despawnEffect = Fx.none;
                                smokeEffect = Fx.none;
                                drawFlare = false;
                                maxRange = length + 10;

                                collidesTeam = true;

                                colors = new Color[]{Pal.sapBullet.cpy().a(.2f), Pal.sapBullet.cpy().a(.5f), Pal.sapBullet.cpy().mul(1.2f), Color.white};
                            }};

                            shootStatus = StatusEffects.slow;
                            shootStatusDuration = bullet.lifetime + shoot.firstShotDelay;
                        }}
            );
            abilities.add(new BurrowAbility());
        }});

        invade = EntityRegistry.content("invade", BurrowerUnit.class, name -> new BurrowerUnitType(name){{
            //constructor = LegsUnit::create;
            speed = 0.5f;
            drag = 0.11f;
            hitSize = 14f;
            rotateSpeed = 6f;
            health = 990;
            armor = 4f;
            legStraightness = 0.7f;
            stepShake = 0.01f;
            mechStepParticles = false;

            legCount = 6;
            legLength = 12f;

            lockLegBase = true;
            legContinuousMove = true;
            legExtension = -2f;
            legBaseOffset = 3f;
            legMaxLength = 1.0f;
            legMinLength = 0.7f;
            legLengthScl = 1.1f;
            legForwardScl = 1f;
            legGroupSize = 3;
            rippleScale = 0.2f;

            legMoveSpace = 1.4f;
            allowLegStep = true;
            hovering = true;
            legPhysicsLayer = false;

            shadowElevation = 0.1f;
            groundLayer = Layer.legUnit - 1f;
            targetAir = false;
            researchCostMultiplier = 0f;

            weapons.add(new Weapon("tantros-invade-weapon"){{
                            mirror = true;
                            top = false;
                            shake = 4f;
                            shootY = 6f;
                            reload = 30;
                            layerOffset = -0.01f;
                            rotate = true;
                            rotationLimit = 90f;

                            x = 6;
                            y = 7f;

                            parentizeEffects = true;

                            recoil = 2f;
                            shootSound = Sounds.shootLancer;


                            bullet = new LaserBulletType(){{
                                damage = 45f;
                                recoil = 0f;
                                sideAngle = 45f;
                                sideWidth = 1f;
                                sideLength = 16f;
                                length = 32f;
                                colors = new Color[]{Pal.sapBullet.cpy().a(.2f), Pal.sapBullet.cpy().a(.5f), Pal.sapBullet.cpy().mul(1.2f), Color.white};
                            }};

                            shootStatus = StatusEffects.slow;
                            shootStatusDuration = bullet.lifetime + shoot.firstShotDelay;
                        }}
            );

            abilities.add(
                    new ShieldRegenFieldAbility(20f, 60f, 60f * 5, 60f)
            );
            abilities.add(new BurrowAbility());
        }});

        enact = new UnitType("enact"){{
            coreUnitDock = true;
            controller = u -> new BuilderAI(true, 300);
            constructor = UnitEntity::create;
            isEnemy = false;
            envDisabled = 0;
            envEnabled = Env.underwater;

            range = 60f;
            faceTarget = true;
            targetPriority = -2;
            lowAltitude = false;
            mineWalls = true;
            mineFloor = false;
            mineHardnessScaling = false;
            flying = true;
            mineSpeed = 6f;
            mineTier = 3;
            buildSpeed = 1.2f;
            drag = 0.08f;
            speed = 2f;
            rotateSpeed = 2.5f;
            accel = 0.07f;
            itemCapacity = 60;
            health = 300f;
            armor = 1f;
            hitSize = 9f;
            engineSize = 2;
            engineOffset = 4;

            targetable = true;
            hittable = true;

            abilities = Seq.with(
                    new MoveEffectAbility(0,0,Liquids.water.color, new MultiEffect(TantrosFx.parallaxBubble,TantrosFx.parallaxBubble,TantrosFx.parallaxBubble), 10)
            );

            weapons.add(new RepairBeamWeapon(){{
                widthSinMag = 0.11f;
                reload = 20f;
                x = 0f;
                y = 6.5f;
                rotate = false;
                shootY = 0f;
                beamWidth = 0.7f;
                repairSpeed = 3.1f;
                fractionRepairSpeed = 0.06f;
                aimDst = 0f;
                shootCone = 15f;
                mirror = false;

                targetUnits = false;
                targetBuildings = true;
                autoTarget = false;
                controllable = true;
                laserColor = Pal.accent;
                healColor = Pal.accent;

                bullet = new BulletType(){{
                    maxRange = 60f;
                }};
            }});
        }};

        delegate = new UnitType("delegate"){{
            constructor = LegsUnit::create;
            defaultCommand = groundMineCommand;

            drawBuildBeam = true;

            commands.add(
                        Seq.with(
                        moveCommand,
                        enterPayloadCommand,
                        groundRepairCommand,
                        groundMineCommand,
                        groundRebuildCommand,
                        groundAssistCommand
                    )
            );
            speed = 0.72f;
            drag = 0.11f;
            hitSize = 9f;
            rotateSpeed = 3f;
            health = 160;
            stepShake = 0f;

            rotateToBuilding = true;

            /*legCount = 4;
            legLength = 8f;
            //lockLegBase = true;
            legContinuousMove = true;
            legBaseOffset = 1f;
            legLengthScl = 0.96f;
            //legForwardScl = 1.5f;*/

            legCount = 4;
            legLength = 6f;
            legExtension = -2f;
            legMaxLength = 1.1f;
            legMinLength = 0.2f;
            legForwardScl = 0.6f;
            legMoveSpace = 1.4f;
            lockLegBase = true;
            rippleScale = 0.1f;

            legMoveSpace = 1f;
            allowLegStep = true;
            hovering = true;
            legPhysicsLayer = false;

            shadowElevation = 0.1f;
            groundLayer = Layer.legUnit - 1f;
            targetAir = false;
            researchCostMultiplier = 0f;

            mineFloor = false;
            mineWalls = true;
            mineTier = 2;
            mineSpeed = 0.5f;

            buildSpeed = 0.01f;
            buildRange = Vars.buildingRange * 0.25f;

            abilities.add(new RepairFieldAbility(5f, 60f * 8, 50f));

            weapons.add(
                new RepairBeamWeapon(){{

                    widthSinMag = 0.11f;
                    reload = 20f;
                    x = 0f;
                    y = 6f;
                    rotate = false;
                    shootY = 0f;
                    beamWidth = 0.3f;
                    repairSpeed = 0.1f;
                    fractionRepairSpeed = 0.06f;
                    aimDst = 0f;
                    shootCone = 15f;
                    mirror = false;

                    targetUnits = false;
                    targetBuildings = true;
                    autoTarget = false;
                    controllable = true;
                    laserColor = Pal.accent;
                    healColor = Pal.accent;

                    bullet = new BulletType(){{
                        maxRange = 60f;
                    }};
                }}
            );

        }
            @Override
            public void getUnitStances(Unit unit, Seq<UnitStance> out){
                //return mining stances based on present items
                if(unit.controller() instanceof CommandAI ai && ai.currentCommand() == groundMineCommand){
                    out.add(UnitStance.mineAuto);
                    for(Item item : indexer.getAllPresentOres()){
                        if(unit.canMine(item) && ((mineFloor && indexer.hasOre(item)) || (mineWalls && indexer.hasWallOre(item)))){
                            var itemStance = ItemUnitStance.getByItem(item);
                            if(itemStance != null){
                                out.add(itemStance);
                            }
                        }
                    }
                }else{
                    out.addAll(stances);
                }
            }
        };

        largeFisk = new UnitType("large-fisk"){{

            playerControllable = false;
            logicControllable = false;
            targetable = false;
            hittable = false;
            drawCell = false;

            deathExplosionEffect = Fx.none;

            constructor = UnitEntity::create;
            isEnemy = false;
            envDisabled = 0;
            envEnabled = Env.underwater;

            flying = true;

            omniMovement = false;
            drag = 0.08f;
            speed = 2f;
            rotateSpeed = 2.5f;
            accel = 0.07f;

            health = 300f;
            armor = 1f;
            hitSize = 16f;

        }};

    }
}
