package tantros.world.blocks.distribution;

import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import mindustry.gen.Building;
import mindustry.graphics.Pal;
import mindustry.ui.Bar;
import mindustry.world.blocks.distribution.Duct;
import mindustry.world.meta.StatUnit;
import tantros.world.meta.TantrosStats;

public class BoostDuct extends Duct {

    public int max_pressure = 7;
    public TextureRegion[] glowRegions;

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

    @Override
    public void load() {
        super.load();
        this.glowRegions = new TextureRegion[5];

        for(int i = 0; i < 5; ++i) {
            this.glowRegions[i] = Core.atlas.find(this.name + "-glow-" + i, "duct-glow-" + i);
        }
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
        protected void drawAt(float x, float y, int bits, float rotation, SliceMode slice, boolean under) {
            super.drawAt(x, y, bits, rotation, slice, under);
            if(!under && pressure > 0) {
                Draw.color(Color.white);
                Draw.alpha((float) pressure / max_pressure);
                Draw.rect(BoostDuct.this.sliced(BoostDuct.this.glowRegions[bits], slice), x, y, rotation);
                Draw.reset();
            }
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
