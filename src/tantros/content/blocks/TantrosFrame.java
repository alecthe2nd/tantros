package tantros.content.blocks;

import arc.Events;
import arc.math.Mathf;
import arc.struct.Seq;
import mindustry.Vars;
import mindustry.content.Items;
import mindustry.game.EventType;
import mindustry.type.Category;
import mindustry.type.UnitType;
import mindustry.type.Weapon;
import mindustry.world.Block;
import mindustry.world.draw.DrawBlock;
import mindustry.world.draw.DrawDefault;
import mindustry.world.draw.DrawRegion;
import mindustry.world.meta.BuildVisibility;
import tantros.content.world.TantrosUnitTypes;
import tantros.type.effect.IsBuilding;
import tantros.world.blocks.BlockExtended;
import tantros.world.draw.extended.DrawBlockExtended;
import tantros.world.draw.extended.DrawMultiExtended;
import tantros.world.draw.extended.DrawUnit;
import tantros.world.draw.extended.DrawWeapon;

import static mindustry.type.ItemStack.with;

public class TantrosFrame {

    public static Block flakFrame,
    sherdFrame,
    fractoidFrame,
    roachFrame,
    infestFrame,
    invadeFrame,
    delegateFrame
            ;

    public static void load(){

        flakFrame = buildFrame(TantrosUnitTypes.flak);

        sherdFrame = buildFrame(TantrosUnitTypes.sherd);

        fractoidFrame = buildFrame(TantrosUnitTypes.fractoid);

        roachFrame = buildFrame(TantrosUnitTypes.roach);

        infestFrame = buildFrame(TantrosUnitTypes.infest);

        invadeFrame = buildFrame(TantrosUnitTypes.invade);

        delegateFrame = buildFrame(TantrosUnitTypes.delegate);

    }

    public static BlockExtended buildFrame(UnitType unitType){
        return new BlockExtended(unitType.name + "-frame"){{
            requirements(Category.units, BuildVisibility.sandboxOnly, with(Items.copper, 20, Items.metaglass, 35, Items.silicon, 20));
            effects.add(new IsBuilding());

            rotate = true;
            drawArrow = true;
            rotateDraw = true;
            size = Mathf.round(unitType.hitSize / Vars.tilesize);

            drawer = new DrawMultiExtended(
                    new DrawUnit(unitType)
            );
            Events.on(EventType.ContentInitEvent.class, (EventType.ContentInitEvent event)->{
                if(drawer instanceof DrawMultiExtended multi) {
                    Seq<DrawBlock> drawers = new Seq<>(multi.drawers);
                    for (Weapon weapon : unitType.weapons) {
                        drawers.add(new DrawWeapon(weapon));
                    }
                    multi.drawers = drawers.toArray();
                }
            });
        }};
    }

}
