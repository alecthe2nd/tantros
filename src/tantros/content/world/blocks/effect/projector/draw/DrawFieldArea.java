package tantros.content.world.blocks.effect.projector.draw;

import arc.func.Floatf;
import arc.func.Prov;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.math.Mathf;
import mindustry.content.Liquids;
import mindustry.core.Renderer;
import mindustry.graphics.Layer;
import tantros.content.world.blocks.effect.GenericProjector;
import tantros.content.world.blocks.effect.projector.FieldEmitter;
import tantros.content.world.blocks.effect.projector.ProjectorEmitter;

import static mindustry.Vars.renderer;

public class DrawFieldArea<E extends FieldEmitter<E>> implements DrawEmitter<E>  {

    @Override
    public void drawSelect(E emitter, GenericProjector.GenericProjectorBuild build) {

    }

    @Override
    public void draw(E emitter, GenericProjector.GenericProjectorBuild build) {
            float radius = emitter.range(build);
            int sides = emitter.sides(build);
            float fieldRotation = emitter.fieldRotation(build);
            float hit = emitter.flash(build);

            if(radius > 0.001f){

                Draw.reset();
                Draw.color(emitter.color(build), build.team().color, Mathf.clamp(hit));

                
                if(renderer.animateShields){
                    Draw.z(Layer.shields + 0.001f * hit);
                    Draw.alpha(1);
                    Fill.poly(build.x, build.y, sides, radius, fieldRotation);
                }else{
                    Draw.z(Layer.shields);
                    Lines.stroke(1.5f);
                    Draw.alpha(0.09f + Mathf.clamp(0.08f * hit));
                    Fill.poly(build.x, build.y, sides, radius, fieldRotation);
                    Draw.alpha(1f);
                    Lines.poly(build.x, build.y, sides, radius, fieldRotation);
                    Draw.reset();
                }
            }

        Draw.reset();
    }
}
