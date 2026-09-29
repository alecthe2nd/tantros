package tantros.logic;

import arc.Core;
import arc.scene.style.Drawable;
import arc.scene.style.TextureRegionDrawable;
import arc.scene.ui.Button;
import arc.scene.ui.ButtonGroup;
import arc.scene.ui.layout.Cell;
import arc.scene.ui.layout.Stack;
import arc.scene.ui.layout.Table;
import arc.util.Align;
import arc.util.Log;
import mindustry.Vars;
import mindustry.content.Items;
import mindustry.ctype.Content;
import mindustry.ctype.MappableContent;
import mindustry.gen.Icon;
import mindustry.logic.LAccess;
import mindustry.type.Item;
import mindustry.type.Liquid;
import mindustry.type.UnitType;
import mindustry.ui.Styles;
import mindustry.world.Block;

import static mindustry.Vars.iconSmall;
import static mindustry.logic.LCanvas.tooltip;

public class SignalParseDisplay extends BaseDisplay{

    public String typeName = "copper";

    public Object type = Items.copper;

    private transient int selected = 0;

    @Override
    public void build(Table table){
        table.add(Core.bundle.get("ui.logic.signal-parser.extract")).padRight(3);


        Cell<Button> selectAccess =  table.button(b -> {
            b.label(()->Core.bundle.format("ui.logic.signal-parser.type", typeName));

            //240
            b.clicked(() -> showSelectTable(b, (t, hide) -> {
                Table[] tables = {
                        //items
                        new Table(i -> {
                            i.left();
                            int c = 0;
                            for(Item item : Vars.content.items()){
                                if(!item.unlockedNow() || item.hidden) continue;
                                i.button(new TextureRegionDrawable(item.uiIcon), Styles.flati, iconSmall, () -> {
                                    set(item.name, item);
                                    hide.run();
                                }).size(40f);

                                if(++c % 6 == 0) i.row();
                            }
                        }),
                        //liquids
                        new Table(i -> {
                            i.left();
                            int c = 0;
                            for(Liquid item : Vars.content.liquids()){
                                if(!item.unlockedNow() || item.hidden) continue;
                                i.button(new TextureRegionDrawable(item.uiIcon), Styles.flati, iconSmall, () -> {
                                    set(item.name, item);
                                    hide.run();
                                }).size(40f);

                                if(++c % 6 == 0) i.row();
                            }
                        }),
                        new Table(i -> {
                            i.left();
                            int c = 0;
                            for(UnitType item : Vars.content.units()){
                                if(!item.unlockedNow() || item.hidden) continue;
                                i.button(new TextureRegionDrawable(item.uiIcon), Styles.flati, iconSmall, () -> {
                                    set("@" + item.name, item);
                                    hide.run();
                                }).size(40f);

                                if(++c % 6 == 0) i.row();
                            }

                            for(Block item : Vars.content.blocks()){
                                if(!item.unlockedNow() || item.isHidden()) continue;
                                i.button(new TextureRegionDrawable(item.uiIcon), Styles.flati, iconSmall, () -> {
                                    set(item.name, item);
                                    hide.run();
                                }).size(40f);

                                if(++c % 6 == 0) i.row();
                            }
                        }),
                        //sensors
                        new Table(i -> {
                            for(LAccess sensor : LAccess.senseable){
                                i.button(bundle(sensor), Styles.flatt, () -> {
                                    set(sensor.name(), sensor);
                                    hide.run();
                                }).size(240f, 40f).self(c -> tooltip(c, sensor)).row();
                            }
                        })
                };

                Drawable[] icons = {Icon.box, Icon.liquid, Icon.units, Icon.tree};
                Stack stack = new Stack(tables[selected]);
                ButtonGroup<Button> group = new ButtonGroup<>();

                for(int i = 0; i < tables.length; i++){
                    int fi = i;

                    t.button(icons[i], Styles.squareTogglei, () -> {
                        selected = fi;

                        stack.clearChildren();
                        stack.addChild(tables[selected]);

                        t.parent.parent.pack();
                        t.parent.parent.invalidateHierarchy();
                    }).height(50f).growX().checked(selected == fi).group(group);
                }
                t.row();
                t.add(stack).colspan(4).width(240f).left();
            }));
        }, Styles.logict, () -> {}).margin(3f);
        selectAccess.height(40).growX().color(table.color);

        table.add(Core.bundle.get("ui.logic.signal-parser.from")).padLeft(3);

    }

    public void set(String text, Object type){
        this.type = type;
        this.typeName = text;
    }

    public void set(Object type){
        this.type = type;
        this.typeName = unpack(type);
    }

    public String unpack(Object type){
        if(type instanceof MappableContent content){
            return content.name;
        }
        if(type instanceof LAccess access){
            return access.name();
        }
        return "ohno";
    }
}
