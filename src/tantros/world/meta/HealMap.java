package tantros.world.meta;

import arc.struct.IntFloatMap;
import arc.util.Log;
import arc.util.Reflect;
import mindustry.Vars;
import mindustry.world.blocks.defense.RegenProjector;

import java.lang.reflect.Field;

import static mindustry.Vars.state;
import static mindustry.Vars.world;

public class HealMap {
    public IntFloatMap mendMap;
    public boolean failedToGetUpdateId = false;
    public long fallbackUpdateId;
    public Field lastUpdateFrame;

    public HealMap(){
        try {
            mendMap = Reflect.get(RegenProjector.class, "mendMap");
        } catch(Exception e){
            Log.err("Failed to extract RegenProjector mend map. Switching to fallback. Due to this," +
                    "regen activities may stack with Regen Projectors.", e);
            mendMap = new IntFloatMap();
        }
        try {
            lastUpdateFrame = RegenProjector.class.getDeclaredField("lastUpdateFrame");
            lastUpdateFrame.setAccessible(true);
            fallbackUpdateId = Reflect.get(lastUpdateFrame);
        } catch(Exception e){
            Log.err("Failed to extract RegenProjector lastUpdateFrame. Switching to fallback. Due to this," +
                    "regen activities may stack with Regen Projectors.", e);
            lastUpdateFrame = null;
            failedToGetUpdateId = true;
            fallbackUpdateId = 0;
        }


    }

    public void tryHealUpdate(){
        if(tryGetLastUpdateFrame() != state.updateId){
            trySetLastUpdateFrame(state.updateId);

            for(var entry : mendMap.entries()){
                var build = world.build(entry.key);
                if(build != null){
                    build.heal(entry.value);
                    build.recentlyHealed();
                }
            }
            mendMap.clear();
        }
    }

    public float tryGetLastUpdateFrame(){
        if(failedToGetUpdateId) return fallbackUpdateId;
        float out;
        try {
            out = fallbackUpdateId = Reflect.get(lastUpdateFrame);
        } catch(Exception e){
            Log.err("Failed to get lastUpdateFrame. Switching to fallback. Due to this," +
                    "regen activities may stack with Regen Projectors.", e);
            lastUpdateFrame = null;
            failedToGetUpdateId = true;
            out = fallbackUpdateId = 0;
            mendMap = new IntFloatMap();
        }
        return out;
    }

    public void trySetLastUpdateFrame(long value){
        fallbackUpdateId = value;
        if(failedToGetUpdateId) return;
        try {
            Reflect.set(RegenProjector.class, lastUpdateFrame, value);
        } catch(Exception e){
            Log.err("Failed to set lastUpdateFrame. Switching to fallback. Due to this," +
                    "regen activities may stack with Regen Projectors.", e);
            lastUpdateFrame = null;
            failedToGetUpdateId = true;
            mendMap = new IntFloatMap();
        }
    }
}
