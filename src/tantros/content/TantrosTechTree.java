package tantros.content;


import arc.struct.Seq;
import mindustry.content.*;
import mindustry.game.Objectives;
import tantros.content.blocks.*;
import tantros.content.planets.TantrosSectorPresets;
import tantros.content.recipes.TantrosRecipes;
import tantros.content.world.TantrosItems;
import tantros.content.world.TantrosLiquids;
import tantros.content.world.TantrosUnitTypes;

public class TantrosTechTree {

    public static Objectives.Objective
            copper, lead, steam, metaglass, silicon,
            titanium,
            embark, shallows, polarEdge,
            never
            ;

    public static void load(){
        copper = new Objectives.Produce(Items.copper);
        lead = new Objectives.Produce(Items.lead);
        steam = new Objectives.Produce(TantrosLiquids.steam);
        metaglass = new Objectives.Produce(Items.metaglass);
        silicon = new Objectives.Produce(Items.silicon);
        titanium = new Objectives.Produce(Items.titanium);
        embark = new Objectives.OnSector(TantrosSectorPresets.embark);
        shallows = new Objectives.OnSector(TantrosSectorPresets.shallows);
        polarEdge = new Objectives.OnSector(TantrosSectorPresets.polarEdge);
        never = new Objectives.Produce(Items.dormantCyst);
        Planets.tantros.techTree = TechTree.nodeRoot("tantros", TantrosBlocks.coreShell, true, () -> {
            TechTree.node(TantrosSectorPresets.embark, () -> {
                TechTree.node(TantrosSectorPresets.shallows, Seq.with(new Objectives.SectorComplete(TantrosSectorPresets.embark)), () -> {
                    TechTree.node(TantrosSectorPresets.polarEdge, Seq.with(new Objectives.SectorComplete(TantrosSectorPresets.shallows)), () -> {

                    });
                });
            });
            TechTree.node(TantrosDistribution.copperDuct, Seq.with(copper, embark), () -> {
                TechTree.node(TantrosDistribution.copperDuctRouter, () -> {
                    TechTree.node(TantrosDistribution.copperOverflowDuct, Seq.with(
                            new Objectives.SectorComplete(TantrosSectorPresets.embark)
                    ),()->{});
                    TechTree.node(TantrosDistribution.copperUnderflowDuct, Seq.with(
                            new Objectives.SectorComplete(TantrosSectorPresets.embark)
                    ),()->{});
                });
                TechTree.node(TantrosDistribution.copperDuctBridge, () -> {
                    //TechTree.node(TantrosDistribution.copperDuctUnloader);
                });
            });
            TechTree.node(TantrosSource.copperBore, Seq.with(copper, embark), () -> {
                TechTree.node(TantrosSource.deepBoreDrill, Seq.with(never), () -> {
                    TechTree.node(TantrosSource.deepLaserDrill, Seq.with(never), () -> {

                    });
                });
                TechTree.node(TantrosSource.effervescenceCollector, Seq.with(never), () -> {
                    TechTree.node(TantrosDistribution.copperPipeline, Seq.with(metaglass, never), () -> {
                        TechTree.node(TantrosDistribution.copperPipelineRouter, Seq.with(metaglass, never), () -> {
                            TechTree.node(TantrosDistribution.copperLiquidContainer, Seq.with(metaglass, never), () -> {
                                TechTree.node(TantrosDistribution.copperLiquidTank, Seq.with(never), () -> {
                                    TechTree.node(TantrosDistribution.copperLiquidSilo, Seq.with(never), () -> {

                                    });
                                });
                            });
                        });
                        TechTree.node(TantrosDistribution.copperPipelineBridge, Seq.with(metaglass, never), () -> {

                        });
                        TechTree.node(TantrosDistribution.metaglassPipeline, Seq.with(metaglass, never), () -> {
                            TechTree.node(TantrosDistribution.metaglassPipelineRouter, Seq.with(metaglass, never), () -> {
                                TechTree.node(TantrosDistribution.metaglassLiquidContainer, Seq.with(metaglass, never), () -> {
                                    TechTree.node(TantrosDistribution.metaglassLiquidTank, Seq.with(never), () -> {
                                        TechTree.node(TantrosDistribution.metaglassLiquidSilo, Seq.with(never), () -> {

                                        });
                                    });
                                });
                            });
                            TechTree.node(TantrosDistribution.metaglassPipelineBridge, Seq.with(metaglass, never), () -> {

                            });
                        });
                    });
                });
                TechTree.node(TantrosSource.seawaterIntake, Seq.with(never), () -> {
                });

                TechTree.node(TantrosSource.siltSifter, Seq.with(shallows), () -> {

                });
            });
            TechTree.node(TantrosPower.tidalTurbine, Seq.with(shallows), () -> {
                TechTree.node(TantrosPower.sealedBeamNode, Seq.with(shallows), () -> {
                });
                TechTree.node(TantrosProduction.cystCombustionHeater, Seq.with(never), () -> {
                    TechTree.node(TantrosProduction.copperBoiler,() -> {
                        TechTree.node(TantrosPower.steamEngine, () -> {
                            TechTree.node(TantrosPower.steamDynamo, () -> {

                            });
                        });
                    });
                    TechTree.node(TantrosProduction.hydrogenCatalysisHeater, () -> {

                    });
                });
            });
            TechTree.node(TantrosProduction.metaglassAnnealer, Seq.with(shallows), () -> {
                TechTree.node(TantrosProduction.siliconPressureSmelter, Seq.with(polarEdge), () -> {

                });
                TechTree.node(TantrosProduction.graphiticDecomposer, Seq.with(never), () -> {
                    TechTree.node(TantrosProduction.electrolysisChamber, Seq.with(never), () -> {

                    });
                    TechTree.node(TantrosSource.atmosphereIntakeTower, Seq.with(never), () -> {

                    });
                });
            });
            TechTree.node(TantrosDefense.copperBulkhead, Seq.with(copper, embark), () -> {
                TechTree.node(TantrosTurret.bident, Seq.with(copper, embark), () -> {
                    TechTree.node(TantrosTurret.puncture, Seq.with(
                            shallows
                    ), () -> {
                        TechTree.node(TantrosTurret.thrust, Seq.with(
                                copper, steam
                        ), () -> {

                        });
                        TechTree.node(TantrosTurret.jetstream, Seq.with(copper, never), () -> {

                        });
                    });
                    TechTree.node(TantrosTurret.lob, Seq.with(
                            polarEdge
                    ), () -> {

                    });
                });
                TechTree.node(TantrosDefense.largeCopperBulkhead, () -> {
                    TechTree.node(TantrosDefense.metaglassBulkhead, Seq.with(metaglass, never), () -> {
                        TechTree.node(TantrosDefense.largeMetaglassBulkhead, Seq.with(metaglass, never), () -> {
                            TechTree.node(TantrosDefense.largeMetaglassBulkheadDoor, () -> {

                            });
                        });
                    });
                });
            });
            TechTree.nodeProduce(Items.copper, () -> {
                TechTree.nodeProduce(Items.oxide, () -> {
                    TechTree.nodeProduce(Items.beryllium, () -> {
                    });
                });
                TechTree.nodeProduce(Items.lead, () -> {
                    TechTree.nodeProduce(Items.sand, () -> {
                        TechTree.node(TantrosRecipes.metaglassAnnealing, Seq.with(
                                new Objectives.Research(TantrosProduction.metaglassAnnealer)
                        ), () -> {
                            TechTree.nodeProduce(Items.metaglass, () -> {
                                TechTree.nodeProduce(Liquids.water, () -> {
                                    TechTree.nodeProduce(TantrosLiquids.steam, () -> {

                                    });
                                });
                            });
                        });
                    });
                    TechTree.node(TantrosRecipes.siliconPressureSmelting, Seq.with(
                            new Objectives.Research(TantrosProduction.siliconPressureSmelter)
                    ), () -> {
                        TechTree.nodeProduce(Items.silicon, () -> {
                            TechTree.node(TantrosRecipes.surgeAnnealing, Seq.with(titanium), () -> {
                                TechTree.nodeProduce(Items.surgeAlloy, () -> {
                                });
                            });
                        });
                    });
                    TechTree.nodeProduce(TantrosItems.bluecyst, () -> {
                        TechTree.nodeProduce(Liquids.hydrogen, () -> {

                        });
                    });
                    TechTree.nodeProduce(TantrosItems.redcyst, () -> {
                        TechTree.nodeProduce(Liquids.ozone, () -> {

                        });
                    });
                    TechTree.nodeProduce(Items.titanium, () -> {
                        TechTree.nodeProduce(Items.thorium, () -> {
                            TechTree.nodeProduce(Items.tungsten, () -> {
                                TechTree.nodeProduce(Items.carbide, () -> {

                                });
                            });
                            TechTree.nodeProduce(Items.phaseFabric, () -> {

                            });
                        });
                        TechTree.nodeProduce(Items.plastanium, () -> {

                        });
                    });
                });
            });
            TechTree.node(TantrosEffect.deepSonar, Seq.with(polarEdge, silicon), ()->{

            });
            TechTree.node(TantrosPayload.delegateFabricator, Seq.with(never, silicon), ()->{
                TechTree.node(TantrosUnitTypes.delegate, Seq.with(never, silicon), ()->{

                });

                TechTree.node(TantrosPayload.smallFrameFabricator, Seq.with(never, silicon), ()-> {
                    TechTree.node(TantrosFrame.flakFrame, Seq.with(never, silicon), () -> {
                        TechTree.node(TantrosUnitTypes.flak, Seq.with(never, silicon), () -> {
                            TechTree.node(TantrosFrame.sherdFrame, Seq.with(never, silicon), () -> {
                                TechTree.node(TantrosUnitTypes.sherd, Seq.with(never, silicon), () -> {
                                    TechTree.node(TantrosFrame.fractoidFrame, Seq.with(never, silicon), () -> {
                                        TechTree.node(TantrosUnitTypes.fractoid, Seq.with(never, silicon), () -> {

                                        });
                                    });
                                });
                            });
                            TechTree.node(TantrosFrame.roachFrame, Seq.with(never, silicon), () -> {
                                TechTree.node(TantrosUnitTypes.roach, Seq.with(never, silicon), () -> {
                                    TechTree.node(TantrosFrame.infestFrame, Seq.with(never, silicon), () -> {
                                        TechTree.node(TantrosUnitTypes.infest, Seq.with(never, silicon), () -> {
                                            TechTree.node(TantrosFrame.invadeFrame, Seq.with(never, silicon), () -> {
                                                TechTree.node(TantrosUnitTypes.invade, Seq.with(never, silicon), () -> {

                                                });
                                            });
                                        });
                                    });
                                });
                            });
                        });
                    });
                });
            });
        });
    }
}
