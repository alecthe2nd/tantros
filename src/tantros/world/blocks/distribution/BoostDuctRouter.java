package tantros.world.blocks.distribution;

import arc.Core;
import arc.math.Mathf;
import mindustry.gen.Building;
import mindustry.graphics.Pal;
import mindustry.ui.Bar;
import mindustry.world.blocks.distribution.DuctRouter;
import mindustry.world.meta.StatUnit;
import tantros.content.world.meta.TantrosStats;

public class BoostDuctRouter extends DuctRouter {

    public int max_pressure = 7;

    public BoostDuctRouter(String name) {
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
        addBar("pressure", (BoostDuctRouterBuild entity) -> new Bar(() -> Core.bundle.format("bar.pressure", Mathf.round(Math.max((entity.pressure), 0))), () -> Pal.lightishGray, () -> (float) entity.pressure / max_pressure));
    }

    public class BoostDuctRouterBuild extends DuctRouterBuild implements BoostPneumatic{

        public int pressure = 0;

        public int largestPressure = 0;

        @Override
        public void updateTile() {
            super.updateTile();
            pressure = largestPressure;
            if(this.timeScale > 1f) {

                for (int i = 0; i < proximity.size; i++) {
                    Building other = proximity.get(i);
                    int rel = relativeTo(other);

                    if (!(rel == (rotation + 2) % 4) && other.team == team && other instanceof BoostPneumatic boostable && boostable.canReceiveBoost(this)) {
                        boostable.passBoost(this.timeScale, this.timeScaleDuration, pressure - 1);
                    }
                }
            }
            largestPressure = 0;

        }

        @Override
        public boolean canReceiveBoost(Building build) {
            int rel = relativeTo(build);
            return BoostPneumatic.super.canReceiveBoost(build)
                    && rel == (rotation + 2) % 4
                    && build.team == team;
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
