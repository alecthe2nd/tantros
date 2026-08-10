package tantros.type.effect;

import arc.func.Cons;
import arc.struct.Seq;
import tantros.type.blockInput.ItemSelectInput;
import tantros.type.buildConfig.SetItemConfig;
import tantros.type.buildingState.drills.ItemSelectionState;
import tantros.world.blocks.BlockExtended;

public class HasSelectableItems implements BlockEffect{

    public String stateName;

    @Override
    public void update(BlockExtended.BuildExtended build) {

    }

    @Override
    public void apply(BlockExtended block) {
        stateName = block.postStateRequest(
                ItemSelectionState::new,
                "ItemSelection",
                ItemSelectionState.class,
                Seq.with(
                        SetItemConfig.class
                ));
        block.inputs.add(new ItemSelectInput(stateName));
        block.configurable = true;
    }
}
