package tantros.content.blocks;

import arc.struct.Seq;
import mindustry.Vars;
import mindustry.content.Items;
import mindustry.content.Liquids;
import mindustry.type.Category;
import mindustry.world.Block;
import tantros.content.world.blocks.effect.GenericProjector;
import tantros.content.world.blocks.effect.projector.EnvEmitter;
import tantros.content.world.blocks.effect.projector.draw.DrawCircleEmitterRange;
import tantros.content.world.blocks.effect.projector.draw.DrawEnvIconEmitter;
import tantros.content.world.blocks.effect.projector.draw.DrawFieldArea;
import tantros.content.world.blocks.effect.projector.draw.DrawMultiEmitter;
import tantros.world.environment.LocalEnv;

import static mindustry.type.ItemStack.with;

public class TantrosEffect {

    public static Block

    hydrogenProjector
    ;

    public static void load(){

        hydrogenProjector = new GenericProjector("hydrogen-projector"){{
            requirements(Category.effect, with(Items.copper, 2, Items.lead, 6));
            size = 2;
            emitters = Seq.with(
                    new EnvEmitter(){{
                        range = 10 * Vars.tilesize;
                        drawer = new DrawMultiEmitter<>(){{
                            drawers.add(new DrawCircleEmitterRange<>());
                            drawers.add(new DrawEnvIconEmitter());
                            drawers.add(new DrawFieldArea<>());
                        }};
                        env = LocalEnv.with(Liquids.hydrogen);
                        sides = 6;
                    }}
            );
        }};

    }
}
