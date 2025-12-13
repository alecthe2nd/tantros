package tantros.content.blocks;

import arc.graphics.Color;
import arc.math.Interp;
import arc.math.Mathf;
import arc.struct.EnumSet;
import mindustry.content.*;
import mindustry.entities.bullet.BasicBulletType;
import mindustry.entities.bullet.LiquidBulletType;
import mindustry.entities.bullet.MissileBulletType;
import mindustry.entities.effect.MultiEffect;
import mindustry.entities.part.RegionPart;
import mindustry.entities.pattern.ShootAlternate;
import mindustry.gen.Sounds;
import mindustry.graphics.Layer;
import mindustry.graphics.Pal;
import mindustry.type.Category;
import mindustry.world.Block;
import mindustry.world.blocks.defense.turrets.ItemTurret;
import mindustry.world.blocks.defense.turrets.LiquidTurret;
import mindustry.world.consumers.ConsumeCoolant;
import mindustry.world.consumers.ConsumeLiquid;
import mindustry.world.draw.DrawTurret;
import mindustry.world.meta.BlockFlag;
import tantros.content.world.TantrosLiquids;

import static mindustry.type.ItemStack.with;

public class TantrosTurret {

    public static Block
            bident,
            jetstream,
            thrust
            ;

    public static void load(){
        bident = new ItemTurret("bident"){{
            requirements(Category.turret, with(Items.copper, 35));
            ammo(
                    Items.copper,  new BasicBulletType(2f, 9){{
                        width = 7f;
                        height = 9f;
                        lifetime = 60f;
                        ammoMultiplier = 2;

                        hitEffect = despawnEffect = Fx.hitBulletColor;
                        hitColor = backColor = trailColor = Pal.copperAmmoBack;
                        frontColor = Pal.copperAmmoFront;
                    }},
                    Items.oxide, new BasicBulletType(1.5f, 6){{
                        width = 7f;
                        height = 9f;
                        lifetime = 60f;
                        ammoMultiplier = 2;

                        hitEffect = despawnEffect = Fx.hitBulletColor;
                        hitColor = backColor = trailColor = Pal.copperAmmoBack;
                        frontColor = Pal.copperAmmoFront;

                        fragBullets = 3;
                        fragRandomSpread = 0f;
                        fragSpread = 25f;
                        fragVelocityMin = 1f;

                        fragBullet = new BasicBulletType(3f, 3f){{
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
                    }},
                    Items.metaglass, new BasicBulletType(3.5f, 18){{
                        width = 9f;
                        height = 12f;
                        ammoMultiplier = 4;
                        lifetime = 60f;
                        rangeChange = 80f;

                        hitEffect = despawnEffect = Fx.hitBulletColor;
                        hitColor = backColor = trailColor = Pal.glassAmmoBack;
                        frontColor = Pal.glassAmmoFront;
                    }},
                    Items.silicon, new BasicBulletType(3f, 12){{
                        width = 7f;
                        height = 9f;
                        homingPower = 0.2f;
                        reloadMultiplier = 1.5f;
                        ammoMultiplier = 5;
                        lifetime = 60f;

                        trailLength = 5;
                        trailWidth = 1.5f;

                        hitEffect = despawnEffect = Fx.hitBulletColor;
                        hitColor = backColor = trailColor = Pal.siliconAmmoBack;
                        frontColor = Pal.siliconAmmoFront;
                    }}
            );

            shoot = new ShootAlternate(3.5f);

            recoils = 2;
            drawer = new DrawTurret(){{
                for(int i = 0; i < 2; i ++){
                    int f = i;
                    parts.add(new RegionPart("-barrel-" + (i == 0 ? "l" : "r")){{
                        progress = PartProgress.recoil;
                        recoilIndex = f;
                        under = true;
                        moveY = -1.5f;
                    }});
                }
            }};

            recoil = 0.5f;
            shootY = 3f;
            reload = 20f;
            range = 80;
            shootCone = 15f;
            ammoUseEffect = Fx.casing1;
            health = 250;
            inaccuracy = 2f;
            rotateSpeed = 10f;
            coolant = consume(new ConsumeCoolant(6f/60f){{
                filter = liquid -> liquid != Liquids.water && liquid.coolant && (this.allowLiquid && !liquid.gas || this.allowGas && liquid.gas) && liquid.temperature <= maxTemp && liquid.flammability < maxFlammability;
            }});
            researchCostMultiplier = 0.05f;

            limitRange(5f);
        }};


        jetstream = new LiquidTurret("jetstream"){{
            requirements(Category.turret, with(Items.metaglass, 45, Items.oxide, 75, Items.copper, 25));
            ammo(
                    Liquids.water,new LiquidBulletType(Liquids.water){{
                        knockback = 0.9f;
                        drag = 0.01f;
                        layer = Layer.bullet - 2f;
                        inaccuracy = 7.5f;
                        reload = 6;
                        limitRange(this, 5f);
                    }},
                    TantrosLiquids.steam, new LiquidBulletType(TantrosLiquids.steam){{
                        knockback = 0.7f;
                        damage = 6;
                        drag = 0.01f;
                        boilTime = 80;
                    }}
            );
            size = 2;
            recoil = 1f;
            reload = 3f;
            inaccuracy = 2.5f;
            shootCone = 50f;
            liquidCapacity = 10f;
            shootEffect = Fx.shootLiquid;
            range = 110f;
            scaledHealth = 250;

            flags = EnumSet.of(BlockFlag.turret, BlockFlag.extinguisher);
        }};
        thrust = new ItemTurret("thrust"){
            {
                requirements(Category.turret, with(Items.copper, 25, Items.graphite, 20, Items.metaglass, 15));

                ammo(
                        Items.copper,  new MissileBulletType(3.7f, 10){{
                            width = 8f;
                            height = 8f;
                            shrinkY = 0f;
                            splashDamageRadius = 30f;
                            splashDamage = 30f * 1.5f;
                            ammoMultiplier = 1f;
                            hitEffect = Fx.blastExplosion;
                            despawnEffect = Fx.blastExplosion;

                            status = StatusEffects.blasted;

                            hitColor = backColor = trailColor = TantrosLiquids.steam.color;
                            frontColor = Pal.copperAmmoFront;
                        }},
                        Items.graphite,  new MissileBulletType(3.7f, 10){{
                            width = 8f;
                            height = 8f;
                            shrinkY = 0f;
                            splashDamageRadius = 30f;
                            splashDamage = 30f * 1.5f;
                            ammoMultiplier = 2f;
                            hitEffect = Fx.blastExplosion;
                            despawnEffect = Fx.blastExplosion;

                            status = StatusEffects.blasted;

                            hitColor = backColor = trailColor = Pal.blastAmmoBack;
                            frontColor = Pal.blastAmmoFront;
                        }},
                        Items.silicon,  new MissileBulletType(3.7f, 10){{
                            width = 8f;
                            height = 8f;
                            shrinkY = 0f;
                            splashDamageRadius = 30f;
                            splashDamage = 30f * 1.5f;
                            ammoMultiplier = 1f;
                            hitEffect = Fx.blastExplosion;
                            despawnEffect = Fx.blastExplosion;
                            homingPower = 0.1f;

                            status = StatusEffects.blasted;

                            hitColor = backColor = trailColor = Pal.blastAmmoBack;
                            frontColor = Pal.blastAmmoFront;
                        }}
                );

                drawer = new DrawTurret(){{
                    parts.add(new RegionPart("-missile"){{
                                progress = PartProgress.reload.curve(Interp.pow2In);

                                colorTo = new Color(1f, 1f, 1f, 0f);
                                color = Color.white;
                                mixColorTo = Pal.accent;
                                mixColor = new Color(1f, 1f, 1f, 0f);
                                outline = false;
                                under = true;
                                layerOffset = -0.01f;

                                moves.add(new PartMove(PartProgress.warmup, 0f, 2f, 0f));
                            }});
                }};

                recoil = 0.5f;
                shootSound = Sounds.missile;

                minWarmup = 0.94f;
                newTargetInterval = 40f;
                targetAir = true;

                ammoPerShot = 1;
                maxAmmo = 5;
                size = 1;
                reload = 50;
                range = 200;
                shootCone = 1f;
                scaledHealth = 220;
                rotateSpeed = 5f;

                consume(new ConsumeLiquid(TantrosLiquids.steam, 5f / 60f));
                limitRange();
            }
        };
    }
}
