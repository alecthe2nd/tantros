package tantros.content;

import mindustry.game.Schematic;
import mindustry.game.Schematics;

public class TantrosLoadouts {

    public static Schematic basicShell;

    public static void load(){
        basicShell = Schematics.readBase64("bXNjaAF4nGNgYWBhZmDJS8xNZeByzi9KtQrOSM3JYeBOSS1OLsosKMnMz2NgYGDLSUxKzSlmYIqOZWQQKknMKynKL9ZNBqrXLQarZ2BgBCEgAQBy2hSU");
    }
}
