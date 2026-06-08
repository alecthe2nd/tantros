package tantros.type.production;

import arc.math.Mathf;
import arc.math.Rand;
import mindustry.content.Bullets;
import mindustry.content.Liquids;
import mindustry.entities.bullet.BulletType;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatValue;
import mindustry.world.meta.StatValues;
import mindustry.world.meta.Stats;
import tantros.type.Resource;
import tantros.world.blocks.production.ProductionBlock;

public class ProduceBulletSpray extends Produce{

    public BulletType bulletType;

    public float cone = 360f, offX = 0, offY = 0,
            velScl = 1, velSclMin = 1,
            lifeScl = 1, lifeSclMin = 1;

    public int bulletCount = 1;

    public ProduceBulletSpray(BulletType bulletType){
        this.bulletType = bulletType;
    }

    @Override
    public void apply(ProductionBlock block) {

    }

    @Override
    public void trigger(ProductionBlock.ProductionBuild build) {
        for(int i = 0; i < bulletCount; i++) {
            bulletType.create(build, build.team(), build.x + offX, build.y + offY, build.rotdeg() + Mathf.range(cone / 2f), -1f, (velSclMin >= 0) ? Mathf.random(velSclMin, velScl) : velScl, (lifeSclMin >= 0) ? Mathf.random(lifeSclMin, lifeScl) : lifeScl, null);
        }
    }

    @Override
    public void update(ProductionBlock.ProductionBuild build) {

    }

    @Override
    public boolean canCraft(ProductionBlock.ProductionBuild build) {
        return true;
    }

    @Override
    public void display(Stats stats, ProductionBlock block) {
    }
}
