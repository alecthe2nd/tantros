package tantros.world.draw.extended;

import mindustry.gen.Building;
import mindustry.graphics.Drawf;
import mindustry.graphics.Pal;
import tantros.type.effect.projector.range.RangeConfig;
import tantros.type.buildingState.BuildingState;
import tantros.type.buildingState.logic.Link;
import tantros.type.buildingState.logic.Links;
import tantros.world.blocks.BlockExtended;

import static mindustry.Vars.tilesize;
import static mindustry.Vars.world;

public class DrawLinkConfigureRange<E extends Links & BuildingState> extends DrawBlockExtended{

    public Class<E> linksType;

    public DrawLinkConfigureRange(Class<E> linksType){
        this.linksType = linksType;
    }

    @Override
    public void drawConfigure(BlockExtended.BuildExtended build) {
        RangeConfig range = build.getBlock().getBlockConfig(RangeConfig.class);
        if(range == null) return;
        if(!build.block.privileged){
            Drawf.circles(build.x, build.y, range.maxScale);
        }

        Links links = build.getState(linksType);
        if(links != null) {
            for (Link l : links.getLinks()) {
                Building other = world.build(l.x, l.y);
                if (links.validLink(build, other)) {
                    Drawf.square(other.x, other.y, other.block.size * tilesize / 2f + 1f, Pal.place);
                }
            }
        }
    }
}
