package tantros.world.blocks.defense.turrets;

import arc.struct.Seq;
import arc.util.Nullable;
import mindustry.gen.Bullet;
import mindustry.world.blocks.defense.turrets.ItemTurret;

public class OneShotTurret extends ItemTurret {

    public OneShotTurret(String name) {
        super(name);
    }

    public class OneShotTurretBuild extends ItemTurretBuild{
        public Seq<BulletEntry> bullets = new Seq<>();

        @Override
        public void updateTile(){
            super.updateTile();

            bullets.removeAll(b -> !b.bullet.isAdded() || b.bullet.type == null || b.bullet.owner != this);

        }

        @Override
        protected void updateShooting(){
            if(!bullets.any()){
                super.updateShooting();
            }
        }

        @Override
        protected void handleBullet(@Nullable Bullet bullet, float offsetX, float offsetY, float angleOffset){

            if(bullet != null){
                bullets.add(new BulletEntry(bullet, offsetX, offsetY, angleOffset, 0f));

                /*//make sure the length updates to the last set value
                Tmp.v1.trns(rotation, shootY + lastLength).add(x, y);
                bullet.aimX = Tmp.v1.x;
                bullet.aimY = Tmp.v1.y;*/
            }
        }
    }
}
