package tantros.type.blockInput;

import arc.scene.ui.layout.Table;
import mindustry.gen.Icon;
import mindustry.ui.Styles;
import tantros.Tantros;
import tantros.type.buildingState.logic.SignalParserState;
import tantros.world.blocks.BlockExtended;

public class SignalParserInput implements BlockInput{

    @Override
    public void apply(BlockExtended block) {
        block.configurable = true;
    }

    @Override
    public void buildConfiguration(BlockExtended.BuildExtended build, Table table) {
        SignalParserState state = build.getState(SignalParserState.class);
        if(state == null) return;
        table.button(Icon.pencil, Styles.cleari, ()->{
            Tantros.signalParserLogicDialog.show(build, state);
        }).size(40);
    }
}
