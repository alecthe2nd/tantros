package tantros.world.draw.extended;

import arc.graphics.Color;
import arc.math.Mathf;
import arc.util.Tmp;
import mindustry.Vars;
import mindustry.graphics.Drawf;
import mindustry.graphics.Pal;
import tantros.type.effect.projector.range.RangeConfig;
import tantros.type.effect.projector.range.RangeState;
import tantros.world.blocks.BlockExtended;

import static mindustry.Vars.indexer;
import static mindustry.Vars.tilesize;

public class DrawPlacementBlockTargets extends DrawBlockExtended{

    public Color baseColor = (new Color()).set(Pal.accent);

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
        indexer.eachBlock(build.team, Tmp.r1.setCentered(build.x, build.y, r * 2), b -> range.inRange(build, b), other -> Drawf.selected(other, Tmp.c1.set(baseColor).a(Mathf.absin(4.0F, 1.0F))));
    }

    @Override
    public void drawPlace(int x, int y, int rotation, boolean valid, BlockExtended block) {
        RangeConfig range = block.getBlockConfig(RangeConfig.class);
        if(range == null) return;
        float bx = (float)(x*tilesize) + block.offset, by = (float)(y*tilesize) + block.offset;

        indexer.eachBlock(Vars.player.team(), Tmp.r1.setCentered(bx, by, range.maxScale * 2), b -> range.inRange(Tmp.v1.set(bx, by), b), other -> Drawf.selected(other, Tmp.c1.set(this.baseColor).a(Mathf.absin(4.0F, 1.0F))));
    }
}
