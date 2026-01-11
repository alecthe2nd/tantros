package tantros.world.blocks.distribution.liquidTransport;

import arc.Core;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.math.geom.Geometry;
import arc.math.geom.Point2;
import arc.struct.Seq;
import mindustry.Vars;
import mindustry.content.Bullets;
import mindustry.content.Fx;
import mindustry.entities.Puddles;
import mindustry.type.Liquid;
import mindustry.world.Tile;
import mindustry.world.blocks.distribution.ChainedBuilding;
import mindustry.world.meta.Env;

public class PipelineVent extends PipelineBlock{

    public Seq<Point2> outlets = Seq.with(
            new Point2(4, 2),
            new Point2(4, 6)
    );

    public TextureRegion ventOpen;
    public TextureRegion ventClosed;

    public PipelineVent(String name) {
        super(name);
        rotate = true;
    }

    @Override
    public void load() {
        super.load();
        ventOpen = Core.atlas.find(name + "-open");
        ventClosed = Core.atlas.find(name + "-closed");
    }

    public class PipelineVentBuild extends PipelineBlockBuild implements ChainedBuilding {

        /** Whether the vent is open and draining.*/
        public boolean open = false;

        @Override
        public void draw() {
            super.draw();

            Draw.rect((open)? ventOpen: ventClosed, x, y, rotation * 90f);
        }

        @Override
        public void updateTile() {
            super.updateTile();

            if(next != null && next.liquids.currentAmount() > block.liquidCapacity * 0.9f){
                open = true;
            }
            if(next == null || (next.liquids.currentAmount() < block.liquidCapacity * 0.1f)){
                open = false;
            }
        }

        @Override
        public void doFlow() {

            if(open){
                flow = idealFlow;
                liquids.remove(liquids.current(), idealFlow);
                for(Point2 point : outlets){
                    Point2 out = point.cpy().rotate(rotation);
                    Liquid liquid = liquids.current();
                    if(liquid.willBoil()){
                        if(Mathf.chanceDelta(0.16f)){
                            liquid.vaporEffect.at(out.x, out.y, liquid.gasColor);
                        }
                    } else if(Vars.state.rules.hasEnv(Env.space)){
                        if(Mathf.chanceDelta(0.11f)){
                            Bullets.spaceLiquid.create(null, this.team(), out.x, out.y, Mathf.range(50f), -1f, Mathf.random(0f, 0.2f), Mathf.random(0.6f, 1f), liquid);
                        }
                    } else {
                        liquid.particleEffect.at(out.x, out.y);
                    }
                }
                Tile puddleTile = this.tile;
                if(puddleTile != null) {
                    Puddles.deposit(puddleTile, tile, liquids.current(), idealFlow / outlets.size, true, true);
                }
            } else {
                flowForward();
            }
        }
    }
}
