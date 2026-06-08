package tantros.content.blocks;

import arc.Events;
import arc.util.Log;
import mindustry.Vars;
import mindustry.content.Items;
import mindustry.game.EventType;
import mindustry.type.Category;
import mindustry.world.Block;
import mindustry.world.draw.DrawDefault;
import mindustry.world.draw.DrawRegion;
import tantros.type.effect.projector.range.RangeConfig;
import tantros.type.buildConfig.AddUnitConfig;
import tantros.type.buildingState.logic.OneLink;
import tantros.type.effect.IsBuilding;
import tantros.type.effect.IsWalkable;
import tantros.type.effect.logic.UnitCommandAssigner;
import tantros.type.effect.logic.UnitLinkSetter;
import tantros.world.blocks.BlockExtended;
import tantros.world.draw.DrawTeamRegion;
import tantros.world.draw.extended.*;

import static mindustry.type.ItemStack.with;

public class TantrosLogic {

    public static Block
    linkingPad,
    unitInstructor
    ;

    public static void load(){
        linkingPad = new BlockExtended("linking-pad"){{
            requirements(Category.logic, with(Items.metaglass, 10));
            drawer = new DrawMultiExtended(
                    new DrawDefault(),
                    new DrawTeamRegion(),
                    //drawPlace
                    new DrawPlacementRange(),
                    //drawConfig
                    new DrawLinkConfigureRange<>(OneLink.class)
            );
            size = 3;

            effects.add(
                    new UnitLinkSetter<>(
                            OneLink.class,
                            ()-> new OneLink((b)->b.block.configurations.containsKey(AddUnitConfig.class)),
                            new RangeConfig(4*Vars.tilesize)
                    )
            );
            effects.add(new IsBuilding());
            effects.add(new IsWalkable());
        }};

        unitInstructor = new BlockExtended("unit-instructor"){{
            requirements(Category.logic, with(Items.metaglass, 10));
            drawer = new DrawMultiExtended(
                    new DrawRegion("-bottom"),
                    new DrawUnitPointers(),
                    new DrawDefault(),
                    new DrawTeamRegion(),
                    new DrawCommandQueue()
            );
            size = 2;

            effects.add(new IsBuilding());
            effects.add(new UnitCommandAssigner());
        }};


        Events.on(EventType.ConfigEvent.class, (e)-> Log.info("Config event detected at '" + e.tile + "' by player '"+ e.player + "' of value " + e.value));
    }
}
