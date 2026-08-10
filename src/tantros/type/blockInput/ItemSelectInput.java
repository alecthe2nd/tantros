package tantros.type.blockInput;

import arc.func.*;
import arc.scene.style.TextureRegionDrawable;
import arc.scene.ui.ImageButton;
import arc.scene.ui.layout.Table;
import mindustry.gen.Building;
import mindustry.type.Item;
import mindustry.ui.Styles;
import tantros.type.buildConfig.BuildConfigurationUnit;
import tantros.type.buildConfig.SetItemConfig;
import tantros.type.buildingState.drills.ItemSelectionState;
import tantros.world.blocks.BlockExtended;

public class ItemSelectInput implements BlockInput{

    public String stateName;

    public ItemSelectInput(String stateName){
        this.stateName = stateName;
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

        ItemSelectionState state = build.getState(ItemSelectionState.class, stateName);
        if(state == null) return;

        final Boolf<Item> toggled = (i)->state.toggles.get(i, false);
        for(var entry: state.toggles.entries()){
            Item item = entry.key;

            ImageButton button = table.button(new TextureRegionDrawable(item.uiIcon), Styles.clearNoneTogglei, 40f, () -> {
                build.configure(BuildConfigurationUnit.get(SetItemConfig.class).setItem(item));
            }).tooltip(item.localizedName).get();
            button.update(() -> button.setChecked(toggled.get(item)));
        }
    }
}
