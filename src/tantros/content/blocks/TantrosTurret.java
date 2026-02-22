package tantros.content.blocks;

import arc.func.Prov;
import arc.graphics.Color;
import arc.math.Interp;
import arc.struct.EnumSet;
import arc.struct.Seq;
import mindustry.content.*;
import mindustry.entities.Effect;
import mindustry.entities.bullet.*;
import mindustry.entities.effect.MultiEffect;
import mindustry.entities.effect.WaveEffect;
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
import mindustry.world.meta.Env;
import tantros.content.TantrosFx;
import tantros.content.world.TantrosLiquids;

import static mindustry.type.ItemStack.with;

public class TantrosTurret {

    public static Block
            bident,
            jetstream,
            thrust,
            puncture,
            leviathan
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
            drawer = new DrawTurret("sealed-"){{
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
            requirements(Category.turret, with(Items.metaglass, 55, Items.oxide, 125, Items.titanium, 55));
            drawer = new DrawTurret("sealed-");

            ammo(
                    Liquids.water,new LiquidBulletType(Liquids.water){{
                        damage = 6;
                        knockback = 0.9f;
                        drag = 0.01f;
                        layer = Layer.bullet - 2f;
                        inaccuracy = 7.5f;
                        reload = 6;
                        limitRange(this, 7f);
                    }},
                    TantrosLiquids.steam, new LiquidBulletType(TantrosLiquids.steam){{
                        knockback = 0.7f;
                        damage = 36;
                        drag = 0.01f;
                        boilTime = 80;
                    }}
            );
            size = 3;
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
                requirements(Category.turret, with(Items.copper, 25, Items.metaglass, 15, Items.oxide, 10));

                ammo(
                        Items.copper,  new MissileBulletType(3.7f, 15){{
                            width = 8f;
                            height = 8f;
                            shrinkY = 0f;
                            splashDamageRadius = 30f;
                            splashDamage = 10f;
                            ammoMultiplier = 1f;
                            hitEffect = Fx.blastExplosion;
                            despawnEffect = Fx.blastExplosion;

                            status = StatusEffects.blasted;

                            hitColor = backColor = trailColor = TantrosLiquids.steam.color;
                            frontColor = Pal.copperAmmoFront;
                        }},
                        Items.graphite,  new MissileBulletType(3.7f, 20){{
                            width = 8f;
                            height = 8f;
                            shrinkY = 0f;
                            splashDamageRadius = 30f;
                            splashDamage = 8f;
                            ammoMultiplier = 2f;
                            hitEffect = Fx.blastExplosion;
                            despawnEffect = Fx.blastExplosion;

                            status = StatusEffects.blasted;

                            hitColor = backColor = trailColor = Pal.blastAmmoBack;
                            frontColor = Pal.blastAmmoFront;
                        }},
                        Items.silicon,  new MissileBulletType(3.7f, 20){{
                            width = 8f;
                            height = 8f;
                            shrinkY = 0f;
                            splashDamageRadius = 30f;
                            splashDamage = 10;
                            ammoMultiplier = 1f;
                            hitEffect = Fx.blastExplosion;
                            despawnEffect = Fx.blastExplosion;
                            homingPower = 0.1f;

                            status = StatusEffects.blasted;

                            hitColor = backColor = trailColor = Pal.blastAmmoBack;
                            frontColor = Pal.blastAmmoFront;
                        }}
                );

                drawer = new DrawTurret("sealed-"){{
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
                shootSound = Sounds.shootMissile;

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

        puncture = new ItemTurret("puncture"){{
            requirements(Category.turret, with(Items.oxide, 35, Items.metaglass,50, Items.copper, 25));

            Effect sfe = new MultiEffect(Fx.shootBigColor, Fx.colorSparkBig);
            drawer = new DrawTurret("sealed-");
            ammo(
                    Items.metaglass, new BasicBulletType(7.5f, 14){{
                        width = 12f;
                        hitSize = 7f;
                        height = 20f;
                        shootEffect = sfe;
                        smokeEffect = Fx.shootBigSmoke;
                        ammoMultiplier = 1;
                        pierceCap = 2;
                        pierce = true;
                        pierceBuilding = true;
                        hitColor = backColor = trailColor = Pal.glassAmmoFront;
                        frontColor = Color.white;
                        trailWidth = 2.1f;
                        trailLength = 10;
                        hitEffect = despawnEffect = Fx.hitBulletColor;
                        //buildingDamageMultiplier = 0.3f;
                    }},
                    Items.graphite, new BasicBulletType(8f, 12){{
                        width = 13f;
                        height = 19f;
                        hitSize = 7f;
                        shootEffect = sfe;
                        smokeEffect = Fx.shootBigSmoke;
                        ammoMultiplier = 2;
                        reloadMultiplier = 1f;
                        pierceCap = 4;
                        pierce = true;
                        pierceBuilding = true;
                        hitColor = backColor = trailColor = Pal.graphiteAmmoFront;
                        frontColor = Color.white;
                        trailWidth = 2.2f;
                        trailLength = 11;
                        hitEffect = despawnEffect = Fx.hitBulletColor;
                        rangeChange = 40f;
                        //buildingDamageMultiplier = 0.3f;
                    }}
            );

            shootSound = Sounds.shootBreach;

            targetUnderBlocks = false;
            shake = 1f;
            ammoPerShot = 1;
            shootY = -2;
            size = 2;
            envEnabled |= Env.space;
            reload = 40f;
            recoil = 2f;
            range = 90;
            shootCone = 3f;
            scaledHealth = 180;
            rotateSpeed = 1.5f;
            researchCostMultiplier = 0.05f;
            buildTime = 60f * 9f;
            limitRange(12f);
        }};


        leviathan = new ItemTurret("leviathan"){{
            float brange = range = 400;

            Prov<BulletType> wave = () -> new BasicBulletType(10f, 50){{
                width = 20f;
                height = 24f;
                lifetime = brange / this.speed;
                knockback = 2;
                this.pierce = true;
                this.pierceCap = 3;

                hitEffect = despawnEffect = Fx.hitBulletColor;
                hitColor = backColor = trailColor = Pal.water;
                frontColor = Pal.water;
            }};
            requirements(Category.turret, with(Items.thorium, 100, Items.oxide, 200, Items.metaglass,300, Items.surgeAlloy, 100));

            drawer = new DrawTurret("sealed-"){{
                parts.add(new RegionPart("-rear"){{
                    progress = PartProgress.recoil.curve(Interp.pow2In);
                    under = true;
                    y = -8;
                    moveY = -12;
                }});
                for(int i = 0; i < 3; i++){
                    int finalI = 2 - i;
                    parts.add(new RegionPart("-tentacle"){{
                        progress = PartProgress.warmup.curve(Interp.pow2In);
                        x = 6 + 2*finalI;
                        y = -6 - 4* finalI;
                        moveX = -2*finalI;
                        moveY = 9 - 3*finalI;
                        mirror = true;
                        moveRot = -15f * finalI;
                        moves.add(new PartMove(PartProgress.recoil.curve(Interp.pow2In), 0f, 0f, -15f));
                    }});
                }
                parts.add(new RegionPart("-arm"){{
                    progress = PartProgress.recoil.curve(Interp.pow2In).inv();
                    x = -10;
                    mirror = true;
                    moveX = 4;
                    moveY = -2;
                }});
            }};
            ammo(
                    Items.tungsten, new RailBulletType(){{
                        shootEffect = new MultiEffect(Fx.instShoot);
                        hitEffect = Fx.instHit;
                        pierceEffect = Fx.railHit;
                        smokeEffect = Fx.smokeCloud;
                        pointEffect = TantrosFx.instTrailTungsten;
                        despawnEffect = Fx.instBomb;
                        pointEffectSpace = 20f;
                        damage = 700;
                        buildingDamageMultiplier = 0.2f;
                        pierceDamageFactor = 1f;
                        length = brange;
                        hitShake = 6f;
                        ammoMultiplier = 1f;
                        bulletWave(this,
                                wave,
                                90,
                                1);
                    }},
                    Items.surgeAlloy, new RailBulletType(){{
                        shootEffect = Fx.instShoot;
                        hitEffect = Fx.instHit;
                        pierceEffect = Fx.railHit;
                        smokeEffect = Fx.smokeCloud;
                        pointEffect = Fx.instTrail;
                        despawnEffect = Fx.instBomb;
                        pointEffectSpace = 20f;
                        damage = 1150;
                        buildingDamageMultiplier = 0.2f;
                        pierceDamageFactor = 1f;
                        length = brange;
                        hitShake = 6f;
                        ammoMultiplier = 1f;
                        bulletWave(this,
                                wave,
                                90,
                                1);
                    }},
                    Items.carbide, new RailBulletType(){{
                        shootEffect = Fx.instShoot;
                        hitEffect = Fx.instHit;
                        pierceEffect = Fx.railHit;
                        smokeEffect = Fx.smokeCloud;
                        pointEffect = TantrosFx.instTrailCarbide;
                        despawnEffect = Fx.instBomb;
                        pointEffectSpace = 20f;
                        damage = 1500;
                        buildingDamageMultiplier = 0.2f;
                        pierceDamageFactor = 1f;
                        length = brange;
                        hitShake = 6f;
                        ammoMultiplier = 1f;
                        bulletWave(this,
                                wave,
                                90,
                                1);
                    }}
            );
            shootSound = Sounds.shootForeshadow;

            targetUnderBlocks = false;
            shake = 1f;
            ammoPerShot = 1;
            shootY = -2;
            size = 5;
            envEnabled |= Env.space;
            reload = 100f;
            recoil = 0f;
            scaledHealth = 180;
            rotateSpeed = 1.5f;
            researchCostMultiplier = 0.05f;
            buildTime = 60f * 9f;
            trackingRange = range * 1.4f;

            shootWarmupSpeed = 0.04f;
            warmupMaintainTime = 60f;
            minWarmup = 0.99f;

            limitRange(12f);
        }};
    }

    public static void bulletWave(BulletType parent, Prov<BulletType> bulletToWave, int count, float spacing){
        for(int i = 0; i < count; i++){
            float angleOffset = i * spacing - (count - 1) * spacing / 2f;
            BulletType bullet = bulletToWave.get();
            bullet.angleOffset += angleOffset;
            parent.spawnBullets.add(bullet);
        }
    }
}
