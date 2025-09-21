package tantros.world.blocks.distribution.liquidTransport;

import arc.graphics.g2d.Draw;
import arc.math.Mathf;
import mindustry.Vars;
import mindustry.content.Fx;
import mindustry.entities.Puddles;
import mindustry.gen.Building;
import mindustry.graphics.Drawf;
import mindustry.type.Liquid;
import mindustry.world.Tile;
import mindustry.world.blocks.liquid.LiquidBlock;

public abstract class LiquidTransportBlock extends LiquidBlock {


    /** In game Liquid units transported per second.*/
    public float speed = 20;

    /** Whether this block produces puddles at open ends.*/
    public boolean leaks = false;

    public LiquidTransportBlock(String name) {
        super(name);
    }

    public abstract class LiquidTransportBuild extends LiquidBuild{

        public float smoothLiquid = 0f;

        /**
         * The amount that this building wants to transfer to neighbors this tick.
         * */
        public float idealFlow = 0;

        /**
         * The amount that this building succeeded in transferring this tick.
         */
        public float flow = 0;

        @Override
        public void updateTile() {
            idealFlow = Math.min(liquids.currentAmount(), (speed/60f) * edelta());
            if(idealFlow > 0.0001f){
                this.doFlow();
            }
        }

        public abstract Building next();

        public void flowForward(){
            Liquid liquid = liquids.current();
            Building next = next();
            if (next != null ) {
                flow = moveLiquid(next.getLiquidDestination(this, liquid), liquid, idealFlow);
            } else if (leaks){
                Tile puddleTile = tile.nearby(rotation);
                if(puddleTile != null && !puddleTile.block().solid && !puddleTile.block().hasLiquids) {
                    Puddles.deposit(puddleTile, tile, liquid, idealFlow, true, true);
                    liquids.remove(liquid, idealFlow);
                    flow = idealFlow;
                }
            }
            noSleep();
            smoothLiquid = Mathf.lerpDelta(smoothLiquid,  Mathf.clamp(liquids.currentAmount() / liquidCapacity), 0.05f);
        }

        public abstract void doFlow();

        public float moveLiquid(Building next, Liquid liquid, float idealFlow){
            if (next.team == team && next.block.hasLiquids && liquids.get(liquid) > 0.0F) {
                float trueFlow = Math.min(idealFlow, next.block.liquidCapacity - next.liquids.get(liquid));

                if (trueFlow > 0.0F && next.acceptLiquid(this, liquid)) {
                    next.handleLiquid(this, liquid, trueFlow);
                    liquids.remove(liquid, trueFlow);
                    return trueFlow;
                } else if (!next.block.consumesLiquid(liquid) && next.liquids.currentAmount() / next.block.liquidCapacity > 0.1F && idealFlow > 0.1F) {
                    return applyLiquidReactions(next, liquid, idealFlow);
                }
            }
            return 0;
        }

        /** Applies liquid reactions between two liquids in two separate blocks.*/
        public float applyLiquidReactions(Building next, Liquid liquid, float idealFlow){
            float fx = (x + next.x) / 2.0F;
            float fy = (y + next.y) / 2.0F;
            Liquid other = next.liquids.current();
            if (other.blockReactive && liquid.blockReactive) {
                if ((other.flammability > 0.3F && liquid.temperature > 0.7F) || (liquid.flammability > 0.3F && other.temperature > 0.7F)) {
                    damageContinuous(1);
                    next.damageContinuous(1);
                    if (Mathf.chanceDelta(0.1)) {
                        Fx.fire.at(fx, fy);
                    }
                } else if ((liquid.temperature > 0.7F && other.temperature < 0.55F) || (other.temperature > 0.7F && liquid.temperature < 0.55F)) {
                    float trueFlow = Math.min(liquids.get(liquid), idealFlow);
                    liquids.remove(liquid, trueFlow);
                    if (Mathf.chanceDelta(0.2F)) {
                        Fx.steam.at(fx, fy);
                    }
                    return trueFlow;
                }
            }
            return 0;
        }

        @Override
        public void dumpLiquid(Liquid liquid) {
            this.dumpLiquid(liquid, (this.proximity.size > 0)? this.proximity.size: 1f);
        }


        @Override
        public void dumpLiquid(Liquid liquid, float scaling, int outputDir) {

            int dump = this.cdump;
            if (!(this.liquids.get(liquid) <= 1.0E-4F)) {
                if (!Vars.net.client() && Vars.state.isCampaign() && this.team == Vars.state.rules.defaultTeam) {
                    liquid.unlock();
                }

                for(int i = 0; i < this.proximity.size; ++i) {
                    this.incrementDump(this.proximity.size);
                    float idealFlow = Math.min(liquids.currentAmount(), (speed/60f) * edelta());
                    Building other = this.proximity.get((i + dump) % this.proximity.size);
                    if (outputDir == -1 || (outputDir + this.rotation) % 4 == this.relativeTo(other)) {
                        other = other.getLiquidDestination(this, liquid);
                        if (other != null && other.block.hasLiquids && this.canDumpLiquid(other, liquid) && other.liquids != null) {

                            this.transferLiquid(other, idealFlow / (this.proximity.size - i), liquid);
                        }
                    }
                }

            }
        }

    }
}
