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
import mindustry.world.blocks.heat.HeatBlock;
import mindustry.world.draw.DrawBlock;

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
    public void draw(Building build){
        float rotdeg = (build.rotation + rotOffset) * 90;
        Draw.rect(Mathf.mod((build.rotation + rotOffset), 4) > 1 ? top2 : top1, build.x, build.y, rotdeg);

        if(build instanceof HeatBlock heater && heater.heat() > 0){
            Draw.z(Layer.blockAdditive);
            Draw.blend(Blending.additive);
            Draw.color(heatColor, heater.heatFrac() * (heatColor.a * (1f - heatPulse + Mathf.absin(heatPulseScl, heatPulse))));
            if(heat.found()) Draw.rect(heat, build.x, build.y, rotdeg);
            Draw.color(Draw.getColor().mul(glowMult));
            if(drawGlow && glow.found()) Draw.rect(glow, build.x, build.y);
            Draw.blend();
            Draw.color();
        }
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