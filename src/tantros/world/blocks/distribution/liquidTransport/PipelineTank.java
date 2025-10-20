package tantros.world.blocks.distribution.liquidTransport;

import arc.Core;
import arc.graphics.g2d.TextureRegion;

public class PipelineTank extends PipelineBlock{



    public TextureRegion capRegion;

    public PipelineTank(String name) {
        super(name);
        rotate = false;
    }

    @Override
    public void load() {
        super.load();

        capRegion = Core.atlas.find(name + "-cap");

    }

    @Override
    public boolean rotatedOutput(int x, int y){
        return false;
    }



    public class PipelineRouterBuild extends PipelineBlockBuild{

        @Override
        public void updateTile(){
            super.updateTile();
        }

        @Override
        public void doFlow() {
            dumpLiquid(liquids.current());
        }
    }
}
