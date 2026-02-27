package tantros.mod;

import arc.Core;
import arc.files.Fi;
import arc.util.Log;
import mindustry.Vars;
import mindustry.mod.Mods;
import mindustry.mod.Scripts;

import java.lang.reflect.Method;

import static mindustry.Vars.platform;

public class ScriptInjector {

    public static void load(Mods.LoadedMod mod, String file){
        if(mod.root.child("scripts").exists() && mod.root.child("scripts").child("injected").exists()){
            Fi injected = mod.root.child("scripts").child("injected").child(file);
            if(injected.exists() && !injected.isDirectory()){
                try{
                    Scripts scripts = Vars.mods.getScripts();
                    Method run = Scripts.class.getDeclaredMethod("run", String.class, String.class, boolean.class);
                    run.setAccessible(true);
                    run.invoke(scripts, injected.readString(), mod.name + "/" + injected.name(), false);
                }catch(Throwable e){
                    Core.app.post(() -> {
                        Log.err("Error loading injected script @ for mod @.", injected.name(), mod.meta.name);
                        Log.err(e);
                    });
                }
            }
        }
    }

}
