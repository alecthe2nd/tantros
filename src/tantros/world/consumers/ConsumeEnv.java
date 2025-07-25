package tantros.world.consumers;

import mindustry.gen.Building;
import mindustry.world.consumers.Consume;
import mindustry.world.meta.Stats;
import tantros.TantrosVars;
import tantros.content.world.meta.TantrosStats;
import tantros.world.environment.LocalEnv;

public class ConsumeEnv extends Consume {

    public LocalEnv env;
    public float multiplier = 1f;

    private LocalEnv tmpEnv = new LocalEnv();

    public ConsumeEnv(LocalEnv env){
        this.env = env;
    }

    public ConsumeEnv setMultiplier(float multiplier){
        this.multiplier = multiplier;
        return this;
    }

    @Override
    public float efficiency(Building build){
        tmpEnv.replaceWith(TantrosVars.envIndexer.getLocalEnv(build));
        return (tmpEnv.meets(env))? 1.0f: 0.0f;
    }

    @Override
    public float efficiencyMultiplier(Building build){
        return multiplier;
    }

    @Override
    public void display(Stats stats) {
        stats.add(TantrosStats.requiredEnvironments, TantrosStats.liquidEnvironmentReq(env));
    }


}
