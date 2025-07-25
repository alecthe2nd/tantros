package tantros.content.world.blocks.effect;

import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.struct.EnumSet;
import arc.struct.Seq;
import arc.util.Eachable;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.entities.units.BuildPlan;
import mindustry.gen.Building;
import mindustry.logic.Ranged;
import mindustry.type.Liquid;
import mindustry.world.Block;
import mindustry.world.blocks.ExplosionShield;
import mindustry.world.draw.DrawBlock;
import mindustry.world.draw.DrawDefault;
import mindustry.world.meta.BlockFlag;
import mindustry.world.meta.BlockGroup;
import tantros.content.world.blocks.effect.projector.ProjectorEmitter;

public class GenericProjector extends Block {

    public Seq<ProjectorEmitter<?>> emitters = new Seq<>();

    public DrawBlock drawer = new DrawDefault();

    public float minRange = 0f;

    /*
     * Whether this projector should accept only one liquid at a time.
     */
    public boolean singleLiquid = false;

    public GenericProjector(String name) {
        super(name);
        solid = true;
        update = true;
        group = BlockGroup.projectors;
        flags = EnumSet.of(BlockFlag.shield);
    }

    @Override
    public void init(){
        float maxRange = minRange;
        for (ProjectorEmitter<?> emitter: emitters){
            maxRange = Math.max(maxRange, emitter.maxRange());
            for (BlockFlag flag: emitter.flags.array){
                flags.with(flag);
            }
        }
        updateClipRadius(maxRange + 3f);
        super.init();
    }

    @Override
    public void load(){
        super.load();

        drawer.load(this);
    }

    @Override
    public void drawPlanRegion(BuildPlan plan, Eachable<BuildPlan> list){
        drawer.drawPlan(this, plan, list);
    }

    @Override
    public TextureRegion[] icons(){
        return drawer.finalIcons(this);
    }

    @Override
    public void getRegionsToOutline(Seq<TextureRegion> out){
        drawer.getRegionsToOutline(this, out);
    }

    public class GenericProjectorBuild extends Building implements Ranged, ExplosionShield {


        public float warmup = 0f;


        @Override
        public void updateTile() {
            super.updateTile();

            warmup = Mathf.lerpDelta(warmup, efficiency, 0.1f);

            for(ProjectorEmitter<?> emitter: emitters){
                emitter.effect(this);
            }
        }

        @Override
        public void draw(){
            drawer.draw(this);
            for(ProjectorEmitter<?> emitter: emitters){
                emitter.draw(this);
            }
        }

        @Override
        public void drawLight(){
            super.drawLight();
            drawer.drawLight(this);
        }

        @Override
        public float range() {
            float range = minRange * efficiency;
            for(ProjectorEmitter<?> emitter: emitters){
                range = Math.max(emitter.range(this), range);
            }
            return range;
        }

        @Override
        public void drawSelect(){
            for(ProjectorEmitter<?> emitter: emitters){
                emitter.drawSelect(this);
            }
        }

        @Override
        public boolean absorbExplosion(float x, float y, float damage) {
            for(ProjectorEmitter<?> emitter: emitters){
                if(emitter.absorbExplosion(x,y,damage)) return true;
            }
            return false;
        }

        public float warmup(){
            return warmup;
        }


        @Override
        public boolean acceptLiquid(Building source, Liquid liquid) {
            return super.acceptLiquid(source, liquid) && (!singleLiquid || this.liquids.currentAmount() < 0.0001 || liquid == this.liquids.current());
        }

        @Override
        public void write(Writes write) {
            super.write(write);
            write.f(warmup);
        }

        @Override
        public void read(Reads read, byte revision){
            super.read(read, revision);
            warmup = read.f();
        }
    }


}
