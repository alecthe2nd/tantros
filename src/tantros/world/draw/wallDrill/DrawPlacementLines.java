package tantros.world.draw.wallDrill;

import arc.Core;
import arc.graphics.g2d.Draw;
import arc.math.geom.Geometry;
import arc.util.Tmp;
import mindustry.gen.Icon;
import mindustry.graphics.Drawf;
import mindustry.graphics.Pal;
import mindustry.world.Tile;
import tantros.type.blockConfig.BoreDrillConfig;
import tantros.world.blocks.BlockExtended;
import tantros.world.blocks.production.ProductionBlock;
import tantros.world.draw.extended.DrawBlockExtended;

import static mindustry.Vars.tilesize;
import static mindustry.Vars.world;

public class DrawPlacementLines extends DrawBlockExtended {

    @Override
    public void drawPlace(int x, int y, int rotation, boolean valid, BlockExtended block) {
        if(!(block instanceof ProductionBlock)) return;
        BoreDrillConfig boreState = ((ProductionBlock)block).getBlockConfig(BoreDrillConfig.class);

        for(int i = 0; i < block.size; i++) {
            block.nearbySide(x, y, rotation, i, Tmp.p1);

            boolean lineValid = false;
            int j = 0;
            for (; j < boreState.range; j++) {

                int rx = Tmp.p1.x + Geometry.d4x(rotation) * j, ry = Tmp.p1.y + Geometry.d4y(rotation) * j;
                Tile other = world.tile(rx, ry);
                if (other != null && other.solid()) {
                    if (boreState.canMine(other)) {
                        lineValid = true;
                    }
                    break;
                }
            }

            int len = Math.min(j, boreState.range - 1);
            Drawf.dashLine(!lineValid ? Pal.remove : Pal.placing,
                    Tmp.p1.x * tilesize,
                    Tmp.p1.y * tilesize,
                    (Tmp.p1.x + Geometry.d4x(rotation) * len) * tilesize,
                    (Tmp.p1.y + Geometry.d4y(rotation) * len) * tilesize
            );
            if(!lineValid && Core.settings.getBool("drill-assist-indicators")){
                Draw.color(Pal.remove);
                Draw.rect(Icon.cancel.getRegion(),
                        (Tmp.p1.x + Geometry.d4x(rotation) * len) * tilesize,
                        (Tmp.p1.y + Geometry.d4y(rotation) * len) * tilesize);
                Draw.reset();
            }
        }
    }
}
