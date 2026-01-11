package tantros.world.draw;

import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.util.Time;
import mindustry.gen.Building;
import mindustry.graphics.Layer;
import mindustry.world.draw.DrawBubbles;

public class DrawSurfaceRipples extends DrawBubbles {

    public float layer = Layer.block;

    @Override
    public void draw(Building build){

        Draw.z(this.layer);
        Draw.color(color);
        Draw.alpha(0.5f);

        rand.setSeed(build.id);
        for(int i = 0; i < amount; i++){
            float x = rand.range(spread), y = rand.range(spread);
            float life = 1f - ((Time.time / timeScl + rand.random(recurrence)) % recurrence);

            if(life > 0){
                float rad = (1f - life) * radius;
                if(fill){
                    Fill.circle(build.x + x, build.y + y, rad);
                }else{
                    Lines.stroke((life + strokeMin));
                    Lines.poly(build.x + x, build.y + y, sides, rad);
                }
            }
        }

        Draw.color();
    }
}
