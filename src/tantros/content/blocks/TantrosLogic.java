package tantros.content.blocks;

import arc.graphics.Color;
import mindustry.Vars;
import mindustry.content.Items;
import mindustry.entities.part.RegionPart;
import mindustry.graphics.Layer;
import mindustry.type.Category;
import mindustry.world.Block;
import mindustry.world.draw.DrawBlockParts;
import mindustry.world.draw.DrawDefault;
import mindustry.world.draw.DrawRegion;
import mindustry.world.meta.BuildVisibility;
import tantros.graphics.TantrosPal;
import tantros.logic.operations.Operations;
import tantros.type.blockConfig.BeamLinkConfig;
import tantros.type.blockConfig.OperationConfig;
import tantros.type.blockInput.OperationDisplayInput;
import tantros.type.blockInput.SignalParserInput;
import tantros.type.effect.logic.*;
import tantros.type.effect.projector.range.RangeConfig;
import tantros.type.buildConfig.AddUnitConfig;
import tantros.type.buildingState.logic.OneLink;
import tantros.type.effect.IsBuilding;
import tantros.type.effect.IsWalkable;
import tantros.world.blocks.BlockExtended;
import tantros.world.draw.DrawTeamRegion;
import tantros.world.draw.extended.DrawCommandQueue;
import tantros.world.draw.extended.DrawMessage;
import tantros.world.draw.extended.DrawMultiExtended;
import tantros.world.draw.extended.DrawUnitPointers;
import tantros.world.draw.linking.DrawLinkBeams;
import tantros.world.draw.linking.DrawLinkConfigureRange;
import tantros.world.draw.linking.DrawPlacementRange;

import static mindustry.type.ItemStack.with;

public class TantrosLogic {

    public static Block
    linkingPad,
    unitInstructor,
    buildingSensor,
    buildingInterface,
    signalParser,
    signalMutator,
    signalMerger,
    debugSignalDisplay
    ;

