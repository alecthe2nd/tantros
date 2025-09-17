package tantros.world.blocks.storage;

import arc.graphics.g2d.TextureRegion;
import arc.struct.Seq;
import arc.util.Eachable;
import mindustry.entities.units.BuildPlan;
import mindustry.world.blocks.storage.CoreBlock;
import mindustry.world.draw.DrawBlock;
import tantros.content.world.draw.DrawCore;

public class CustomCoreBlock extends CoreBlock {

    public DrawBlock drawer = new DrawCore();

    public CustomCoreBlock(String name) {
        super(name);
    }

    @Override
    public void drawPlanRegion(BuildPlan plan, Eachable<BuildPlan> list){
        drawer.drawPlan(this, plan, list);
    }

    @Override
    public void getRegionsToOutline(Seq<TextureRegion> out){
        drawer.getRegionsToOutline(this, out);
    }

    @Override
    protected TextureRegion[] icons() {
        return drawer.icons(this);
    }

    public class CustomCoreBuild extends CoreBuild{

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
