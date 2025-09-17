package tantros.world.blocks.distribution.liquidTransport;

import arc.Core;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.geom.Geometry;
import mindustry.gen.Building;
import mindustry.type.Liquid;
import mindustry.world.Edges;

import static mindustry.Vars.renderer;
import static mindustry.type.Liquid.animationFrames;

public class PipelineRouter extends LiquidTransportBlock{



    public TextureRegion capRegion;

    public PipelineRouter(String name) {
        super(name);
        rotate = true;
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

    public class PipelineRouterBuild extends LiquidTransportBuild{

        public boolean[] capped = {false,false,false,false};

        @Override
        public void draw(){
            super.draw();

            for(int r = 0; r < capped.length; r++){
                if(capped[r] && capRegion.found()) Draw.rect(capRegion, x, y, r * 90);
            }
        }

        @Override
        public boolean acceptLiquid(Building source, Liquid liquid){
            return (liquids.current() == liquid || liquids.currentAmount() < 0.2f) &&
                    (Edges.getFacingEdge(source.tile, tile).relativeTo(tile) == rotation);
        }

        @Override
        public void onProximityUpdate(){
            super.onProximityUpdate();

            for(int r = 0; r < capped.length; r++){
                int trns = this.block.size / 2 + 1;
                Building neighbor = this.nearby(Geometry.d4(r).x * trns, Geometry.d4(r).y * trns);
                capped[r] = neighbor == null || neighbor.team != team || !neighbor.block.hasLiquids;
            }
        }

        @Override
        public void updateTile(){
            dumpLiquid(liquids.current());
        }
    }
}
