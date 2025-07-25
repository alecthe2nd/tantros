package tantros.content.world.blocks.drill;

import arc.graphics.g2d.TextureRegion;
import arc.struct.Seq;
import arc.util.Eachable;
import mindustry.entities.units.BuildPlan;
import mindustry.world.blocks.production.BeamDrill;
import mindustry.world.draw.DrawBlock;
import mindustry.world.draw.DrawDefault;

public class CustomDrawerBeamDrill extends BeamDrill {

    public DrawBlock drawer = new DrawDefault();


    public CustomDrawerBeamDrill(String name) {
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

    public class CustomDrawerBeamDrillBuild extends BeamDrillBuild{

        @Override
        public float warmup() {
            return warmup;
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
