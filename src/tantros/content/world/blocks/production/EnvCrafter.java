package tantros.content.world.blocks.production;

import mindustry.type.Liquid;
import mindustry.world.blocks.production.GenericCrafter;
import tantros.TantrosVars;
import tantros.world.environment.LocalEnv;

public class EnvCrafter extends GenericCrafter {

    public LocalEnv minimumEnv;

    public LocalEnv localEnv = new LocalEnv();

    public EnvCrafter(String name) {
        super(name);
    }

    public class EnvCrafterBuild extends GenericCrafterBuild{

        @Override
        public boolean shouldConsume() {

            return super.shouldConsume() && (localEnv == null || localEnv.meets(minimumEnv));
        }

        @Override
        public void updateTile() {
            localEnv = TantrosVars.envIndexer.getLocalEnv(this);
            super.updateTile();
        }
    }
}
