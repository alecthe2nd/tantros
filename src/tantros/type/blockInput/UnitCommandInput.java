package tantros.type.blockInput;

import arc.Core;
import arc.func.Cons2;
import arc.scene.ui.ImageButton;
import arc.scene.ui.layout.Table;
import arc.util.pooling.Pools;
import mindustry.Vars;
import mindustry.game.EventType.TapEvent;
import mindustry.gen.Building;
import mindustry.gen.Icon;
import mindustry.ui.Styles;
import tantros.type.blockInput.util.TapInputListener;
import tantros.type.buildConfig.BuildConfigurationUnit;
import tantros.type.buildConfig.ClearQueueConfig;
import tantros.type.buildConfig.ClearUnitsConfig;
import tantros.world.blocks.BlockExtended;


public class UnitCommandInput implements BlockInput {

    private static final Cons2<BlockExtended.BuildExtended, TapEvent> handler = UnitCommandInput::handle;

    @Override
    public void apply(BlockExtended block) {
        block.configurable = true;
    }

    @Override
    public void post(BlockExtended.BuildExtended build) {
        TapInputListener listener = Pools.obtain(TapInputListener.class, TapInputListener::new);
        listener.prepare(TapEvent.class, handler, build);
        build.listeners.add(
                listener
        );
    }

    public static void handle(BlockExtended.BuildExtended build, TapEvent event){
        if (!Vars.headless && Vars.control.input.config.getSelected() == build && Vars.control.input.config.isShown()) {
            if(event.tile.build != build && Vars.control.input.commandMode) {
                build.configure(event.tile.pos());
            }
        }

    }

    @Override
    public boolean onConfigureBuildTapped(BlockExtended.BuildExtended build, Building other) {
        if(build == other){
            build.deselect();
        }
        return false;
    }

    @Override
    public void buildConfiguration(BlockExtended.BuildExtended build, Table table) {
        ImageButton button1 = table.button(Icon.cancel, Styles.clearNoneTogglei, 40f, () -> {
            build.configure(BuildConfigurationUnit.get(ClearQueueConfig.class));
        }).tooltip(Core.bundle.get("clear-commands.auto.name")).get();
        button1.clicked(()->{button1.setChecked(false);});
        ImageButton button2 = table.button(Icon.admin, Styles.clearNoneTogglei, 40f, () -> {
            build.configure(BuildConfigurationUnit.get(ClearUnitsConfig.class));
        }).tooltip(Core.bundle.get("clear-units.auto.name")).get();
        button2.clicked(()->{button2.setChecked(false);});
    }
}
