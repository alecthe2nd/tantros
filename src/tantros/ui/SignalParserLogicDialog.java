package tantros.ui;

import arc.util.Log;
import mindustry.gen.Tex;
import mindustry.ui.dialogs.BaseDialog;
import tantros.logic.SignalParseDisplay;
import tantros.type.buildConfig.BuildConfigurationUnit;
import tantros.type.buildConfig.SetParseType;
import tantros.type.buildingState.logic.SignalParserState;
import tantros.world.blocks.BlockExtended;

import static arc.Core.bundle;

public class SignalParserLogicDialog extends BaseDialog {

    BlockExtended.BuildExtended current;

    SignalParseDisplay display = new SignalParseDisplay();

    public SignalParserLogicDialog(){
        super(bundle.get("ui.logic.signal-parser.configure", "Configure Signal Parser"));

        shouldPause = true;

        this.cont.table(Tex.button, (t)->{
            display.build(t);
        });

        addCloseButton();
        addCloseListener();

        shown(()->{

        });

        hidden(()->{
            if(current == null) return;
            SetParseType access = BuildConfigurationUnit.get(SetParseType.class);
            access.setParseType(display.type);
            current.configureAny(access);
        });
    }


    public void show(BlockExtended.BuildExtended build, SignalParserState state) {
        current = build;
        this.display.set(state.getParseType());
        show();
    }
}
