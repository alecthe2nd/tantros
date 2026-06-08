package tantros.world.draw.extended;

import arc.Core;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.util.Time;
import mindustry.Vars;
import mindustry.gen.Building;
import mindustry.graphics.Layer;
import mindustry.world.Block;
import tantros.type.buildingState.BoilerPressureBuildup;
import tantros.type.buildingState.OutputHeatState;
import tantros.world.blocks.BlockExtended;

public class DrawPressureWarning extends DrawBlockExtended{

    public TextureRegion warning;

    public String defaultName = "tantros-danger";
    public String name;

    public DrawPressureWarning(){
        this("danger");
    }

    public DrawPressureWarning(String name){
        this.name = name;
    }

    @Override
    public void draw(Building build) {
        if(build instanceof BlockExtended.BuildExtended extended){
            BoilerPressureBuildup buildup = extended.getState(BoilerPressureBuildup.class);
            if(buildup == null) return;
            if(buildup.pressure > 0.001f){
                float a = Mathf.clamp(buildup.pressureFrac() * 1.33f) * Mathf.absin(5f,1);
                Draw.alpha(a);
                float z = Draw.z();
                Draw.z(Layer.power+0.1f);
                Draw.rect(warning, extended.x, extended.y);
                Draw.reset();
                Draw.z(z);
            }
        }
    }

    @Override
    public void load(Block block) {
        warning = Core.atlas.find(block.name + "-" + name, defaultName);
    }
}
