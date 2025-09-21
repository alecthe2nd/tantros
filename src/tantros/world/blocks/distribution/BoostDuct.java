package tantros.world.blocks.distribution;

import arc.Core;
import arc.math.Mathf;
import mindustry.gen.Building;
import mindustry.graphics.Pal;
import mindustry.ui.Bar;
import mindustry.world.blocks.distribution.Duct;
import mindustry.world.meta.StatUnit;
import tantros.world.meta.TantrosStats;

public class BoostDuct extends Duct {

    public int max_pressure = 7;

    public BoostDuct(String name) {
        super(name);
    }

    @Override
    public void setStats(){
        super.setStats();

        stats.add(TantrosStats.maxPressure, max_pressure, StatUnit.blocks);

    }

    @Override
    public void setBars(){
        super.setBars();
        addBar("pressure", (BoostDuctBuild entity) -> new Bar(() -> Core.bundle.format("bar.pressure", Mathf.round(Math.max((entity.pressure), 0))), () -> Pal.lightishGray, () -> (float) entity.pressure / max_pressure));
    }

    public class BoostDuctBuild extends DuctBuild implements BoostPneumatic{

        public int pressure = 0;

        public int largestPressure = 0;

        @Override
        public void updateTile() {
            super.updateTile();
            pressure = largestPressure;
            if(this.timeScale > 1.0f){
                Building front = this.front();
                if (front instanceof BoostPneumatic boostable && boostable.canReceiveBoost(this)) {
                    boostable.passBoost(this.timeScale, this.timeScaleDuration, pressure - 1);
                }
            }
            largestPressure = 0;
        }

        @Override
        public boolean canReceiveBoost(Building build) {
            return BoostPneumatic.super.canReceiveBoost(build)
                    //and is not receiving from front side
                    && !(relativeTo(build) == rotation);
        }

        @Override
        public void passBoost(float boost, float duration, int pressure) {
            this.applyBoost(boost, duration);
            this.largestPressure = Math.max(Math.min(pressure, max_pressure), this.largestPressure);
        }

        @Override
        public int pressure() {
            return pressure;
        }
    }
}
