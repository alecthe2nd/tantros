package tantros.content.world.blocks.effect;

import arc.graphics.g2d.TextureRegion;
import arc.struct.Seq;
import arc.util.Eachable;
import mindustry.entities.units.BuildPlan;
import mindustry.gen.Building;
import mindustry.logic.Ranged;
import mindustry.world.Block;
import mindustry.world.blocks.ExplosionShield;
import mindustry.world.draw.DrawBlock;
import mindustry.world.draw.DrawDefault;
import mindustry.world.meta.BlockGroup;
import mindustry.world.meta.Env;
import tantros.content.world.blocks.effect.projector.ProjectorEmitter;

public class GenericProjector extends Block {

    public Seq<ProjectorEmitter<?>> emitters = new Seq<>();

    public DrawBlock drawer = new DrawDefault();

    public float minRange = 0f;

    public GenericProjector(String name) {
        super(name);
        solid = true;
        update = true;
        group = BlockGroup.projectors;
        //hasPower = true;
        //hasItems = true;
        //canOverdrive = false;
        //emitLight = true;
        //lightRadius = 50f;
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
    }


}
