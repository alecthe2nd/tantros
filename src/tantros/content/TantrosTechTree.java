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
            copper, lead, steam, coal, metaglass, silicon,
            titanium,
            embark,
            never
            ;

    public static void load(){
        copper = new Objectives.Produce(Items.copper);
        lead = new Objectives.Produce(Items.lead);
        steam = new Objectives.Produce(TantrosLiquids.steam);
        coal = new Objectives.Produce(Items.coal);
        metaglass = new Objectives.Produce(Items.metaglass);
        silicon = new Objectives.Produce(Items.silicon);
        titanium = new Objectives.Produce(Items.titanium);
        embark = new Objectives.OnSector(TantrosSectorPresets.embark);
        never = new Objectives.Produce(Items.dormantCyst);
        Planets.tantros.techTree = TechTree.nodeRoot("tantros", TantrosBlocks.coreShell, true, () -> {
            TechTree.node(TantrosSectorPresets.embark, () -> {
                /*TechTree.node(TantrosSectorPresets.oldReef, Seq.with(new Objectives.SectorComplete(TantrosSectorPresets.embark)), () -> {

                });*/
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
                TechTree.node(TantrosSource.effervescenceCollector, Seq.with(never), () -> {
                    TechTree.node(TantrosDistribution.copperPipeline, Seq.with(metaglass, never), () -> {
                        TechTree.node(TantrosDistribution.copperPipelineRouter, Seq.with(metaglass, never), () -> {
                            TechTree.node(TantrosDistribution.copperLiquidContainer, Seq.with(metaglass, never), () -> {
                                TechTree.node(TantrosDistribution.copperLiquidTank, Seq.with(never), () -> {
                                    TechTree.node(TantrosDistribution.copperLiquidSilo, Seq.with(never), () -> {

                                    });
                                });
                                TechTree.node(TantrosSource.deepBoreDrill, Seq.with(never), () -> {
                                    TechTree.node(TantrosSource.deepLaserDrill, Seq.with(never), () -> {

                                    });
                                });
                            });
                        });
                        TechTree.node(TantrosDistribution.copperPipelineBridge, Seq.with(metaglass, never), () -> {

                        });
                    });
                });
                TechTree.node(TantrosSource.seawaterIntake, Seq.with(never), () -> {
                });

                TechTree.node(TantrosSource.siltSifter, Seq.with(never), () -> {

                });
            });
            TechTree.node(TantrosPower.tidalTurbine, Seq.with(copper, never), () -> {
                TechTree.node(TantrosBlocks.sealed_node, () -> {
                });
                TechTree.node(TantrosProduction.combustionHeater, Seq.with(never), () -> {
                    TechTree.node(TantrosProduction.copperBoiler,() -> {
                        TechTree.node(TantrosPower.steamTurbine, () -> {
                            TechTree.node(TantrosPower.steamDynamo, () -> {

                            });
                        });
                    });
                    TechTree.node(TantrosProduction.hydrogenCatalysisHeater, () -> {

                    });
                });
            });
            TechTree.node(TantrosProduction.metaglassAnnealer, Seq.with(copper, never), () -> {
                TechTree.node(TantrosProduction.siliconPressureSmelter, Seq.with(never), () -> {

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
                            copper, never
                    ), () -> {

                    });
                    TechTree.node(TantrosTurret.thrust, Seq.with(
                            copper, steam
                    ), () -> {

                    });
                    TechTree.node(TantrosTurret.jetstream, Seq.with(copper, never), () -> {

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
                                    TechTree.nodeProduce(Liquids.hydrogen, () -> {

                                    });
                                    TechTree.nodeProduce(Liquids.ozone, () -> {

                                    });
                                    TechTree.nodeProduce(TantrosLiquids.steam, () -> {

                                    });
                                    TechTree.nodeProduce(Liquids.nitrogen, () -> {

                                    });
                                });
                            });
                        });
                    });
                    TechTree.nodeProduce(Items.coal, () -> {
                        TechTree.node(TantrosRecipes.coalDecomposition, Seq.with(
                                new Objectives.Research(TantrosProduction.graphiticDecomposer)
                        ), () -> {
                            TechTree.nodeProduce(Items.graphite, () -> {
                            });

                        });
                    });
                    TechTree.nodeProduce(TantrosItems.bluecyst, () -> {
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
                    });
                    TechTree.nodeProduce(TantrosItems.redcyst, () -> {
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
            TechTree.node(TantrosEffect.deepSonar, Seq.with(never), ()->{

            });
            TechTree.node(TantrosPayload.smallUnitAssembler, Seq.with(never, silicon), ()->{
                TechTree.node(TantrosUnitTypes.delegate, Seq.with(never, silicon), ()->{

                });
            });
        });
    }
}
