package tantros.world.blocks.distribution.liquidTransport;

import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.math.geom.Geometry;
import arc.util.Nullable;
import arc.util.Time;
import arc.util.Tmp;
import mindustry.content.Fx;
import mindustry.entities.Puddles;
import mindustry.entities.TargetPriority;
import mindustry.gen.Building;
import mindustry.graphics.Drawf;
import mindustry.graphics.Layer;
import mindustry.type.Liquid;
import mindustry.world.Block;
import mindustry.world.Tile;
import mindustry.world.blocks.Autotiler;
import mindustry.world.blocks.distribution.ChainedBuilding;
import mindustry.world.blocks.distribution.Duct;
import mindustry.world.blocks.liquid.LiquidBlock;

import static mindustry.Vars.renderer;
import static mindustry.Vars.tilesize;
import static mindustry.type.Liquid.animationFrames;

public class Pipeline extends LiquidTransportBlock implements Autotiler {

    static final float rotatePad = 6, hpad = rotatePad / 2f / 4f;
    static final float[][] rotateOffsets = {{hpad, hpad}, {-hpad, hpad}, {-hpad, -hpad}, {hpad, -hpad}};


    /** Whether this block produces puddles at open ends.*/
    public boolean leaks = true;

    public Color transparentColor = new Color(0.4f, 0.4f, 0.4f, 0.1f);

    public TextureRegion[] topRegions = new TextureRegion[5];
    public TextureRegion[] botRegions = new TextureRegion[5];
    public TextureRegion capRegion;

    /** indices: [rotation] [fluid type] [frame] */
    public TextureRegion[][][] rotateRegions;

    /** If true, the liquid region is padded at corners, so it doesn't stick out. */
    public boolean padCorners = true;

    public Pipeline(String name) {
        super(name);
        rotate = true;
        solid = false;
        floating = true;
        underBullets = true;
        conveyorPlacement = true;
        noUpdateDisabled = true;
        priority = TargetPriority.transport;
    }

    @Override
    public boolean blends(Tile tile, int rotation, int otherx, int othery, int otherrot, Block otherblock) {
        //if(!armored){
            return (otherblock.outputsLiquid || (lookingAt(tile, rotation, otherx, othery, otherblock) && otherblock.hasLiquids))
                    && lookingAtEither(tile, rotation, otherx, othery, otherrot, otherblock);
        //}else{
        //    return (otherblock.outputsItems() && blendsArmored(tile, rotation, otherx, othery, otherrot, otherblock)) || (lookingAt(tile, rotation, otherx, othery, otherblock) && otherblock.hasItems);
        //}
    }

    @Override
    public void load() {
        super.load();
        for(int i = 0; i < 5; i++){
            topRegions[i] = Core.atlas.find(name + "-top-" + i);
            botRegions[i] = Core.atlas.find(name + "-bottom-" + i);
        }

        capRegion = Core.atlas.find(name + "-cap");

        rotateRegions = new TextureRegion[4][2][animationFrames];

        if(renderer != null){
            float pad = rotatePad;
            var frames = renderer.getFluidFrames();

            for(int rot = 0; rot < 4; rot++){
                for(int fluid = 0; fluid < 2; fluid++){
                    for(int frame = 0; frame < animationFrames; frame++){
                        TextureRegion base = frames[fluid][frame];
                        TextureRegion result = new TextureRegion();
                        result.set(base);

                        if(rot == 0){
                            result.setX(result.getX() + pad);
                            result.setHeight(result.height - pad);
                        }else if(rot == 1){
                            result.setWidth(result.width - pad);
                            result.setHeight(result.height - pad);
                        }else if(rot == 2){
                            result.setWidth(result.width - pad);
                            result.setY(result.getY() + pad);
                        }else{
                            result.setX(result.getX() + pad);
                            result.setY(result.getY() + pad);
                        }

                        rotateRegions[rot][fluid][frame] = result;
                    }
                }
            }
        }
    }

    public class PipelineBuild extends LiquidTransportBuild implements ChainedBuilding{

        public int blendbits, xscl, yscl, blending;
        public @Nullable Building next;
        public @Nullable Pipeline.PipelineBuild nextc;

        public boolean capped, backCapped;

        public float smoothLiquid = 0f;

        @Override
        public Building next() {
            return next;
        }

