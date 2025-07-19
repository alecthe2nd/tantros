package tantros.world.environment;

import arc.math.geom.Position;

public interface LocalEnvEmitter extends Position {

    LocalEnv env();

    float range();

    default boolean inRange(Position pos){
        return this.dst(pos) <= this.range();
    }
}
