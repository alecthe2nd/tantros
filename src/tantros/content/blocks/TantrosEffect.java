package tantros.content.blocks;

import arc.graphics.Color;
import arc.struct.Seq;
import mindustry.Vars;
import mindustry.content.Bullets;
import mindustry.content.Items;
import mindustry.content.Liquids;
import mindustry.content.UnitTypes;
import mindustry.entities.bullet.LiquidBulletType;
import mindustry.graphics.Layer;
import mindustry.type.Category;
import mindustry.world.Block;
import mindustry.world.consumers.ConsumeLiquidFilter;
import mindustry.world.draw.DrawDefault;
import mindustry.world.draw.DrawMulti;
import mindustry.world.draw.DrawPulseShape;
import mindustry.world.draw.DrawRegion;
import mindustry.world.meta.Env;
import tantros.graphics.TantrosPal;
import tantros.type.buildConfig.ProgressTimerConfig;
import tantros.type.effect.IsBuilding;
import tantros.type.effect.projector.mend.HealsInRangeContinuously;
import tantros.type.effect.projector.mend.HealsInRangeWithPulses;
import tantros.type.effect.projector.mend.MendConfig;
import tantros.type.effect.projector.range.HasRange;
import tantros.type.effect.projector.range.RangeConfig;
import tantros.type.production.ProduceBulletSpray;
import tantros.world.blocks.BlockExtended;
import tantros.world.blocks.effect.FacingPressureBooster;
import tantros.world.blocks.effect.GenericProjector;
import tantros.world.blocks.effect.GroundPenetratingRadar;
import tantros.world.blocks.effect.projector.EnvEmitter;
import tantros.world.blocks.effect.projector.draw.DrawCircleEmitterRange;
import tantros.world.blocks.effect.projector.draw.DrawEnvIconEmitter;
import tantros.world.blocks.effect.projector.draw.DrawFieldArea;
import tantros.world.blocks.effect.projector.draw.DrawMultiEmitter;
import tantros.world.blocks.production.ProductionBlock;
import tantros.world.consumers.ConsumeNearbyDamagedBuilding;
import tantros.world.draw.extended.DrawMultiExtended;
import tantros.world.draw.extended.DrawPlacementRange;
import tantros.world.environment.LocalEnv;

import static mindustry.type.ItemStack.with;

public class TantrosEffect {

    public static Block

    deepSonar,
    pneumaticPump,
    hydrogenProjector,
    waterProjector,
    atmosphereProjector,
    mendDispenser,
    mendEmitter
    ;

    public static void load(){

        deepSonar = new GroundPenetratingRadar("deep-sonar"){{
            requirements(Category.effect, with( Items.copper, 10, Items.lead, 10, Items.metaglass, 30, Items.silicon, 20));
            glowColor = outlineColor = TantrosPal.radarLight;
            fogRadius = 15;//12;
            envEnabled |= Env.underwater;
            consumePower(18f/60f);
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

        mendEmitter = new BlockExtended("mend-emitter"){{
            requirements(Category.effect, with(Items.copper, 10, Items.oxide, 20, Items.silicon, 25));
            size = 1;

            drawer = new DrawMultiExtended(
                    new DrawDefault(),
                    new DrawPulseShape(false){{
                        layer = Layer.effect;
                        color = TantrosPal.mendLight;
                        timeScl = 100;
                    }},
                    new DrawPlacementRange(){{
                        dashed = true;
                    }}
            );

            effects.add(
                    new IsBuilding(),
                    new HealsInRangeContinuously(
                            new MendConfig(20f / 60f, MendConfig.MendType.ABSOLUTE),
                            new RangeConfig(40f)
                    ),
                    new HealsInRangeWithPulses(
                            new MendConfig(20, MendConfig.MendType.RELATIVE),
                            new RangeConfig(40f),
                            new ProgressTimerConfig(10 * 60)
                    ).asProgressSource(this)
            );

            consumePower(20f/60f);
        }};
    }
}
