package tantros.type.effect.projector.range;

import arc.math.geom.Position;
import mindustry.Vars;
import mindustry.gen.Building;
import tantros.type.blockConfig.BlockConfig;
import tantros.world.blocks.BlockExtended;

public class RangeConfig implements BlockConfig {

    public String rangeStateName = "0-defaultRange";

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

    private boolean inRangeRaw(Position pos, Position posToCheck, float range) {
        return (shape.optimizedCheck != null)? shape.optimizedCheck.get(pos, posToCheck, range): false;
    }

    private boolean inRangeBuilding(Position pos, Building posToCheck, float range) {
        return this.inRangeRaw(pos, posToCheck, range + posToCheck.block.size * Vars.tilesize / 2.0F);
    }

    public boolean inRange(Position pos, Position posToCheck, float range) {
        return this.inRangeRaw(pos, posToCheck, range);
    }

    public boolean inRange(Position pos, Position posToCheck) {
        return this.inRangeRaw(pos, posToCheck, maxScale);
    }

    public boolean inRange(Position pos, Building posToCheck) {
        return this.inRangeBuilding(pos, posToCheck, maxScale);
    }

    public boolean inRange(Position pos, Building posToCheck, float range) {
        return this.inRangeBuilding(pos, posToCheck, range);
    }

}
