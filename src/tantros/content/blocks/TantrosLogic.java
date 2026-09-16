package tantros.content.blocks;

import arc.graphics.Color;
import mindustry.Vars;
import mindustry.content.Items;
import mindustry.entities.part.RegionPart;
import mindustry.graphics.Layer;
import mindustry.type.Category;
import mindustry.world.Block;
import mindustry.world.draw.DrawBlockParts;
import mindustry.world.draw.DrawDefault;
import mindustry.world.draw.DrawRegion;
import tantros.type.effect.logic.SensesBlockContents;
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
    unitInstructor,
    buildingSensor
    ;

    public static void load(){
        linkingPad = new BlockExtended("linking-pad"){{
            requirements(Category.logic, with(Items.metaglass, 30, Items.silicon, 10));
            drawer = new DrawMultiExtended(
                    new DrawDefault(),
                    new DrawTeamRegion(),
                    //drawPlace
                    new DrawPlacementRange(),
                    //drawConfig
                    new DrawLinkConfigureRange<>(OneLink.class)
            );
            size = 3;

            effect(
                    new UnitLinkSetter<>(
                            OneLink.class,
                            ()-> new OneLink((b)->b.block.configurations.containsKey(AddUnitConfig.class)),
                            new RangeConfig(4*Vars.tilesize)
                    )
            );
            effect(new IsBuilding());
            effect(new IsWalkable());
        }};

        unitInstructor = new BlockExtended("unit-instructor"){{
            requirements(Category.logic, with(Items.oxide, 20, Items.silicon, 40));
            drawer = new DrawMultiExtended(
                    new DrawRegion("-bottom"),
                    new DrawUnitPointers(),
                    new DrawDefault(),
                    new DrawTeamRegion(),
                    new DrawCommandQueue()
            );
            size = 2;

            effect(new IsBuilding());
            effect(new UnitCommandAssigner());
        }};


        buildingSensor = new BlockExtended("building-sensor"){{
            requirements(Category.logic, with(Items.silicon, 20));
            drawer = new DrawMultiExtended(
                    new DrawDefault(),
                    new DrawBlockParts(){{
                        parts.add(new RegionPart("-top1"){{
                            progress = ((PartProgress)(param)->param.rotation).apply((p)->0f,(a,b)->(a < 180)?1f:0f);
                            color = new Color(1f, 1f, 1f, 0f);
                            colorTo = Color.white;
                            y = 4;
                            outline = false;
                            layer = Layer.blockAdditive;
                        }});
                        parts.add(new RegionPart("-top2"){{
                            progress = ((PartProgress)(param)->param.rotation).apply((p)->0f,(a,b)->(a < 180)?0f:1f);
                            color = new Color(1f, 1f, 1f, 0f);
                            colorTo = Color.white;
                            y = 4;
                            outline = false;
                            layer = Layer.blockAdditive;
                        }});
                    }}
            );
            size = 1;

            effect(new IsBuilding());
            //effect(new SensesBlockContents());
            rotate  = true;
            rotateDraw = false;
            rotateDrawEditor = false;
            drawArrow = true;
            noUpdateDisabled = false;

        }};

        //TODO delete this: Events.on(EventType.ConfigEvent.class, (e)-> Log.info("Config event detected at '" + e.tile + "' by player '"+ e.player + "' of value " + e.value));
    }
}
