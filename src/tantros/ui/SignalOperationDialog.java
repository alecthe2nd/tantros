package tantros.ui;

import arc.scene.ui.layout.Table;
import mindustry.gen.Tex;
import mindustry.ui.dialogs.BaseDialog;
import tantros.logic.SignalOperationsDisplay;
import tantros.type.blockConfig.BeamLinkConfig;
import tantros.type.buildConfig.BuildConfigurationUnit;
import tantros.type.buildConfig.SetOperation;
import tantros.type.buildingState.OperationSettingState;
import tantros.world.blocks.BlockExtended;

import static arc.Core.bundle;

public class SignalOperationDialog extends BaseDialog {

    BlockExtended.BuildExtended current;

    SignalOperationsDisplay display = new SignalOperationsDisplay();

    public SignalOperationDialog(){
        super(bundle.get("ui.logic.operation.configure", "Configure Operation"));

        shouldPause = true;

        Table table = this.cont.table(Tex.button, (t)->{
            display.build(t);
        }).get();

        addCloseButton();
        addCloseListener();

        shown(()->{
            display.build(table);
        });

        hidden(()->{
            if(current == null) return;
            SetOperation unit = BuildConfigurationUnit.get(SetOperation.class);
            unit.operation = display.operation;
            for(int i = 0; i < unit.paramPriority.length; i++){
                if(i < display.paramPriority.length){
                    unit.paramPriority[i] = display.paramPriority[i];
                } else {
                    unit.paramPriority[i] = 0;
                }
            }
            current.configureAny(unit);
        });
    }


    public void show(BlockExtended.BuildExtended build, OperationSettingState opsState, BeamLinkConfig beamLinkConfig) {
        current = build;
        this.display.set(opsState, beamLinkConfig, build.block.rotate);

        show();
    }
}
