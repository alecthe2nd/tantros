package tantros.content;


import mindustry.content.*;
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
            TechTree.node(TantrosBlocks.copperBore, () -> {
                TechTree.node(Blocks.conduit, () -> {
                    TechTree.node(Blocks.liquidJunction, () -> {
                        TechTree.node(Blocks.liquidRouter, () -> {
                            TechTree.node(Blocks.bridgeConduit, () -> {

                            });
                        });
                    });
                });
                TechTree.node(TantrosBlocks.siltSifter, () -> {

                });
            });
            TechTree.node(TantrosBlocks.tidal_turbine, () -> {
                TechTree.node(TantrosBlocks.sealed_node, () -> {
                    TechTree.node(TantrosBlocks.steamTurbine, () -> {

                    });

                });
            });
            TechTree.node(TantrosBlocks.metaglassAnnealer, () -> {
                TechTree.node(TantrosBlocks.graphiticDecomposer, () -> {
                    TechTree.node(TantrosBlocks.atmosphereIntake, () -> {
                        TechTree.node(TantrosBlocks.siliconPressureSmelter, () -> {

                        });
                        TechTree.node(TantrosBlocks.combustionBoiler, () -> {

                        });
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
                            });
                            TechTree.node(Liquids.nitrogen, () -> {

                            });
                        });
                    });
                    TechTree.node(Items.coal, () -> {
                        TechTree.node(Items.graphite, () -> {
                        });
                    });
                });
            });
            TechTree.node(TantrosBlocks.deepSonar,()->{

            });
        });
    }
}
