package tantros.world.blocks.drill;

import arc.func.Func;
import arc.graphics.g2d.TextureRegion;
import arc.struct.Seq;
import arc.util.Eachable;
import mindustry.entities.units.BuildPlan;
import mindustry.gen.Building;
import mindustry.world.blocks.production.Drill;
import mindustry.world.draw.DrawBlock;
import mindustry.world.draw.DrawDefault;
import tantros.content.world.draw.util.ConstantProvider;
import tantros.content.world.draw.util.NumberProviderc;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class CustomDrawerDrill extends Drill implements NumberProviderc {

    public DrawBlock drawer = new DrawDefault();

    public Map<String,Supplier<Func<Building,Float>>> providerSourceMap = new HashMap<>();

    public CustomDrawerDrill(String name) {
        super(name);
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

    @Override
    public void setNumberSource(String name, Supplier<Func<Building, Float>> source) {
        providerSourceMap.put(name, source);
    }

    @Override
    public Supplier<Func<Building, Float>> getSource(String name) {
        if(!providerSourceMap.containsKey(name) || providerSourceMap.get(name) == null){
            providerSourceMap.put(name, ConstantProvider::new);
        }
        return providerSourceMap.get(name);
    }

    public class CustomDrawerDrillBuild extends DrillBuild implements NumberProviderBuildc{

        public Map<String,Func<Building,Float>> providerMap = new HashMap<>();


        @Override
        public float totalProgress() {
            return timeDrilled;
        }

        @Override
        public float warmup() {
            return warmup;
        }

        @Override
        public void draw(){
            drawer.draw(this);
        }

        @Override
        public void drawLight(){
            super.drawLight();
            drawer.drawLight(this);
        }

        @Override
        public Func<Building, Float> get(String name) {
            if(!providerMap.containsKey(name) || providerMap.get(name) == null){
                providerMap.put(name, getSource(name).get());
            }
            return providerMap.get(name);
        }
    }
}
