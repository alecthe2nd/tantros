package tantros.world.blocks.distribution.liquidTransport;

import arc.Core;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.geom.Geometry;
import mindustry.gen.Building;
import mindustry.type.Liquid;
import mindustry.world.Edges;

public abstract class PipelineBlock extends LiquidTransportBlock{

    public TextureRegion capRegion;

    public PipelineBlock(String name) {
        super(name);
    }

    @Override
    public void load() {
        super.load();

        capRegion = Core.atlas.find(name + "-cap");
    }

    public abstract class PipelineBlockBuild extends LiquidTransportBuild{

        public boolean[] cappedEdges = new boolean[]{false, false, false, false};

        public Building next;

        @Override
        public boolean acceptLiquid(Building source, Liquid liquid){
            return (liquids.current() == liquid || liquids.currentAmount() < 0.2f) &&
                    (!rotate || Edges.getFacingEdge(source.tile, tile).relativeTo(tile) == rotation);
        }

        @Override
        public Building next() {
            return next;
        }

        public void checkCaps(){

            int trns = this.block.size / 2 + 1;
            for(int i = 0; i < cappedEdges.length; i++){
                int rot = (rotation + i) % 4;
                Building neighbor = this.nearby(Geometry.d4(rot).x * trns, Geometry.d4(rot).y * trns);
                cappedEdges[i] = neighbor == null || neighbor.team != team || !neighbor.block.hasLiquids;
            }
        }

        @Override
        public void onProximityUpdate(){
            super.onProximityUpdate();
            next = front();
            this.checkCaps();
        }

        @Override
        public void draw() {
            super.draw();
            drawCaps();
        }

        public void drawCaps(){
            if(capRegion.found()) {
                for (int i = 0; i < 4; i++) {
                    if (cappedEdges[i]) Draw.rect(capRegion, x, y, rotdeg() + (i * 90));
                }
            }
        }
    }
}
