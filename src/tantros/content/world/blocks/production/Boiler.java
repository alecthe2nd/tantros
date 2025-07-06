package tantros.content.world.blocks.production;

import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.graphics.Pal;
import mindustry.ui.Bar;
import mindustry.world.blocks.power.NuclearReactor;
import mindustry.world.blocks.production.GenericCrafter;

public class Boiler extends GenericCrafter {

    public float stressCapacity = 30f;

    public float pressureDamage = 1f;

    public Boiler(String name) {
        super(name);
        ignoreLiquidFullness = true;
    }

    @Override
    public void setBars(){
        super.setBars();
        addBar("pressure", (Boiler.BoilerBuild entity) -> new Bar("bar.pressure", Pal.lightishGray, () -> entity.pressure / stressCapacity));
    }

    public class BoilerBuild extends GenericCrafterBuild{

        public float pressure = 0.0f;

        @Override
        public void updateTile() {
            super.updateTile();
            float overflow;
            boolean didOverflow = false;
            if (efficiency > 0){
                if(outputLiquids != null){
                    float inc = getProgressIncrease(1f);
                    for(var output : outputLiquids){
                        overflow = Math.max((output.amount * inc) - (liquidCapacity - liquids.get(output.liquid)), 0);
                        if (overflow / (output.amount * inc) > 0.25f){
                            pressure += overflow;
                            didOverflow = true;
                        }
                    }
                }
            }
            pressure = Math.min(Math.max(pressure,0), stressCapacity);

            if (pressure >= stressCapacity){
                damage(pressureDamage);
            }
            if (!didOverflow) {
                pressure += -0.1f;
            }
        }

        @Override
        public void write(Writes write){
            super.write(write);
            write.f(pressure);
        }

        @Override
        public void read(Reads read, byte revision){
            super.read(read, revision);
            pressure = read.f();
        }
    }
}
