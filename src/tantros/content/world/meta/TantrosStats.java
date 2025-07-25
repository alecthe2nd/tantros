package tantros.content.world.meta;

import arc.func.Boolf;
import arc.graphics.Color;
import arc.scene.ui.Image;
import arc.scene.ui.layout.Stack;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import arc.util.Scaling;
import arc.util.Strings;
import mindustry.type.Liquid;
import mindustry.ui.Styles;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatCat;
import mindustry.world.meta.StatUnit;
import mindustry.world.meta.StatValue;
import tantros.world.environment.LocalEnv;

import static mindustry.Vars.content;
import static mindustry.Vars.iconMed;
import static mindustry.world.meta.StatValues.displayLiquid;
import static mindustry.world.meta.StatValues.withTooltip;

public class TantrosStats {

    public static StatCat

            environment = new StatCat("environment")

            ;

    public static Stat

            pressureRange = new Stat("pressurerange", StatCat.function),

            maxPressure = new Stat("maxpressure", StatCat.function),

            requiredEnvironments = new Stat("requiredenvironments", TantrosStats.environment);



    public static StatValue liquidEnvironmentReq(LocalEnv req){
        return table -> {
            int count = 0;
            for( Liquid liquid: req.liquids){
                table.add(displayLiquid(liquid)).padRight(5);
                if (count != 0){
                    table.add("+");
                }
                count ++;
            }
            /*
            Seq<Liquid> list = content.liquids().select(i -> filter.get(i) && i.unlockedNow() && !i.isHidden());

            for(int i = 0; i < list.size; i++){
                table.add(displayLiquid(list.get(i), amount, perSecond)).padRight(5);

                if(i != list.size - 1){
                    table.add("/");
                }
            }
            */
        };
    }

    public static Table displayLiquid(Liquid liquid){
        Table t = new Table();

        t.add(new Stack(){{
            add(new Image(liquid.uiIcon).setScaling(Scaling.fit));
        }}).size(iconMed).padRight(3).with(s -> withTooltip(s, liquid, false));

        t.add(liquid.localizedName);

        return t;
    }
}
