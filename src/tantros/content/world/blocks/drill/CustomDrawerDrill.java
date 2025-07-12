package tantros.content.world.blocks.drill;

import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.struct.Seq;
import arc.util.Eachable;
import mindustry.entities.units.BuildPlan;
import mindustry.world.blocks.production.Drill;
import mindustry.world.draw.DrawBlock;
import mindustry.world.draw.DrawDefault;

public class CustomDrawerDrill extends Drill {

    public DrawBlock drawer = new DrawDefault();

    public CustomDrawerDrill(String name) {
        super(name);
    }

    @Override
    public void load(){
        super.load();
        drawer.load(this);
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
    public void getRegionsToOutline(Seq<TextureRegion> out){
        drawer.getRegionsToOutline(this, out);
    }

    public class CustomDrawerDrillBuild extends DrillBuild{

        private float lastResult = 0f;
        private float lastEfficiency = 0f;

        @Override
        public float totalProgress() {
            return timeDrilled;
        }

        @Override
        public float warmup() {
            boolean canValueLower = efficiency <= 1.0E-7F;
            float result = 0f;
            if(canValueLower){
                if(warmup > 1.0E-7F) {
                    result = (this.lastEfficiency > 1.0E-7F)? warmup / this.lastEfficiency: 0f;
                } else{
                    this.lastResult = 0f;
                    this.lastEfficiency = 0f;
                    return 0f;
                }
            } else{
                this.lastEfficiency = efficiency;
                result = warmup / efficiency;
            }

            this.lastResult = Mathf.clamp((canValueLower)? result : Math.max(result, this.lastResult));

            return this.lastResult;
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
