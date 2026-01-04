package tantros.content.blocks;

import arc.graphics.Color;
import arc.struct.Seq;
import mindustry.Vars;
import mindustry.content.Items;
import mindustry.content.Liquids;
import mindustry.type.Category;
import mindustry.world.Block;
import mindustry.world.consumers.ConsumeLiquidFilter;
import mindustry.world.draw.DrawMulti;
import mindustry.world.draw.DrawRegion;
import mindustry.world.meta.Env;
import tantros.world.blocks.effect.FacingPressureBooster;
import tantros.world.blocks.effect.GenericProjector;
import tantros.world.blocks.effect.GroundPenetratingRadar;
import tantros.world.blocks.effect.projector.EnvEmitter;
import tantros.world.blocks.effect.projector.draw.DrawCircleEmitterRange;
import tantros.world.blocks.effect.projector.draw.DrawEnvIconEmitter;
import tantros.world.blocks.effect.projector.draw.DrawFieldArea;
import tantros.world.blocks.effect.projector.draw.DrawMultiEmitter;
import tantros.world.environment.LocalEnv;

import static mindustry.type.ItemStack.with;

public class TantrosEffect {

    public static Block

    deepSonar,
    pneumaticPump,
    hydrogenProjector,
    waterProjector,
    atmosphereProjector
    ;

    public static void load(){

        deepSonar = new GroundPenetratingRadar("deep-sonar"){{
            requirements(Category.effect, with( Items.copper, 10, Items.lead, 10, Items.metaglass, 30, Items.silicon, 20));
            glowColor = outlineColor = Color.valueOf("00ffb2");
            fogRadius = 12;
            envEnabled |= Env.underwater;
            consumePower(6f/60f);
        }};

        pneumaticPump = new FacingPressureBooster("pneumatic-pump"){{
            requirements(Category.effect, with(Items.copper, 5, Items.metaglass, 10, Items.graphite, 7));
            boost = 1.5f;
            pressure_range = 20;
            squareSprite = false;

            hasLiquids = true;
            liquidCapacity = 9f;

            drawer = new DrawMulti(
                    new DrawRegion(),
                    new DrawRegion("-top"){{
                        buildingRotate = true;
                    }}
            );

            regionRotated1 = 1;

            consumeLiquid(Liquids.hydrogen, 0.25f/60f);

        }};

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

        waterProjector = new GenericProjector("water-projector"){{
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
                        env = LocalEnv.with(Liquids.water);
                        sides = 6;
                    }}
            );
        }};

        atmosphereProjector = new GenericProjector("atmosphere-projector"){{
            requirements(Category.effect, with(Items.copper, 15, Items.lead, 30, Items.metaglass, 60, Items.silicon, 25));
            size = 2;

            hasLiquids = true;
            liquidCapacity = 20;

            emitters = Seq.with(
                    new EnvEmitter(){
                        {
                            //
                            range = 14.1421f * Vars.tilesize;
                            fieldRotation = 45f;
                            drawer = new DrawMultiEmitter<>() {{
                                drawers.add(new DrawCircleEmitterRange<>());
                                drawers.add(new DrawEnvIconEmitter());
                                drawers.add(new DrawFieldArea<>());
                            }};
                            sides = 4;
                        }}
            );
            singleLiquid = true;
            consume(new ConsumeLiquidFilter(liquid -> liquid.gas, 10f/60f));

        }};

    }
}
