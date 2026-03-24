package tantros.content.blocks;

import arc.struct.Seq;
import mindustry.content.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.payloads.Constructor;
import mindustry.world.blocks.units.UnitAssembler;
import mindustry.world.blocks.units.UnitFactory;
import tantros.content.world.TantrosUnitTypes;
import tantros.world.blocks.units.unitAssembly.BranchableUnitAssembler;
import tantros.world.blocks.units.unitAssembly.BranchedUnitAssemblerModule;

import static mindustry.type.ItemStack.*;

public class TantrosPayload {

    public static Block
            delegateFabricator,
            smallFrameFabricator,
            smallUnitAssembler,
            smallMechAssemblyModule,
            smallExplosiveAssemblyModule,
            smallBenthicAssembler,
            smallSubBenthicAssembler
            ;

    public static BranchableUnitAssembler.BranchedAssemblerUnitPlan
            tantrosUnitTree,
            flakPlan,
            roachPlan;

    public static void load(){

        tantrosUnitTree = new BranchableUnitAssembler.BranchedAssemblerUnitPlan(TantrosUnitTypes.delegate,60f * 10f, PayloadStack.list()){{
            itemReq = with(Items.lead, 15, Items.silicon, 20);
        }}.addChild(
                flakPlan =  new BranchableUnitAssembler.BranchedAssemblerUnitPlan(TantrosUnitTypes.flak,60f * 20f, PayloadStack.list(TantrosUnitTypes.delegate, 1)){{
                    itemReq = with(Items.metaglass, 30);
                }}
        ).addChild(
                roachPlan = new BranchableUnitAssembler.BranchedAssemblerUnitPlan(TantrosUnitTypes.roach,60f * 20f, PayloadStack.list(TantrosUnitTypes.delegate, 1)){{
                    itemReq = with(Items.coal, 20, Items.graphite, 10);
                    liquidReq = LiquidStack.with(Liquids.ozone, 3f/60f);
                }}
        );

        /*smallMechAssemblyModule = new BranchedUnitAssemblerModule("small-mech-assembly-module"){{
            requirements(Category.units, with(Items.copper, 20, Items.metaglass, 35, Items.silicon, 20));
            tier = 1;
            plan = flakPlan;
        }};

        smallExplosiveAssemblyModule = new BranchedUnitAssemblerModule("small-explosive-assembly-module"){{
            requirements(Category.units, with(Items.lead, 20, Items.graphite, 35, Items.silicon, 20));
            tier = 1;
            plan = roachPlan;
        }};

        smallUnitAssembler = new BranchableUnitAssembler("small-unit-assembler"){{
            requirements(Category.units, with(Items.copper, 50, Items.lead, 50));
            //regionSuffix = "-dark";
            size = 3;
            planTree = tantrosUnitTree;
            areaSize = 3;
            researchCostMultiplier = 0.4f;

            consumePower(60f/60f);
        }};*/

        delegateFabricator = new UnitFactory("delegate-fabricator"){{
            requirements(Category.units, with(Items.silicon, 115, Items.lead, 90, Items.oxide, 100));
            size = 3;
            configurable = false;
            plans.add(new UnitPlan(TantrosUnitTypes.delegate, 60f * 15f, with(Items.lead, 10, Items.silicon, 15)));
            regionSuffix = "-sealed";
            fogRadius = 3;
            consumePower(.75f);
        }};

        smallFrameFabricator = new Constructor("small-frame-fabricator"){{
            requirements(Category.units, with(Items.silicon, 50, Items.beryllium, 75, Items.tungsten, 40));
            regionSuffix = "-sealed";
            hasPower = true;
            buildSpeed = 0.1f;
            consumePower(2f);
            size = 3;
            filter = Seq.with(
                    TantrosFrame.flakFrame,
                    TantrosFrame.sherdFrame,
                    TantrosFrame.fractoidFrame,
                    TantrosFrame.roachFrame,
                    TantrosFrame.infestFrame,
                    TantrosFrame.invadeFrame
                    //skim
                    //skiff
                    //fleet
            );
            minBlockSize = 1;
            maxBlockSize = 3;
        }};

        smallBenthicAssembler = new UnitAssembler("small-benthic-assembler"){{
            requirements(Category.units, with(Items.oxide, 50, Items.copper, 30, Items.metaglass, 30, Items.silicon, 35));
            regionSuffix = "-sealed";
            size = 3;
            plans.add(
                    new UnitAssembler.AssemblerUnitPlan(TantrosUnitTypes.flak, 60f * 15f, PayloadStack.list(TantrosUnitTypes.delegate, 1, TantrosFrame.flakFrame, 1)),
                    new UnitAssembler.AssemblerUnitPlan(TantrosUnitTypes.sherd,  60f * 30f, PayloadStack.list(TantrosUnitTypes.delegate, 2, TantrosFrame.sherdFrame, 1)),
                    new UnitAssembler.AssemblerUnitPlan(TantrosUnitTypes.fractoid,  60f * 45f, PayloadStack.list(TantrosUnitTypes.delegate, 4, TantrosFrame.fractoidFrame, 1))
            );
            areaSize = 5;
            //researchCostMultiplier = 0.4f;

            consumePower(0.5f);
        }};

        smallSubBenthicAssembler = new UnitAssembler("small-sub-benthic-assembler"){{
            requirements(Category.units, with(Items.oxide, 60, Items.lead, 20, Items.metaglass, 25, Items.silicon, 55));
            regionSuffix = "-sealed";
            size = 3;
            plans.add(
                    new UnitAssembler.AssemblerUnitPlan(TantrosUnitTypes.roach, 60f * 15f, PayloadStack.list(TantrosUnitTypes.delegate, 1, TantrosFrame.roachFrame, 1)),
                    new UnitAssembler.AssemblerUnitPlan(TantrosUnitTypes.infest,  60f * 55f, PayloadStack.list(TantrosUnitTypes.delegate, 2, TantrosFrame.infestFrame, 1)),
                    new UnitAssembler.AssemblerUnitPlan(TantrosUnitTypes.invade,  60f * 60 * 2f, PayloadStack.list(TantrosUnitTypes.delegate, 4, TantrosFrame.invadeFrame, 1))
            );
            areaSize = 5;
            //researchCostMultiplier = 0.4f;

            consumePower(0.75f);
        }};

    }
}
