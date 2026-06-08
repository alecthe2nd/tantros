package tantros.world.consumers;

import arc.func.Boolf;
import arc.scene.ui.layout.Table;
import mindustry.world.meta.Stats;
import tantros.world.blocks.BlockExtended;

public class ConsumeIf extends ExtendedConsume{

    protected Boolf<BlockExtended.BuildExtended> condition;

    public ConsumeIf(Boolf<BlockExtended.BuildExtended> condition){
        this.condition = condition;
    }

    @Override
    public float efficiency(BlockExtended.BuildExtended build) {
        return condition.get(build)?1f:0f;
    }
}
