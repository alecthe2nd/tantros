package tantros.content;


import arc.struct.Seq;
import mindustry.content.*;
import mindustry.game.Objectives;
import mindustry.type.SectorPreset;
import tantros.content.blocks.*;
import tantros.content.planets.TantrosSectorPresets;
import tantros.content.recipes.TantrosRecipes;
import tantros.content.world.TantrosLiquids;
import tantros.content.world.TantrosUnitTypes;
import tantros.type.production.Produce;

public class TantrosTechTree {

    public static Objectives.Objective
            copper, lead, graphite, steam,
            embark,
            never
            ;

    public static void load(){
        copper = new Objectives.Produce(Items.copper);
        lead = new Objectives.Produce(Items.lead);
        graphite = new Objectives.Produce(Items.graphite);
        steam = new Objectives.Produce(TantrosLiquids.steam);
        embark = new Objectives.OnSector(TantrosSectorPresets.embark);
        never = new Objectives.Produce(Items.dormantCyst);
        Planets.tantros.techTree = TechTree.nodeRoot("tantros", TantrosBlocks.coreShell, true, () -> {
            TechTree.node(TantrosDistribution.copperDuct, Seq.with(copper, embark), () -> {
                TechTree.node(TantrosDistribution.copperDuctRouter, () -> {
                    TechTree.node(TantrosDistribution.copperOverflowDuct, Seq.with(
                            new Objectives.SectorComplete(TantrosSectorPresets.embark)
                    ),()->{});
                    TechTree.node(TantrosDistribution.copperUnderflowDuct, Seq.with(
                            new Objectives.SectorComplete(TantrosSectorPresets.embark)
                    ),()->{});
                });
                TechTree.node(TantrosDistribution.copperDuctBridge, Seq.with(new Objectives.Produce(Items.lead)), () -> {
                    //TechTree.node(TantrosDistribution.copperDuctUnloader);
                });
            });
            TechTree.node(TantrosPayload.smallUnitAssembler, Seq.with(never), ()->{
                TechTree.node(TantrosUnitTypes.delegate, () -> {
                    TechTree.node(TantrosPayload.smallMechAssemblyModule, ()->{
                        TechTree.node(TantrosUnitTypes.flak, ()->{

                        });
                    });
                    TechTree.node(TantrosPayload.smallExplosiveAssemblyModule, ()->{
                        TechTree.node(TantrosUnitTypes.roach , ()->{
                        });
                    });
                });
            });
            TechTree.node(TantrosSource.mechanicalBore, Seq.with(copper, embark), () -> {
                TechTree.node(TantrosSource.seawaterIntake, Seq.with(never), () -> {

                    TechTree.node(TantrosDistribution.copperPipeline, () -> {
                        TechTree.node(TantrosDistribution.copperPipelineRouter, () -> {
                            TechTree.node(TantrosDistribution.copperLiquidContainer, () -> {
                                TechTree.node(TantrosDistribution.copperLiquidTank, () -> {
                                    TechTree.node(TantrosDistribution.copperLiquidSilo, () -> {

                                    });
                                });
                                TechTree.node(TantrosSource.deepBoreDrill, () -> {
                                    TechTree.node(TantrosSource.deepLaserDrill, () -> {

                                    });
                                });
                            });
                        });
                    });
                });

                TechTree.node(TantrosSource.siltSifter, Seq.with(new Objectives.Produce(Items.lead)), () -> {

                });
            });
            TechTree.node(TantrosPower.tidalTurbine, Seq.with(copper, embark), () -> {
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
            TechTree.node(TantrosProduction.metaglassAnnealer, Seq.with(copper, embark), () -> {
                TechTree.node(TantrosProduction.graphiticDecomposer, Seq.with(never), () -> {
                    TechTree.node(TantrosProduction.siliconPressureSmelter, () -> {

                    });
                    TechTree.node(TantrosProduction.electrolysisChamber, () -> {

                    });
                    TechTree.node(TantrosSource.atmosphereIntakeTower, () -> {

                    });
                });
            });
            TechTree.node(TantrosBlocks.copperBulkhead, Seq.with(copper, embark), () -> {
                TechTree.node(TantrosTurret.bident, Seq.with(copper, embark), () -> {
                    TechTree.node(TantrosTurret.puncture, Seq.with(
                            copper, graphite
                    ), () -> {

                    });
                    TechTree.node(TantrosTurret.thrust, Seq.with(
                            copper, graphite, steam
                    ), () -> {

                    });
                    TechTree.node(TantrosTurret.jetstream, Seq.with(copper, never), () -> {

                    });
                });
                TechTree.node(TantrosBlocks.largeCopperBulkhead, () -> {

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
                        TechTree.nodeProduce(Items.graphite, () -> {
                            TechTree.nodeProduce(Items.silicon, () -> {
                                TechTree.nodeProduce(Items.titanium, () -> {

                                });
                            });
                        });
                    });
                });
            });
            TechTree.node(TantrosEffect.deepSonar,()->{

            });
        });
    }
}
