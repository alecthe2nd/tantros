package tantros.ui;

import arc.scene.ui.layout.Table;
import mindustry.ui.Bar;

public class UIUtil {

    public static void addBar(Table table, Bar bar){
        if(bar == null) return;
        table.add(bar).growX();
        table.row();
    }
}
