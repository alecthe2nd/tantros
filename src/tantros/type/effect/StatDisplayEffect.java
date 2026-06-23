package tantros.type.effect;


import arc.Core;
import arc.scene.ui.layout.Table;
import mindustry.ui.Styles;
import mindustry.world.meta.Stat;
import tantros.world.blocks.BlockExtended;

public abstract class StatDisplayEffect implements BlockEffect {
    public String bundle = "";

    protected StatDisplayEffect(){
        var type = getClass();
        bundle = "block-ability." + (type.isAnonymousClass() ? type.getSuperclass() : type).getSimpleName().replace("Effect", "").toLowerCase();
    }

    @Override
    public void setStats(BlockExtended block) {
        block.stats.add(Stat.abilities, (t)-> {
            t.row();
            t.table(Styles.grayPanel, a -> {
                a.add("[accent]" + Core.bundle.get(bundle)).padBottom(4).center().top().expandX();
                a.row();
                a.left().top().defaults().left();
                addStats(a);
            }).pad(5).margin(10).growX().top().uniformX();
        });
    }

    public void addStats(Table t){
        if(Core.bundle.has(bundle + ".description")){
            t.add(Core.bundle.get(bundle + ".description")).wrap().width(350f);
            t.row();
        }
    }
}
