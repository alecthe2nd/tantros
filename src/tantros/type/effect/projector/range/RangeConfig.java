package tantros.type.effect.projector.range;

import tantros.type.blockConfig.BlockConfig;

public class RangeConfig implements BlockConfig {

    public RangeShape shape;

    public float maxScale;
    public float minScale = 0;

    public RangeConfig(float range){
        this(range, 0, RangeShape.circle);
    }

    public RangeConfig(float range, RangeShape shape){
        this(range, 0, shape);
    }

    public RangeConfig(float range, float minRange){
        this(range, minRange, RangeShape.circle);
    }
    public RangeConfig(float range, float minRange, RangeShape shape){
        this.maxScale = range;
        this.minScale = minRange;
        this.shape = shape;
    }

}
