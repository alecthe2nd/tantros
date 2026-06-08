package tantros.graphics;

import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.math.geom.Position;
import arc.math.geom.Vec2;
import arc.struct.FloatSeq;
import arc.struct.Seq;
import mindustry.graphics.Drawf;

import static arc.graphics.g2d.Draw.rect;
import static arc.graphics.g2d.Draw.vert;
import static mindustry.Vars.renderer;

public class TantrosDraw {

    /** Draws a sprite that should be light-wise correct, when rotated. Provided sprite must be symmetrical in shape. Extended from Anuken's Drawf class.*/
    public static void spinSprite(TextureRegion region, float x, float y, float r, float symmetryAngle){
        float a = Draw.getColorAlpha();
        r = Mathf.mod(r, symmetryAngle);
        Draw.rect(region, x, y, r);
        Draw.alpha(r / symmetryAngle*a);
        Draw.rect(region, x, y, r - symmetryAngle);
        Draw.alpha(a);
    }

    public static void bubble(float x, float y, float lifetime){
        rect(renderer.bubbles[Math.min((int)(renderer.bubbles.length * Mathf.curveMargin(lifetime, 0.11f, 0.06f)), renderer.bubbles.length - 1)], DrawPsuedoParrallax.xHeight(x, lifetime * 20), DrawPsuedoParrallax.yHeight(y, lifetime * 20));
    }

    public static FloatSeq tmpVertices = new FloatSeq();

    public static void poly(Seq<? extends Position> vertices){
        tmpVertices.clear();
        for(Position vertex: vertices){
            tmpVertices.add(vertex.getX(), vertex.getY());
        }
        Fill.poly(tmpVertices);
    }

    public static void poly(Seq<? extends Position> vertices, float offsetx, float offsety, float scl){
        for(int i = 0; i < vertices.size; i++){
            Position current = vertices.get(i);
            Position next = i == vertices.size - 1 ? vertices.get(0) : vertices.get(i + 1);
            Lines.line(current.getX() * scl + offsetx, current.getY() * scl + offsety, next.getX() * scl + offsetx, next.getY() * scl + offsety);
        }
    }

    public static void dashPoly(Seq<? extends Position> vertices, float offsetx, float offsety, float scl){
        for(int i = 0; i < vertices.size; i++){
            Position current = vertices.get(i);
            Position next = i == vertices.size - 1 ? vertices.get(0) : vertices.get(i + 1);
            Color c = Draw.getColor();
            Drawf.dashLine(c, current.getX() * scl + offsetx, current.getY() * scl + offsety, next.getX() * scl + offsetx, next.getY() * scl + offsety);
            Draw.color(c);
        }
    }
}
