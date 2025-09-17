package tantros.world.blocks.power;

import mindustry.world.blocks.power.PowerGenerator;

public class PassiveGenerator extends PowerGenerator {

    public PassiveGenerator(String name) {
        super(name);
    }

    public class PassiveGeneratorBuild extends GeneratorBuild{

        public PassiveGeneratorBuild(){
            productionEfficiency = 1.0f;
        }

    }
}
