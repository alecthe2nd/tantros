package tantros.world.draw.wallDrill;

import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.math.geom.Geometry;
import arc.util.Tmp;
import mindustry.gen.Icon;
import mindustry.graphics.Drawf;
import mindustry.graphics.Pal;
import mindustry.type.Item;
import mindustry.world.Tile;
import tantros.type.blockConfig.BoreDrillConfig;
import tantros.world.blocks.BlockExtended;
import tantros.world.blocks.production.ProductionBlock;
import tantros.world.draw.extended.DrawBlockExtended;

import static mindustry.Vars.*;

public class DrawBoreEfficiency extends DrawBlockExtended {

    @Override
    public void drawPlace(int x, int y, int rotation, boolean valid, BlockExtended block) {
        if(! (block instanceof ProductionBlock prod)) return;

        BoreDrillConfig boreConfig = block.getBlockConfig(BoreDrillConfig.class);
        if(boreConfig == null) return;

        Item item = null, invalidItem = null;
        boolean multiple = false;
        int count = 0;

        for(int i = 0; i < block.size; i++){
           block.nearbySide(x, y, rotation, i, Tmp.p1);

            int j = 0;
            Item found = null;
            for(; j < boreConfig.range; j++){
                int rx = Tmp.p1.x + Geometry.d4x(rotation)*j, ry = Tmp.p1.y + Geometry.d4y(rotation)*j;
                Tile other = world.tile(rx, ry);
                if(other != null && other.solid()){
                    Item drop = other.wallDrop();
                    if(drop != null){
                        if(drop.hardness <= boreConfig.tier && (boreConfig.blockedItems == null || !boreConfig.blockedItems.contains(drop))){
                            found = drop;
                            count++;
                        }else{
                            invalidItem = drop;
                        }
                    }
                    break;
                }
            }

            if(found != null){
                //check if multiple items will be drilled
                if(item != found && item != null){
                    multiple = true;
                }
                item = found;
            }

            int len = Math.min(j, boreConfig.range - 1);
            Drawf.dashLine(found == null ? Pal.remove : Pal.placing,
                    Tmp.p1.x * tilesize,
                    Tmp.p1.y *tilesize,
                    (Tmp.p1.x + Geometry.d4x(rotation)*len) * tilesize,
                    (Tmp.p1.y + Geometry.d4y(rotation)*len) * tilesize
            );
        }

        if(item != null){
            float width = block.drawPlaceText(Core.bundle.formatFloat("bar.drillspeed", 60f / prod.productionTime * count, 2), x, y, valid);
            if(!multiple){
                float dx = x * tilesize + block.offset - width/2f - 4f, dy = y * tilesize + block.offset + block.size * tilesize / 2f + 5, s = iconSmall / 4f;
                Draw.mixcol(Color.darkGray, 1f);
                Draw.rect(item.fullIcon, dx, dy - 1, s, s);
                Draw.reset();
                Draw.rect(item.fullIcon, dx, dy, s, s);
            }
        }else if(invalidItem != null){
            block.drawPlaceText(Core.bundle.get("bar.drilltierreq"), x, y, false);
        }
    }
}
