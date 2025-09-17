package tantros.content.blocks;

import arc.struct.EnumSet;
import mindustry.content.Fx;
import mindustry.content.Items;
import mindustry.content.Liquids;
import mindustry.entities.bullet.BasicBulletType;
import mindustry.entities.bullet.LiquidBulletType;
import mindustry.entities.part.RegionPart;
import mindustry.entities.pattern.ShootAlternate;
import mindustry.graphics.Layer;
import mindustry.graphics.Pal;
import mindustry.type.Category;
import mindustry.world.Block;
import mindustry.world.blocks.defense.turrets.ItemTurret;
import mindustry.world.blocks.defense.turrets.LiquidTurret;
import mindustry.world.consumers.ConsumeCoolant;
import mindustry.world.draw.DrawTurret;
import mindustry.world.meta.BlockFlag;
import tantros.content.world.TantrosLiquids;

import static mindustry.type.ItemStack.with;

public class TantrosTurret {

    public static Block
            bident,
            jetstream
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
            requirements(Category.turret, with(Items.metaglass, 45, Items.lead, 75, Items.copper, 25));
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
    }
}
