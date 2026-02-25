package tantros.content;

import arc.util.Log;
import mindustry.Vars;
import mindustry.game.Schematic;
import mindustry.game.Schematics;

import java.lang.reflect.Method;

public class TantrosLoadouts {

    public static Schematic basicShell;

    public static void load(){
        basicShell = build("bXNjaAF4nGNgYWBhZmDJS8xNZeByzi9KtQrOSM3JYeBOSS1OLsosKMnMz2NgYGDLSUxKzSlmYIqOZWQQKknMKynKL9ZNBqrXLQarZ2BgBCEgAQBy2hSU");
    }

    public static Schematic build(String schemeString){
        Schematic schematic = Schematics.readBase64(schemeString);

        try{
            Method m = Schematics.class.getDeclaredMethod("checkLoadout", Schematic.class, boolean.class);
            m.setAccessible(true);
            m.invoke(Vars.schematics, schematic, false);
        } catch (Exception e) {
            Log.err(e);
        }

        return schematic;
    }
}
