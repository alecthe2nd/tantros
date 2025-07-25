package tantros.content;


import arc.struct.Seq;
import mindustry.content.*;
import mindustry.game.Objectives;
import tantros.content.blocks.TantrosBlocks;
import tantros.content.blocks.TantrosEffect;
import tantros.content.blocks.TantrosPower;
import tantros.content.blocks.TantrosSource;
import tantros.content.world.TantrosLiquids;

public class TantrosTechTree {

    public static void load(){
        Planets.tantros.techTree = TechTree.nodeRoot("tantros", TantrosBlocks.coreShell, true, () -> {
            TechTree.node(TantrosBlocks.copperDuct, () -> {
                TechTree.node(TantrosBlocks.copperDuctRouter, () -> {
                    TechTree.node(TantrosBlocks.copperDuctBridge, () -> {

                    });
                });
            });
            TechTree.node(TantrosSource.mechanicalBore, () -> {
                TechTree.node(TantrosSource.seawaterIntake, () -> {
                    TechTree.node(Blocks.conduit, () -> {
                        TechTree.node(Blocks.liquidJunction, () -> {
                            TechTree.node(Blocks.liquidRouter, () -> {
                                TechTree.node(Blocks.bridgeConduit, () -> {

                                });
                            });
                        });
                        TechTree.node(TantrosSource.deepBoreDrill, () -> {
                            TechTree.node(TantrosSource.deepLaserDrill, () -> {

                            });
                        });
                    });
                });

                TechTree.node(TantrosSource.siltSifter, () -> {

                });
            });
            TechTree.node(TantrosPower.tidalTurbine, () -> {
                TechTree.node(TantrosBlocks.sealed_node, () -> {
                    TechTree.node(TantrosBlocks.steamTurbine, () -> {

                    });

                });
            });
            TechTree.node(TantrosBlocks.metaglassAnnealer, () -> {
                TechTree.node(TantrosBlocks.graphiticDecomposer, () -> {
                    TechTree.node(TantrosBlocks.siliconPressureSmelter, () -> {

                    });
                    TechTree.node(TantrosBlocks.electrolysisChamber, () -> {
                        TechTree.node(TantrosBlocks.combustionBoiler, Seq.with(new Objectives.Research(Liquids.hydrogen), new Objectives.Research(Liquids.hydrogen)), () -> {

                        });
                    });
                    TechTree.node(TantrosSource.atmosphereIntakeTower, () -> {

                    });
                });
            });
            TechTree.node(TantrosBlocks.bident, () -> {

            });
            TechTree.node(Items.copper, () -> {
                TechTree.node(Items.lead, () -> {
                    TechTree.node(Items.sand, () -> {
                        TechTree.node(Items.metaglass, () -> {
                            TechTree.node(Liquids.water, () -> {
                                TechTree.node(Liquids.hydrogen, () -> {

                                });
                                TechTree.node(Liquids.ozone, () -> {

                                });
                                TechTree.node(TantrosLiquids.steam, () -> {

                                });
                                TechTree.node(Liquids.nitrogen, () -> {

                                });
                            });
                        });
                    });
                    TechTree.node(Items.coal, () -> {
                        TechTree.node(Items.graphite, () -> {
                            TechTree.node(Items.silicon, () -> {
                                TechTree.node(Items.titanium, () -> {

                                });
                                TechTree.node(Items.beryllium, () -> {
                                    TechTree.node(Items.oxide, () -> {

                                    });
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
