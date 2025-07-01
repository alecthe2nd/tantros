package tantros.content;


import mindustry.content.Blocks;
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
        });
    }
}
