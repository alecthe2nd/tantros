package tantros.content.world.blocks.distribution;

import arc.Core;
import arc.math.Mathf;
import mindustry.gen.Building;
import mindustry.graphics.Pal;
import mindustry.ui.Bar;
import mindustry.world.blocks.distribution.Duct;
import mindustry.world.meta.StatUnit;
import tantros.content.world.meta.TantrosStats;

public class BoostDuct extends Duct {

    public int pressure_range = 7;

    public BoostDuct(String name) {
        super(name);
    }

    @Override
    public void setStats(){
        super.setStats();

        stats.add(TantrosStats.pressureRange, pressure_range, StatUnit.blocks);

    }

    @Override
    public void setBars(){
        super.setBars();
        addBar("pressure", (BoostDuct.BoostDuctBuild entity) -> new Bar(() -> Core.bundle.format("bar.pressure", Mathf.round(Math.max((entity.pressure), 0))), () -> Pal.lightishGray, () -> (float) entity.pressure / pressure_range));
    }

    public class BoostDuctBuild extends DuctBuild{

        public int pressure = 0;

        @Override
        public void updateTile() {
            if(this.timeScale > 1.0f){
                if(pressure == 0){
                    pressure = pressure_range;
                }
                if(pressure > 1) {
                    Building front = this.front();
                    if (front instanceof BoostDuctBuild duct) {
                        front.applyBoost(this.timeScale, this.timeScaleDuration);
                        duct.pressure = this.pressure - 1;
                    }
                }
            } else {
                pressure = 0;
            }
            super.updateTile();
        }
    }
}
