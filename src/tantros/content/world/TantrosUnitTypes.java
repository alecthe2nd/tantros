package tantros.content.world;

import arc.graphics.Color;
import arc.graphics.g2d.Lines;
import arc.math.Interp;
import arc.struct.Seq;
import mindustry.ai.UnitCommand;
import mindustry.ai.types.BoostAI;
import mindustry.ai.types.BuilderAI;
import mindustry.ai.types.SuicideAI;
import mindustry.content.*;
import mindustry.entities.Effect;
import mindustry.entities.abilities.*;
import mindustry.entities.bullet.*;
import mindustry.entities.effect.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.input.Binding;
import mindustry.type.*;
import mindustry.type.unit.ErekirUnitType;
import mindustry.type.weapons.*;
import mindustry.world.meta.*;
import tantros.ai.types.BurrowAI;
import tantros.gen.*;
import tantros.type.units.*;

import static arc.graphics.g2d.Draw.color;
import static arc.graphics.g2d.Lines.stroke;

public class TantrosUnitTypes {

    public static UnitCommand

            burrowCommand
    ;
        ;

    public static UnitType

    testBoat,
    aquas,
    roach,
    enact,
    delegate
    ;

    public static void load(){


        burrowCommand = new UnitCommand("burrow", "down", null, u -> new BurrowAI()){{
            switchToMove = false;
            drawTarget = true;
            resetTarget = false;
        }};

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

        roach = EntityRegistry.content("roach", BurrowerUnit.class, name -> new BurrowerUnitType(name){{
            //constructor = LegsUnit::create;
            aiController = SuicideAI::new;
            speed = 0.5f;
            drag = 0.11f;
            hitSize = 7f;
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
                            Fx.airBubble
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

            /*setEnginesMirror(
                    new UnitEngine(21 / 4f, 19 / 4f, 2.2f, 45f),
                    new UnitEngine(23 / 4f, -22 / 4f, 2.2f, 315f)
            );*/

            abilities = Seq.with(
                    new MoveEffectAbility(0,0,Liquids.water.color, new MultiEffect(Fx.airBubble,Fx.airBubble,Fx.airBubble), 10)
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
            defaultCommand = UnitCommand.mineCommand;

            commands.add(
                        Seq.with(
                        UnitCommand.moveCommand,
                        UnitCommand.enterPayloadCommand,
                        UnitCommand.repairCommand,
                        UnitCommand.mineCommand,
                        UnitCommand.assistCommand
                    )
            );
            speed = 0.72f;
            drag = 0.11f;
            hitSize = 9f;
            rotateSpeed = 3f;
            health = 680;
            armor = 4f;
            legStraightness = 0.3f;
            stepShake = 0f;

            rotateToBuilding = true;

            legCount = 3;
            legLength = 8f;
            lockLegBase = true;
            legContinuousMove = true;
            legExtension = -2f;
            legBaseOffset = 3f;
            legMaxLength = 1.1f;
            legMinLength = 0.2f;
            legLengthScl = 0.96f;
            legForwardScl = 1.1f;
            legGroupSize = 1;
            rippleScale = 0.2f;

            legMoveSpace = 1f;
            allowLegStep = true;
            hovering = true;
            legPhysicsLayer = false;

            shadowElevation = 0.1f;
            groundLayer = Layer.legUnit - 1f;
            targetAir = false;
            researchCostMultiplier = 0f;

            mineTier = 2;
            mineSpeed = 3.5f;

            abilities.add(new RepairFieldAbility(5f, 60f * 8, 50f));

            weapons.add(
                new RepairBeamWeapon(){{
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
                }}
            );

        }};

    }
}
