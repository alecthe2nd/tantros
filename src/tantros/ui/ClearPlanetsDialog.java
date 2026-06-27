package tantros.ui;

import arc.Core;
import arc.scene.ui.layout.Table;
import arc.util.Log;
import mindustry.content.TechTree;
import mindustry.ctype.UnlockableContent;
import mindustry.gen.Tex;
import mindustry.type.Planet;
import mindustry.ui.dialogs.BaseDialog;

import static arc.Core.bundle;
import static mindustry.Vars.*;

public class ClearPlanetsDialog extends BaseDialog {

    public ClearPlanetsDialog() {
        super(bundle.get("setting.clear-specific-planet", "Clear Specific Planet"));
        addCloseButton();


        this.cont.table(Tex.button, t -> {
            t.defaults().size(280f, 60f).left();
            for(Planet planet: content.planets()){
                if(planet.accessible){
                    addButton(t, planet);
                }
                t.row();
            }
            t.button(
                    "Test Button",
                    ()->{
                        Log.info("I test nothing :D");
                    }
            );
        });
    }


    public void addButton(Table t, Planet planet){

        t.button(
                Core.bundle.format("setting.clear-planet", planet.localizedName),
                ()->{

                    ui.showConfirm("@confirm", Core.bundle.format("setting.clear-planet.confirm", planet.localizedName), () -> {
                        for (var sec : planet.sectors) {
                            sec.clearInfo();
                            if (sec.save != null) {
                                sec.save.delete();
                                sec.save = null;
                            }
                        }

                        for (var slot : control.saves.getSaveSlots().copy()) {
                            if (slot.isSector() && slot.meta.rules != null && slot.meta.rules.planet == planet) {
                                slot.delete();
                            }
                        }
                        //universe.clearLoadoutInfo();

                        if(planet.techTree != null) planet.techTree.each(TechTree.TechNode::reset);
                        content.each(c -> {
                            if(c instanceof UnlockableContent u && u.isOnPlanet(planet)){
                                u.clearUnlock();
                            }
                        });
                    });
                }
        ).marginLeft(4);
    }
}
