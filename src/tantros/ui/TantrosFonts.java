package tantros.ui;

import arc.Core;
import arc.graphics.g2d.TextureRegion;
import arc.struct.IntMap;
import arc.struct.ObjectMap;
import arc.util.Log;
import mindustry.Vars;
import mindustry.game.Team;
import mindustry.ui.Fonts;
import tantros.TantrosVars;

import java.io.Reader;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Properties;

//Stolen From Aquarion :troll:
public class TantrosFonts{

    public static final int ICONS_START = 63743;
    public static final int ICONS_END = 57344;

    public static ObjectMap<String, TextureRegion> regions = new ObjectMap<>();

    public static void loadFonts(){

        int startingCode = ICONS_START;
        IntMap<String> unicodeToStr = null;
        try{
            Field field = Fonts.class.getDeclaredField("unicodeToName");
            field.setAccessible(true);
            unicodeToStr = (IntMap<String>) field.get(null);
        } catch (Exception e){
            Log.err("Error fetching unicodeToName.");
            Log.err(e);
        }

        Properties iconProperties = new Properties();
        try(Reader reader = Vars.tree.get("icons/" + TantrosVars.modWrapper.name + "-icons.properties").reader(512)){
            iconProperties.load(reader);
        }catch(Exception e){
            Log.err(e);
            return;
        }

        for(Map.Entry<Object, Object> entry : iconProperties.entrySet()){
            String propertyName = (String)entry.getKey();
            String[] nameParts = propertyName.split("\\.",2);
            if(nameParts.length < 2 || !"icon".equals(nameParts[0])) continue;
            String name = nameParts[1];
            String[] valueParts = ((String)entry.getValue()).split("\\|");
            if(valueParts.length < 2) continue;

            try{
                int codePoint;
                if("auto".equals( valueParts[0] )){
                    int next = getNextCharCode(unicodeToStr, startingCode);
                    if(next == -1){
                        Log.info("Unable to add icon");
                    };
                    startingCode = next;
                    codePoint = startingCode;
                } else {
                    codePoint = Integer.parseInt(valueParts[0]);
                }
                Log.info("Added icon '" + name + "' at '" + codePoint + "'");
                String textureName = valueParts[1];
                TextureRegion region = Core.atlas.find(textureName);
                regions.put(name, region);
                Fonts.registerIcon(name, textureName, codePoint, region);

            }catch(Exception e){
                Log.err(e);
            }
        }
    }

    public static int getNextCharCode(IntMap<String> map, int startingCode){
        if(map == null || startingCode > ICONS_START || startingCode < ICONS_END) return -1;
        for(int i = startingCode; i > ICONS_END - 1; i--){
            if(!map.containsKey(i)) return i;
        }
        return -1;
    }
}