        @Override
        public void draw(){
            int r = this.rotation;

            //draw extra ducts facing this one for tiling purposes
            for(int i = 0; i < 4; i++){
                if((blending & (1 << i)) != 0){
                    int dir = r - i;
                    float rot = i == 0 ? rotation : (dir)*90;
                    drawAt(x + Geometry.d4x(dir) * tilesize*0.75f, y + Geometry.d4y(dir) * tilesize*0.75f, 0,  i == 0 ? r : dir, i != 0 ? Autotiler.SliceMode.bottom : Autotiler.SliceMode.top);
                }
            }

            Draw.scl(xscl, yscl);
            drawAt(x, y, blendbits, rotation, Autotiler.SliceMode.none);
            Draw.reset();

            if(capped && capRegion.found()) Draw.rect(capRegion, x, y, rotdeg());
            if(backCapped && capRegion.found()) Draw.rect(capRegion, x, y, rotdeg() + 180);
        }

        @Override
        public void payloadDraw(){
            Draw.rect(fullIcon, x, y);
        }

        protected void drawAt(float x, float y, int bits, int rotation, Autotiler.SliceMode slice){
            float angle = rotation * 90f;

            Draw.z(Layer.blockUnder);
            Draw.rect(sliced(botRegions[bits], slice), x, y, angle);

            int offset = yscl == -1 ? 3 : 0;

            int frame = liquids.current().getAnimationFrame();
            int gas = liquids.current().gas ? 1 : 0;
            float ox = 0f, oy = 0f;
            int wrapRot = (rotation + offset) % 4;
            TextureRegion liquidr = bits == 1 && padCorners ? rotateRegions[wrapRot][gas][frame] : renderer.fluidFrames[gas][frame];

            if(bits == 1 && padCorners){
                ox = rotateOffsets[wrapRot][0];
                oy = rotateOffsets[wrapRot][1];
            }

            //the drawing state machine sure was a great design choice with no downsides or hidden behavior!!!
            float xscl = Draw.xscl, yscl = Draw.yscl;
            Draw.scl(1f, 1f);
            Drawf.liquid(sliced(liquidr, slice), x + ox, y + oy, smoothLiquid, liquids.current().color.write(Tmp.c1).a(1f));
            Draw.scl(xscl, yscl);


            Draw.z(Layer.blockUnder + 0.2f);
            Draw.color(transparentColor);
            Draw.rect(sliced(botRegions[bits], slice), x, y, angle);
            Draw.color();
            Draw.rect(sliced(topRegions[bits], slice), x, y, angle);
        }

        @Override
        public boolean acceptLiquid(Building source, Liquid liquid){

            return (liquids.current() == liquid || liquids.currentAmount() < 0.2f)
                    && (tile == null || source == this || (source.relativeTo(tile.x, tile.y) + 2) % 4 != rotation);
        }

        @Override
        public void updateTile(){

            float idealFlow = Math.min(liquids.currentAmount(), (speed/60f) * edelta());
            float trueFlow = 0;

            if(idealFlow > 0.0001f){
                Liquid liquid = liquids.current();

                if (next != null ) {
                    trueFlow = moveLiquid(next.getLiquidDestination(this, liquid), liquid, idealFlow);
                } else if (leaks){
                    Tile puddleTile = tile.nearby(rotation);
                    if(puddleTile != null && !puddleTile.block().solid && !puddleTile.block().hasLiquids) {
                        Puddles.deposit(puddleTile, tile, liquid, idealFlow, true, true);
                        liquids.remove(liquid, idealFlow);
                        trueFlow = idealFlow;
                    }
                }
                noSleep();
                smoothLiquid = Mathf.lerpDelta(smoothLiquid,  Mathf.clamp(liquids.currentAmount() / liquidCapacity), 0.05f);

            }
        }

        @Override
        public void onProximityUpdate(){
            super.onProximityUpdate();

            int[] bits = buildBlending(tile, rotation, null, true);
            blendbits = bits[0];
            xscl = bits[1];
            yscl = bits[2];
            blending = bits[4];
            next = front();
            Building prev = back();
            nextc = next instanceof Pipeline.PipelineBuild d ? d : null;

            capped = next == null || next.team != team || !next.block.hasLiquids;
            backCapped = blendbits == 0 && (prev == null || prev.team != team || !prev.block.hasLiquids);
        }
    }
}
