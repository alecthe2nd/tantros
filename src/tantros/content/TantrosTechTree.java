package tantros.content;


import mindustry.content.Blocks;
import mindustry.content.Items;
import mindustry.content.Planets;
import mindustry.content.TechTree;

public class TantrosTechTree {

    public static void load(){
        Planets.tantros.techTree = TechTree.nodeRoot("tantros", Blocks.coreShard, true, () -> {
            TechTree.node(TantrosBlocks.copperDuct, () -> {

            });
            TechTree.node(TantrosBlocks.copperBore, () -> {

            });
            TechTree.node(TantrosBlocks.siltSifter, () -> {

            });
            TechTree.node(TantrosBlocks.sealed_node, () -> {

            });
            TechTree.node(TantrosBlocks.tidal_turbine, () -> {

            });
            TechTree.node(TantrosBlocks.metaglassAnnealer, () -> {

            });
            TechTree.node(TantrosBlocks.graphiticDecomposer, () -> {

            });
            TechTree.node(Items.copper, () -> {
                TechTree.node(Items.lead, () -> {
                    TechTree.node(Items.sand, () -> {
                        TechTree.node(Items.metaglass, () -> {

                        });
                    });
                    TechTree.node(Items.coal, () -> {
                        TechTree.node(Items.graphite, () -> {

                        });
                    });
                });
            });
        });
    }
}