    public static void load(){
        linkingPad = new BlockExtended("linking-pad"){{
            requirements(Category.logic, with(Items.metaglass, 30, Items.silicon, 10));
            drawer = new DrawMultiExtended(
                    new DrawDefault(),
                    new DrawTeamRegion(),
                    //drawPlace
                    new DrawPlacementRange(),
                    //drawConfig
                    new DrawLinkConfigureRange<>(OneLink.class)
            );
            size = 3;

            effect(
                    new UnitLinkSetter<>(
                            OneLink.class,
                            ()-> new OneLink((b)->b.block.configurations.containsKey(AddUnitConfig.class)),
                            new RangeConfig(4*Vars.tilesize)
                    )
            );
            effect(new IsBuilding());
            effect(new IsWalkable());
        }};

        unitInstructor = new BlockExtended("unit-instructor"){{
            requirements(Category.logic, with(Items.oxide, 20, Items.silicon, 40));
            drawer = new DrawMultiExtended(
                    new DrawRegion("-bottom"),
                    new DrawUnitPointers(),
                    new DrawDefault(),
                    new DrawTeamRegion(),
                    new DrawCommandQueue()
            );
            size = 2;

            effect(new IsBuilding());
            effect(new UnitCommandAssigner());
        }};


        buildingSensor = new BlockExtended("building-sensor"){{
            requirements(Category.logic, with(Items.silicon, 20));



            BeamLinkConfig beamConfig = new BeamLinkConfig();
            beamConfig.dir.set(1);
            beamConfig.dir.set(2);
            beamConfig.dir.set(3);
            beamConfig.dir.set(0, false);

            drawer = new DrawMultiExtended(
                    new DrawDefault(),
                    new DrawBlockParts(){{
                        parts.add(new RegionPart("-top1"){{
                            progress = ((PartProgress)(param)->param.rotation).apply((p)->0f,(a,b)->(a < 180)?1f:0f);
                            color = new Color(1f, 1f, 1f, 0f);
                            colorTo = Color.white;
                            y = 4;
                            outline = false;
                            layer = Layer.blockAdditive;
                        }});
                        parts.add(new RegionPart("-top2"){{
                            progress = ((PartProgress)(param)->param.rotation).apply((p)->0f,(a,b)->(a < 180)?0f:1f);
                            color = new Color(1f, 1f, 1f, 0f);
                            colorTo = Color.white;
                            y = 4;
                            outline = false;
                            layer = Layer.blockAdditive;
                        }});
                    }},
                    new DrawLinkBeams(beamConfig){{
                        laserColor1 = TantrosPal.logicLight;
                        laserColor2 = TantrosPal.logic;
                    }}
            );
            size = 1;

            effect(new IsBuilding());
            effect(new SensesBuildings(beamConfig));

            rotate  = true;
            rotateDraw = false;
            rotateDrawEditor = false;
            drawArrow = true;
            noUpdateDisabled = false;

        }};

        debugSignalDisplay = new BlockExtended("debug-signal-display"){{
            requirements(Category.logic, BuildVisibility.sandboxOnly, with());

            targetable = false;
            privileged = true;

            drawer = new DrawMultiExtended(
                    new DrawDefault(),
                    new DrawMessage()
                    );

            effect(new IsBuilding());
            effect(new DisplaysSignals());

        }};

        buildingInterface = new BlockExtended("building-interface"){{
            requirements(Category.logic, with(Items.silicon, 20));

            BeamLinkConfig beamConfig = new BeamLinkConfig();
            beamConfig.dir.set(1);
            beamConfig.dir.set(2);
            beamConfig.dir.set(3);
            beamConfig.dir.set(0, false);

            drawer = new DrawMultiExtended(
                    new DrawDefault(),
                    new DrawBlockParts(){{
                        parts.add(new RegionPart("-top1"){{
                            progress = ((PartProgress)(param)->param.rotation).apply((p)->0f,(a,b)->(a < 180)?1f:0f);
                            color = new Color(1f, 1f, 1f, 0f);
                            colorTo = Color.white;
                            y = 4;
                            outline = false;
                            layer = Layer.blockAdditive;
                        }});
                        parts.add(new RegionPart("-top2"){{
                            progress = ((PartProgress)(param)->param.rotation).apply((p)->0f,(a,b)->(a < 180)?0f:1f);
                            color = new Color(1f, 1f, 1f, 0f);
                            colorTo = Color.white;
                            y = 4;
                            outline = false;
                            layer = Layer.blockAdditive;
                        }});
                    }},
                    new DrawLinkBeams(beamConfig){{
                        laserColor1 = TantrosPal.logicLight;
                        laserColor2 = TantrosPal.logic;
                    }}
            );
            size = 1;

            effect(new IsBuilding());
            effect(new SensesBuildings(beamConfig));

            rotate  = true;
            rotateDraw = false;
            rotateDrawEditor = false;
            noUpdateDisabled = false;

        }};

        signalParser = new BlockExtended("signal-parser"){{
            requirements(Category.logic, with(Items.silicon, 20));

            size = 1;

            BeamLinkConfig beamConfig = new BeamLinkConfig();
            beamConfig.dir.set(0);
            beamConfig.dir.set(1, false);
            beamConfig.dir.set(2, false);
            beamConfig.dir.set(3, false);

            inputs.add(new SignalParserInput());


            drawer = new DrawMultiExtended(
                    new DrawDefault(),
                    new DrawLinkBeams(beamConfig){{
                        laserColor1 = TantrosPal.logicLight;
                        laserColor2 = TantrosPal.logic;
                    }}
            );

            effect(new IsBuilding());
            effect(new ParsesSignals(beamConfig));

            rotate  = true;
            rotateDraw = false;
            rotateDrawEditor = false;
            noUpdateDisabled = false;
        }};

        signalMutator = new BlockExtended("signal-mutator"){{
            requirements(Category.logic, with(Items.silicon, 20));

            size = 1;

            OperationConfig opsConfig = new OperationConfig();
            opsConfig.add((o)->o.params == 1);
            opsConfig.add(Operations.none);

            BeamLinkConfig beamConfig = new BeamLinkConfig();
            beamConfig.dir.set(0);
            beamConfig.dir.set(1, false);
            beamConfig.dir.set(2, false);
            beamConfig.dir.set(3, false);
            this.putBlockConfig(beamConfig);

            inputs.add(new OperationDisplayInput(opsConfig, beamConfig));

            drawer = new DrawMultiExtended(
                    new DrawDefault(),
                    new DrawLinkBeams(beamConfig){{
                        laserColor1 = TantrosPal.logicLight;
                        laserColor2 = TantrosPal.logic;
                    }}
            );

            effect(new IsBuilding());
            effect(new OperatesOnSignals(beamConfig, opsConfig));
        }};

        signalMerger = new BlockExtended("signal-merger"){{
            requirements(Category.logic, with(Items.silicon, 20));

            size = 1;

            OperationConfig opsConfig = new OperationConfig();
            opsConfig.add((o)->o.params == 2);
            opsConfig.add(Operations.none);

            BeamLinkConfig beamConfig = new BeamLinkConfig();
            beamConfig.dir.set(0);
            beamConfig.dir.set(1, false);
            beamConfig.dir.set(2, false);
            beamConfig.dir.set(3, false);
            this.putBlockConfig(beamConfig);

            inputs.add(new OperationDisplayInput(opsConfig, beamConfig));

            drawer = new DrawMultiExtended(
                    new DrawDefault(),
                    new DrawLinkBeams(beamConfig){{
                        laserColor1 = TantrosPal.logicLight;
                        laserColor2 = TantrosPal.logic;
                    }}
            );

            effect(new IsBuilding());
            effect(new OperatesOnSignals(beamConfig, opsConfig));
        }};

        //TODO delete this:
        /*Events.on(EventType.ConfigEvent.class, (e)->
                Log.info("Config event detected at '" + e.tile + "' by player '"+ e.player + "' of value " + e.value + " and type " + ((e.value == null) ? "null" : e.value.getClass()))
        );*/


    }
}
