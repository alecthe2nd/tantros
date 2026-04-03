package tantros.world.draw.extended;

import arc.Core;
import arc.graphics.Blending;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.util.Eachable;
import mindustry.entities.units.BuildPlan;
import mindustry.gen.Building;
import mindustry.graphics.Layer;
import mindustry.world.Block;
import mindustry.world.blocks.heat.HeatConsumer;
import mindustry.world.draw.DrawBlock;
import tantros.type.buildingState.InputHeatState;
import tantros.world.blocks.BlockExtended;

public class DrawHeatInputExtended extends DrawBlockExtended {
    public String suffix = "-heat";
    public Color heatColor = new Color(1f, 0.22f, 0.22f, 0.8f);
    public float heatPulse = 0.3f, heatPulseScl = 10f;

    public float heatGlowThreshold = 15f;

    public TextureRegion heat;

    public DrawHeatInputExtended(String suffix){
        this.suffix = suffix;
    }

    public DrawHeatInputExtended(){
    }

    @Override
    public void drawPlan(Block block, BuildPlan plan, Eachable<BuildPlan> list){
    }

    @Override
    public void draw(Building build){
        float z = Draw.z();
        Draw.z(Layer.blockAdditive);
        if(build instanceof BlockExtended.BuildExtended hc){
            InputHeatState heatState = hc.getState(InputHeatState.class);
            if (heatState == null) return;
            float[] side = heatState.sideHeat;
            float threshold = (heatState.consumptionConfig == null)? heatGlowThreshold: heatState.consumptionConfig.heatPerEfficiency;
            for(int i = 0; i < 4; i++){
                if(side[i] > 0){
                    Draw.blend(Blending.additive);
                    Draw.color(heatColor, side[i] / threshold * (heatColor.a * (1f - heatPulse + Mathf.absin(heatPulseScl, heatPulse))));
                    Draw.rect(heat, build.x, build.y, (i + build.rotation) * 90f);
                    Draw.blend();
                    Draw.color();
                }
            }
        }
        Draw.z(z);
    }

    @Override
    public void load(Block block){
        heat = Core.atlas.find(block.name + suffix);
    }
}
