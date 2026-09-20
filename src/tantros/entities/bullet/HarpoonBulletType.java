package tantros.entities.bullet;

import arc.Core;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.graphics.g2d.TextureRegion;
import arc.util.Log;
import arc.util.Tmp;
import mindustry.Vars;
import mindustry.entities.bullet.BasicBulletType;
import mindustry.gen.*;
import mindustry.graphics.Layer;

public class HarpoonBulletType extends BasicBulletType {

    public float pullingForce = 0.3f;
    public float pullingScalingForce = 0;

    public TextureRegion cableRegion;

    public HarpoonBulletType(float speed, float damage, String bulletSprite){
        super(speed, damage, bulletSprite);
        this.sticky = true;
    }

    public HarpoonBulletType(float speed, float damage){
        this(speed, damage, "bullet");
    }

    /** For mods. */
    public HarpoonBulletType(){
        this(1f, 1f, "bullet");
    }

    @Override
    public void load(){
        super.load();
        cableRegion = Core.atlas.find(sprite + "-cable", "tantros-harpoon-cable");
    }

    @Override
    public void update(Bullet b) {
        super.update(b);
        boolean hitThisTick = b.fdata == 0 && b.stickyTarget != null;
        //make sure to only hit if we haven't already been marked as hit
        if(hitThisTick) b.fdata = -1;
        //reset if we find ourselves non-stuck for some reason
        if(b.stickyTarget == null) b.fdata = 0;

        if(b.owner instanceof Posc owner && this.ownerValid(owner)) {
            if (b.stickyTarget instanceof Unit target) {
                if (this.canStickUnit(b, target, owner)) {
                    b.keepAlive = true;
                    target.impulseNet(
                            Tmp.v1.set(owner).sub(target).limit(
                                    (pullingForce + (1f - target.dst(b) / range) * pullingScalingForce)
                                            * ((owner instanceof Building build) ? build.edelta() : 1f)
                            )
                    );
                    if(hitThisTick) target.damagePierce(b.damage);
                } else {
                    b.keepAlive = false;
                    b.time = lifetime;
                }
            } else if (b.stickyTarget instanceof Building target) {
                if (this.canStickBuilding(b, target, owner)) {
                    if(hitThisTick) target.damagePierce(b.damage);
                } else {
                    b.keepAlive = false;
                    b.time = lifetime;
                }
            }
        } else {
            b.keepAlive = false;
            b.stickyTarget = null;
            b.time = lifetime;
        }
    }

    @Override
    public void despawned(Bullet b) {
        super.despawned(b);
        if(b.stickyTarget instanceof Healthc target){
            target.damagePierce(b.damage);
        }
    }

    public boolean ownerValid(Entityc owner){
        return (owner instanceof Healthc health && health.isValid()) || owner.isAdded();
    }

    public boolean canStickUnit(Bullet bullet, Unit unit, Posc owner){
        return unit.isValid() && unit.dst2(owner) <= range*range;
    }

    public boolean canStickBuilding(Bullet bullet, Building build, Posc owner){
        return build.isValid() && build.dst2(owner) <= range*range;
    }

    @Override
    public void draw(Bullet b) {
        super.draw(b);
        if(b.owner instanceof Posc owner) {
            float size = Vars.tilesize / 2f;
            if(owner instanceof Building build) size = build.block.size * Vars.tilesize / 2f;
            if(owner instanceof Unit unit) size = unit.hitSize / 2f;
            float z = Draw.z();
            Draw.z(Layer.power);
            Tmp.v1.set(b).sub(owner);
            float scale = size/Tmp.v1.len();
            Tmp.v1.scl(scale);
            Log.info(owner + "|" + size + "|" + scale + "|" + Tmp.v1);
            Tmp.v2.set(owner).add(Tmp.v1);
            Lines.line(cableRegion, b.x, b.y, Tmp.v2.x, Tmp.v2.y, true);
            Draw.z(Layer.turret - 0.1f);
            Lines.line(cableRegion, Tmp.v2.x, Tmp.v2.y, owner.x(), owner.y(), true);
            Draw.z(z);
        }
    }


}
