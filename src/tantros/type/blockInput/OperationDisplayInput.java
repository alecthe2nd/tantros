package tantros.type.blockInput;

import arc.scene.ui.layout.Table;
import mindustry.gen.Icon;
import mindustry.ui.Styles;
import tantros.Tantros;
import tantros.type.blockConfig.BeamLinkConfig;
import tantros.type.blockConfig.OperationConfig;
import tantros.type.buildingState.OperationSettingState;
import tantros.world.blocks.BlockExtended;

public class OperationDisplayInput implements BlockInput {

    public OperationConfig operationConfig;

    public BeamLinkConfig beamLinkConfig;

    public OperationDisplayInput(OperationConfig operationConfig, BeamLinkConfig beamLinkConfig){
        this.operationConfig = operationConfig;
        this.beamLinkConfig = beamLinkConfig;
    }

    @Override
    public void apply(BlockExtended block) {
        block.configurable = true;
    }

    @Override
    public void buildConfiguration(BlockExtended.BuildExtended build, Table table) {
        OperationSettingState state = build.getState(OperationSettingState.class, operationConfig.operationStateName);
        if (state == null) return;
        table.button(Icon.pencil, Styles.cleari, () -> {
            Tantros.signalOperationDialog.show(build, state, beamLinkConfig);
        }).size(40);
    }
}