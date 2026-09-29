package tantros.world.draw.extended;

import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Font;
import arc.graphics.g2d.GlyphLayout;
import arc.scene.ui.layout.Scl;
import arc.util.Align;
import arc.util.pooling.Pools;
import mindustry.core.UI;
import mindustry.ui.Fonts;
import tantros.type.buildingState.logic.MessageState;
import tantros.world.blocks.BlockExtended;

import static mindustry.Vars.*;

public class DrawMessage extends DrawBlockExtended{


    @Override
    public void drawSelect(BlockExtended.BuildExtended build) {
        if(renderer.pixelate) return;

        MessageState state = build.getState(MessageState.class);
        if(state == null) return;

        Font font = Fonts.outline;
        GlyphLayout l = Pools.obtain(GlyphLayout.class, GlyphLayout::new);
        boolean ints = font.usesIntegerPositions();
        font.getData().setScale(1 / 4f /Scl.scl(1f));
        font.setUseIntegerPositions(false);

        String text = state.message == null || state.message.isEmpty() ? "[lightgray]" + Core.bundle.get("empty") : UI.formatIcons(state.message.toString());

        l.setText(font, text, Color.white, 90f, Align.left, true);
        float offset = 1f;

        Draw.color(0f, 0f, 0f, 0.2f);
        Fill.rect(build.x, build.y - tilesize/2f - l.height/2f - offset, l.width + offset*2f, l.height + offset*2f);
        Draw.color();
        font.setColor(state.message.isEmpty() ? Color.lightGray : Color.white);
        font.draw(text, build.x - l.width/2f, build.y - tilesize/2f - offset, 90f, Align.left, true);
        font.setUseIntegerPositions(ints);

        font.getData().setScale(1f);

        Pools.free(l);
    }

}
