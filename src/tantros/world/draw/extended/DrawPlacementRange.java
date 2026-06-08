package tantros.world.draw.extended;

import arc.graphics.g2d.Draw;
import arc.math.Mathf;
import mindustry.graphics.Pal;
import tantros.graphics.TantrosDraw;
import tantros.type.effect.projector.range.RangeConfig;
import tantros.type.effect.projector.range.RangeState;
import tantros.world.blocks.BlockExtended;

import static mindustry.Vars.tilesize;

public class DrawPlacementRange extends DrawBlockExtended{

    public boolean dashed = false;

    @Override
    public void drawSelect(BlockExtended.BuildExtended build) {
        float r;
        RangeConfig range;
        RangeState rangeState = build.getState(RangeState.class);
        if(rangeState == null){
            range = build.getBlock().getBlockConfig(RangeConfig.class);
            r = range.maxScale;
        } else {
            range = rangeState.config;
            r = rangeState.range();
        }
        if(range == null) return;
        Draw.color(Pal.accent);
        if(dashed){
            if(range.shape.drawDashed != null){
                range.shape.drawDashed.get(build.x, build.y, r);
            } else {
                TantrosDraw.dashPoly(range.shape.vertices, build.x, build.y, r);
            }
        } else {
            TantrosDraw.poly(range.shape.vertices, build.x, build.y, r);
        }
    }

    @Override
    public void drawPlace(int x, int y, int rotation, boolean valid, BlockExtended block) {
        RangeConfig range = block.getBlockConfig(RangeConfig.class);
        if(range == null) return;
        float bx = x*tilesize + block.offset, by = y*tilesize + block.offset;

        Draw.color(Pal.accent);
        if(dashed){
            if(range.shape.drawDashed != null){
                range.shape.drawDashed.get(bx, by, range.maxScale);
                if(!Mathf.zero(range.minScale)){
                    range.shape.drawDashed.get(bx, by, range.minScale);
                }
            } else {
                TantrosDraw.dashPoly(range.shape.vertices, bx, by, range.maxScale);
                if (!Mathf.zero(range.minScale)) {
                    TantrosDraw.dashPoly(range.shape.vertices, bx, by, range.minScale);
                }
            }
        } else {
            TantrosDraw.poly(range.shape.vertices, bx, by, range.maxScale);
            if(!Mathf.zero(range.minScale)){
                TantrosDraw.poly(range.shape.vertices, bx, by, range.minScale);
            }
        }
    }
}
