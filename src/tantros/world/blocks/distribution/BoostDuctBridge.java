package tantros.world.blocks.distribution;

import arc.Core;
import arc.math.Mathf;
import mindustry.gen.Building;
import mindustry.graphics.Pal;
import mindustry.ui.Bar;
import mindustry.world.blocks.distribution.DuctBridge;
import mindustry.world.meta.StatUnit;
import tantros.content.world.meta.TantrosStats;

public class BoostDuctBridge extends DuctBridge {

    public int max_pressure = 7;

    public BoostDuctBridge(String name) {
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
        addBar("pressure", (BoostDuctBridgeBuild entity) -> new Bar(() -> Core.bundle.format("bar.pressure", Mathf.round(Math.max((entity.pressure), 0))), () -> Pal.lightishGray, () -> (float) entity.pressure / max_pressure));
    }

    public class BoostDuctBridgeBuild extends DuctBridgeBuild implements BoostPneumatic {

        public int pressure = 0;

        public int largestPressure = 0;

        @Override
        public void updateTile() {
            super.updateTile();
            pressure = largestPressure;

            if (this.timeScale > 1){
                if(lastLink instanceof BoostPneumatic boostable && boostable.canReceiveBoost(this)){
                    boostable.passBoost(this.timeScale, this.timeScaleDuration, this.pressure - 1);
                }
                if (lastLink == null){
                    Building front = this.front();
                    if (front instanceof BoostPneumatic boostable && boostable.canReceiveBoost(this)){
                        boostable.passBoost(this.timeScale, this.timeScaleDuration, this.pressure - 1);
                    }
                }
            }
            largestPressure = 0;

        }

        @Override
        public boolean canReceiveBoost(Building build) {
            return BoostPneumatic.super.canReceiveBoost(build)
                    && (this.findLink() != null || (build instanceof DuctBridgeBuild bridge
                                                        && bridge.findLink() == this));
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
