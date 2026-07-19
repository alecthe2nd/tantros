package tantros.world.draw;

import arc.graphics.g2d.Draw;
import arc.util.Log;
import mindustry.gen.Building;
import mindustry.world.draw.DrawBlock;
import mindustry.world.draw.DrawRegion;
import tantros.type.buildingState.CooldownState;
import tantros.world.blocks.BlockExtended;
import tantros.world.blocks.production.ProductionBlock;
import tantros.world.draw.extended.DrawMultiExtended;

public class DrawLoweredCooldown extends DrawMultiExtended {

    public float upperScale = 1f;

    public float lowerScale = 0.1f;

    public float upperBrightness = 1f;

    public float lowerBrightness = 0.1f;

    public DrawLoweredCooldown(String suffix){
        super(new DrawRegion(suffix));
    }

    public DrawLoweredCooldown(DrawBlock... drawBlocks){
        super(drawBlocks);
    }

    @Override
    public void draw(BlockExtended.BuildExtended build) {
        CooldownState cooldownState = null;
        if(build instanceof ProductionBlock.ProductionBuild prod){
            cooldownState = prod.getState(CooldownState.class);
        }
        if(cooldownState == null){
            super.draw(build);
            return;
        }
        float scaleFactor = (1 - cooldownState.cooldown);

        Draw.scl((scaleFactor * (upperScale - lowerScale)) + lowerScale);
        Draw.colorl((scaleFactor * (upperBrightness - lowerBrightness)) + lowerBrightness);
        super.draw(build);
        Draw.color();
        Draw.scl();
    }
}
