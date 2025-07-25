package tantros.world.environment;

import arc.graphics.Color;
import arc.struct.Seq;
import mindustry.content.Liquids;
import mindustry.type.Liquid;
import mindustry.world.meta.Env;

public class LocalEnv {

    public Seq<Liquid> liquids = new Seq<>();

    public boolean isWater(){
        return liquids.contains(Liquids.water);
    }

    public boolean isSpace(){
        return liquids.isEmpty();
    }

    public void mergeIn(LocalEnv env){
        for (Liquid liq : env.liquids){
            this.liquids.addUnique(liq);
        }
    }

    public void replaceWith(LocalEnv env){
        liquids.clear();
        mergeIn(env);
    }

    public Color computeColor(){
        switch (liquids.size){
            case 1:
                return liquids.first().color;
            case 2:
                Color color = new Color();
                color.set(liquids.first().color).lerp(liquids.get(1).color, 0.5f);
                return color;
            default:
                return Color.white;
        }
    }

    public static LocalEnv with(int env){
        LocalEnv newEnv = new LocalEnv();
        if((env & Env.space) > 0){
            return newEnv;
        }
        if((env & Env.oxygen) > 0){
            newEnv.liquids.add(Liquids.ozone);
            newEnv.liquids.add(Liquids.nitrogen);
        }
        if((env & Env.scorching) > 0){
            newEnv.liquids.add(Liquids.nitrogen);
        }
        if((env & Env.underwater) > 0){
            newEnv.liquids.add(Liquids.water);
        }
        return newEnv;
    }

    public static LocalEnv with(Liquid... liquids){
        LocalEnv newEnv = new LocalEnv();
        for(Liquid liq: liquids){
            newEnv.liquids.addUnique(liq);
        }
        return newEnv;
    }

    public boolean meets(LocalEnv requirement){

        return requirement == null || this.liquids.containsAll(requirement.liquids);
    }

}
