package tantros.content.blocks;

import arc.Events;
import arc.func.Cons;
import arc.math.Mathf;
import arc.struct.Seq;
import mindustry.Vars;
import mindustry.content.Items;
import mindustry.game.EventType;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.type.UnitType;
import mindustry.type.Weapon;
import mindustry.world.Block;
import mindustry.world.draw.DrawBlock;
import mindustry.world.draw.DrawDefault;
import mindustry.world.draw.DrawRegion;
import mindustry.world.meta.BuildVisibility;
import tantros.content.world.TantrosItems;
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
    skimFrame,
    //skiffFrame,
    //fleetFrame,
    delegateFrame
            ;

    public static void load(){

        flakFrame = buildFrame(TantrosUnitTypes.flak, with(Items.metaglass, 10));

        sherdFrame = buildFrame(TantrosUnitTypes.sherd, with(Items.lead, 10, Items.metaglass, 25));

        fractoidFrame = buildFrame(TantrosUnitTypes.fractoid, with(Items.titanium, 60, Items.metaglass, 60, Items.silicon, 10));

        roachFrame = buildFrame(TantrosUnitTypes.roach, with(Items.lead, 30, TantrosItems.redcyst, 15));

        infestFrame = buildFrame(TantrosUnitTypes.infest, with(Items.lead, 40, TantrosItems.redcyst, 25));

        invadeFrame = buildFrame(TantrosUnitTypes.invade, with(Items.lead, 60, Items.tungsten, 30, Items.silicon, 20));

        skimFrame = buildFrame(TantrosUnitTypes.skim, with(Items.metaglass, 15, Items.silicon, 5));

        delegateFrame = buildFrame(TantrosUnitTypes.delegate, with(Items.silicon, 10, Items.lead, 5));

    }

    public static BlockExtended buildFrame(UnitType unitType, ItemStack[] cost){
        return buildFrame(unitType, cost, (b)->{});
    }

    public static BlockExtended buildFrame(UnitType unitType, ItemStack[] cost, Cons<BlockExtended> modifier){
        return new BlockExtended((unitType.name.replaceFirst("tantros-","")) + "-frame"){{
            requirements(Category.units, cost);
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
                    multi.drawers = drawers.toArray(DrawBlock.class);
                }
            });
        }};
    }

}
