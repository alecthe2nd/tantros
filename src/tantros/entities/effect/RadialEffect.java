package tantros.entities.effect;

import arc.func.Cons;
import arc.graphics.Color;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import mindustry.entities.Effect;

public class RadialEffect extends Effect {

    public float radius = 1;

    public int spokes = 4;

    public float radialOffset = 0f;

    public Vec2[] spokePositions;

    public RadialEffect(float life, Cons<EffectContainer> renderer){
        super(life, 50f, renderer);
    }

    @Override
    public void init() {
        super.init();
        spokePositions = new Vec2[spokes];
        for(int spoke = 0; spoke < spokes; spoke++){
            spokePositions[spoke] = new Vec2(
                    Mathf.cosDeg((360f/spokes) * spoke + radialOffset) * radius,
                    Mathf.sinDeg((360f/spokes) * spoke + radialOffset) * radius
            );
        }
    }

    @Override
    public float render(int id, Color color, float life, float lifetime, float rotation, float x, float y, Object data) {
        float outLifetime = 0f;
        for(Vec2 spokePosition: spokePositions) {
            outLifetime = Math.max(super.render(id, color, life, lifetime, rotation, x + spokePosition.x, y + spokePosition.y, data), outLifetime);
        }
        return outLifetime;
    }
}
