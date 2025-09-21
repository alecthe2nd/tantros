package tantros.world.blocks.effect;

import arc.Core;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.struct.Seq;
import arc.util.Eachable;
import mindustry.entities.units.BuildPlan;
import mindustry.gen.Building;
import mindustry.gen.Sounds;
import mindustry.graphics.Pal;
import mindustry.ui.Bar;
import mindustry.world.Block;
import mindustry.world.draw.DrawBlock;
import mindustry.world.draw.DrawDefault;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatUnit;
import tantros.world.blocks.distribution.BoostPneumatic;
import tantros.world.meta.TantrosStats;

public class FacingPressureBooster extends Block {

    public Seq<Block> boostables = null;

    public float boost = 1f;

    public float reload = 60f;

    public int pressure_range = 10;

    public DrawBlock drawer = new DrawDefault();

    public FacingPressureBooster(String name) {
        super(name);
        update = true;
        solid = true;
        ambientSound = Sounds.machine;
        sync = true;
        ambientSoundVolume = 0.03f;
        drawArrow = true;
        rotate = true;
    }

    @Override
    public void setStats(){
        super.setStats();
        stats.add(TantrosStats.pressureRange, pressure_range, StatUnit.blocks);
        stats.add(Stat.speedIncrease, "+" + (int)(boost * 100f - 100) + "%");
    }

    @Override
    public void setBars(){
        super.setBars();
        addBar("boost", (FacingPressureBoosterBuild entity) -> new Bar(() -> Core.bundle.format("bar.boost", Mathf.round(Math.max((entity.realBoost() * 100 - 100), 0))), () -> Pal.accent, () -> entity.realBoost() / (boost)));
        addBar("pressure", (FacingPressureBoosterBuild entity) -> new Bar(() -> Core.bundle.format("bar.pressure", Mathf.round(Math.max((entity.pressure()), 0))), () -> Pal.lightishGray, () -> (float) entity.pressure() / pressure_range));
    }

    @Override
    public void drawPlanRegion(BuildPlan plan, Eachable<BuildPlan> list){
        drawer.drawPlan(this, plan, list);
    }

    @Override
    public TextureRegion[] icons(){
        return drawer.finalIcons(this);
    }

    @Override
    public void load(){
        super.load();

        drawer.load(this);
    }

    public class FacingPressureBoosterBuild extends Building implements BoostPneumatic {

        public float realBoost(){
            return boost * efficiency;
        }

        @Override
        public void updateTile() {
            if (efficiency > 0) {
                Building front = this.front();
                if (front instanceof BoostPneumatic boostable && boostable.canReceiveBoost(this)){
                    boostable.passBoost(realBoost(), reload + 1f, (int) (pressure_range * efficiency));
                    consume();
                }
                super.updateTile();
            }
        }

        @Override
        public boolean canReceiveBoost(Building build) {
            return false;
        }

        @Override
        public void passBoost(float boost, float duration, int pressure) {

        }

        @Override
        public int pressure() {
            return (int) (pressure_range * efficiency);
        }

        @Override
        public void draw(){
            drawer.draw(this);
        }

        @Override
        public void drawLight(){
            super.drawLight();
            drawer.drawLight(this);
        }




    }

}
