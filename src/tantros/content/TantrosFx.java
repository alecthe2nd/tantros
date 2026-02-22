package tantros.content;

import arc.math.Mathf;
import mindustry.content.Items;
import mindustry.entities.Effect;
import mindustry.graphics.Drawf;
import mindustry.graphics.Layer;
import mindustry.graphics.Pal;
import tantros.entities.effect.RadialEffect;
import tantros.graphics.DrawPsuedoParrallax;

import static arc.graphics.g2d.Draw.color;
import static arc.graphics.g2d.Draw.rect;
import static arc.math.Angles.randLenVectors;
import static mindustry.Vars.renderer;

public class TantrosFx {

    public static Effect


            parallaxBubble = new Effect(100f, e -> randLenVectors(e.id, 1, e.fin() * 12f, (x, y) -> rect(renderer.bubbles[Math.min((int)(renderer.bubbles.length * Mathf.curveMargin(e.fin(), 0.11f, 0.06f)), renderer.bubbles.length - 1)], DrawPsuedoParrallax.xHeight(e.x + x, e.fin() * 20), DrawPsuedoParrallax.yHeight(e.y + y, e.fin() * 20)))).layer(Layer.flyingUnitLow + 1),

            fourBubbles = new RadialEffect(100f, (e-> parallaxBubble.renderer.get(e))){{
                radius = 10f;
                radialOffset = 45f;
            }}
                    .layer(Layer.flyingUnitLow + 1),

            instTrailTungsten = new Effect(30, e -> {
                for(int i = 0; i < 2; i++){
                    color( Items.tungsten.color);

                    float m = i == 0 ? 1f : 0.5f;

                    float rot = e.rotation + 180f;
                    float w = 15f * e.fout() * m;
                    Drawf.tri(e.x, e.y, w, (30f + Mathf.randomSeedRange(e.id, 15f)) * m, rot);
                    Drawf.tri(e.x, e.y, w, 10f * m, rot + 180f);
                }

                Drawf.light(e.x, e.y, 60f, Pal.bulletYellowBack, 0.6f * e.fout());
            }),

            instTrailCarbide = new Effect(30, e -> {
                for(int i = 0; i < 2; i++){
                    color( Items.carbide.color);

                    float m = i == 0 ? 1f : 0.5f;

                    float rot = e.rotation + 180f;
                    float w = 15f * e.fout() * m;
                    Drawf.tri(e.x, e.y, w, (30f + Mathf.randomSeedRange(e.id, 15f)) * m, rot);
                    Drawf.tri(e.x, e.y, w, 10f * m, rot + 180f);
                }

                Drawf.light(e.x, e.y, 60f, Pal.bulletYellowBack, 0.6f * e.fout());
            })




            ;
}
