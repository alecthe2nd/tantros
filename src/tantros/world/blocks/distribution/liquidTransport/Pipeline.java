package tantros.world.blocks.distribution.liquidTransport;

import arc.Core;
import arc.func.Boolf;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.math.geom.Geometry;
import arc.math.geom.Point2;
import arc.struct.Seq;
import arc.util.Nullable;
import arc.util.Tmp;
import mindustry.content.Blocks;
import mindustry.entities.TargetPriority;
import mindustry.entities.units.BuildPlan;
import mindustry.gen.Building;
import mindustry.graphics.Drawf;
import mindustry.graphics.Layer;
import mindustry.input.Placement;
import mindustry.type.Liquid;
import mindustry.world.Block;
import mindustry.world.Tile;
import mindustry.world.blocks.Autotiler;
import mindustry.world.blocks.distribution.ChainedBuilding;
import mindustry.world.blocks.distribution.ItemBridge;
import mindustry.world.blocks.liquid.Conduit;
import mindustry.world.blocks.liquid.LiquidJunction;
import tantros.content.blocks.TantrosDistribution;
import tantros.input.TantrosPlacement;

import static mindustry.Vars.renderer;
import static mindustry.Vars.tilesize;
import static mindustry.type.Liquid.animationFrames;

public class Pipeline extends PipelineBlock implements Autotiler {

    static final float rotatePad = 6, hpad = rotatePad / 2f / 4f;
    static final float[][] rotateOffsets = {{hpad, hpad}, {-hpad, hpad}, {-hpad, -hpad}, {hpad, -hpad}};

    public Color transparentColor = new Color(0.4f, 0.4f, 0.4f, 0.1f);

    public TextureRegion[] topRegions = new TextureRegion[5];
    public TextureRegion[] botRegions = new TextureRegion[5];
    public TextureRegion capRegion;

    /** indices: [rotation] [fluid type] [frame] */
    public TextureRegion[][][] rotateRegions;

    /** If true, the liquid region is padded at corners, so it doesn't stick out. */
    public boolean padCorners = true;

    public @Nullable Block junctionReplacement, bridgeReplacement, rotBridgeReplacement;

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
    public void init() {
        super.init();
        if (junctionReplacement == null) junctionReplacement = Blocks.liquidJunction;
        if (bridgeReplacement == null) bridgeReplacement = TantrosDistribution.copperPipelineBridge;
    }

    @Override
    public Block getReplacement(BuildPlan req, Seq<BuildPlan> plans){
        if(junctionReplacement == null) return this;

        Boolf<Point2> cont = p -> plans.contains(o -> o.x == req.x + p.x && o.y == req.y + p.y && (req.block instanceof Conduit || req.block instanceof LiquidJunction));
        return cont.get(Geometry.d4(req.rotation)) &&
                cont.get(Geometry.d4(req.rotation - 2)) &&
                req.tile() != null &&
                req.tile().block() instanceof Conduit &&
                Mathf.mod(req.build().rotation - req.rotation, 2) == 1 ? junctionReplacement : this;
    }

    @Override
    public void handlePlacementLine(Seq<BuildPlan> plans){
        if(bridgeReplacement == null) return;

        if(rotBridgeReplacement instanceof PipelineBridge duct){
            TantrosPlacement.calculateBridges(plans, duct, false, b -> b instanceof Pipeline);
        }else{
            Placement.calculateBridges(plans, (ItemBridge)bridgeReplacement, false, b -> b instanceof Pipeline);
        }
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

    @Override
    public TextureRegion[] icons(){
        return new TextureRegion[]{botRegions[0], topRegions[0]};
    }

    public class PipelineBuild extends PipelineBlockBuild implements ChainedBuilding{

        public int blendbits, xscl, yscl, blending;
        public @Nullable Building next;
        public @Nullable Pipeline.PipelineBuild nextc;

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

            drawCaps();
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
            super.updateTile();
        }

        @Override
        public void doFlow() {
            flowForward();
        }

        @Override
        public void onProximityUpdate(){

            int[] bits = buildBlending(tile, rotation, null, true);
            blendbits = bits[0];
            xscl = bits[1];
            yscl = bits[2];
            blending = bits[4];
            next = front();
            Building prev = back();
            nextc = next instanceof Pipeline.PipelineBuild d ? d : null;
            super.onProximityUpdate();
        }

        @Override
        public void checkCaps() {
            super.checkCaps();

            //don't render caps on the sides, and the back cap should only render for straight pipes
            cappedEdges[1] = false;
            cappedEdges[2] = cappedEdges[2] && blendbits == 0;
            cappedEdges[3] = false;
        }
    }
}
