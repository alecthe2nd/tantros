package tantros.world.draw.extended;

import arc.Core;
import arc.graphics.Blending;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.util.Eachable;
import arc.util.Tmp;
import mindustry.entities.units.BuildPlan;
import mindustry.gen.Building;
import mindustry.graphics.Layer;
import mindustry.world.Block;
import mindustry.world.blocks.heat.HeatBlock;
import mindustry.world.draw.DrawBlock;
import tantros.type.buildingState.OutputHeatState;
import tantros.world.blocks.BlockExtended;

public class DrawHeatOutputExtended extends DrawBlockExtended {
    public TextureRegion heat, glow, top1, top2;

    public Color heatColor = new Color(1f, 0.22f, 0.22f, 0.8f);
    public float heatPulse = 0.3f, heatPulseScl = 10f, glowMult = 1.2f;

    public int rotOffset = 0;
    public boolean drawGlow = true;

    public DrawHeatOutputExtended(){}

    public DrawHeatOutputExtended(int rotOffset, boolean drawGlow){
        this.rotOffset = rotOffset;
        this.drawGlow = drawGlow;
    }

    @Override
    public void draw(BlockExtended.BuildExtended build){
        float z = Draw.z();

        OutputHeatState state = build.getState(OutputHeatState.class);
        if(state == null) return;
        for(int i = 0; i < 4; i++){
            if(state.productionConfig.sideOutputs[i] > 0) {
                drawOut(build.x, build.y, (build.rotation + rotOffset + i) * 90, Mathf.mod((build.rotation + rotOffset + i), 4) > 1, (state.productionConfig.heatOutput > 0) ? state.sideHeat[i] / state.productionConfig.heatOutput : 0);
            }
        }
        drawGlow(build.x, build.y, (build.rotation + rotOffset) * 90, (state.productionConfig.heatOutput > 0)? state.heat / state.productionConfig.heatOutput: 0);


        if(build instanceof HeatBlock heater){
            drawOut(build.x, build.y, (build.rotation + rotOffset) * 90, Mathf.mod((build.rotation + rotOffset), 4) > 1, heater.heatFrac());
            drawGlow(build.x, build.y, (build.rotation + rotOffset) * 90, heater.heatFrac());
        }
        Draw.z(z);
    }

    public void drawOut(float x, float y, float rotdeg, boolean useTop1, float heatFrac){
        float z = Draw.z();
        Draw.rect(useTop1 ? top2 : top1, x, y, rotdeg);
        if(heatFrac > 0) {
            Draw.z(Layer.blockAdditive);
            Draw.blend(Blending.additive);
            Draw.color(heatColor, heatFrac * (heatColor.a * (1f - heatPulse + Mathf.absin(heatPulseScl, heatPulse))));
            if (heat.found()) Draw.rect(heat, x, y, rotdeg);
        }
        Draw.blend();
        Draw.color();
        Draw.z(z);
    }

    public void drawGlow(float x, float y, float rotdeg, float heatFrac){
        float z = Draw.z();
        if(heatFrac > 0) {
            Draw.z(Layer.blockAdditive);
            Draw.blend(Blending.additive);
            Draw.color(Tmp.c1.set(heatColor).mul(glowMult), heatFrac * (heatColor.a * (1f - heatPulse + Mathf.absin(heatPulseScl, heatPulse))));
            if (drawGlow && glow.found()) Draw.rect(glow, x, y, rotdeg);
        }
        Draw.blend();
        Draw.color();
        Draw.z(z);
    }

    @Override
    public void drawPlan(Block block, BuildPlan plan, Eachable<BuildPlan> list){
        Draw.rect(Mathf.mod((plan.rotation + rotOffset), 4) > 1 ? top2 : top1, plan.drawx(), plan.drawy(), (plan.rotation + rotOffset) * 90);
    }

    @Override
    public void load(Block block){
        heat = Core.atlas.find(block.name + "-heat");
        glow = Core.atlas.find(block.name + "-glow");
        top1 = Core.atlas.find(block.name + "-top1");
        top2 = Core.atlas.find(block.name + "-top2");
    }

    //TODO currently no icons due to concerns with rotation

}