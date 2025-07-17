package tantros.content.world.blocks.distribution;

import mindustry.gen.Building;
import mindustry.gen.Teamc;

public interface BoostPneumatic {

    default boolean canReceiveBoost(Building build){
        return build instanceof BoostPneumatic source
                && source.pressure() > 1
                && (!(this instanceof Teamc team) || team.team() == build.team);
    }

    void passBoost(float boost, float duration, int pressure);

    int pressure();
}
