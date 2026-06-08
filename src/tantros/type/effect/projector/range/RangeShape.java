package tantros.type.effect.projector.range;

import arc.func.Cons2;
import arc.func.Cons3;
import arc.func.Cons4;
import arc.func.Func3;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.math.geom.Position;
import arc.math.geom.Rect;
import arc.math.geom.Vec2;
import arc.struct.Seq;
import arc.util.Nullable;
import mindustry.graphics.Drawf;

public abstract class RangeShape{

    public Seq<Vec2> vertices = new Seq<>();

    @Nullable public Func3<Position, Position, Float, Boolean> optimizedCheck = null;

    @Nullable public Cons3<Float, Float, Float> drawDashed = null;

    public static RangeShape circle, square;

    public static void load(){
        circle = new RangeShape() {{

            int sides = 20;
            for(int i = 0; i < sides; i++){
                vertices.add(new Vec2().set(1,0).setAngle((360f / sides) * i));
            }

            optimizedCheck = Position::within;
            drawDashed = (Float x, Float y, Float scale)->{
                Color c = Draw.getColor();
                Drawf.dashCircle(x,y,scale, c);
                Draw.color(c);
            };
        }};

        square = new RangeShape() {{
            vertices.add(new Vec2(-1,-1), new Vec2(1,-1), new Vec2(-1,1), new Vec2(1,1));

            optimizedCheck = (pos1,pos2,scale)->
                    Rect.contains(pos1.getX() - scale/2, pos1.getY() - scale/2, scale, scale, pos2.getX(), pos2.getY());
        }};
    }

}
