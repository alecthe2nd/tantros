package tantros.content.blocks;

import mindustry.content.*;
import mindustry.type.*;
import mindustry.world.*;
import tantros.content.world.TantrosUnitTypes;
import tantros.world.blocks.units.unitAssembly.BranchableUnitAssembler;
import tantros.world.blocks.units.unitAssembly.BranchedUnitAssemblerModule;

import static mindustry.type.ItemStack.*;

public class TantrosPayload {

    public static Block
            smallUnitAssembler,
            smallMechAssemblyModule,
            smallExplosiveAssemblyModule
            ;

    public static BranchableUnitAssembler.BranchedAssemblerUnitPlan
            tantrosUnitTree,
            aquasPlan,
            roachPlan;

    public static void load(){

        tantrosUnitTree = new BranchableUnitAssembler.BranchedAssemblerUnitPlan(TantrosUnitTypes.delegate,60f * 10f, PayloadStack.list()){{
            itemReq = with(Items.lead, 15, Items.silicon, 20);
        }}.addChild(
                aquasPlan =  new BranchableUnitAssembler.BranchedAssemblerUnitPlan(TantrosUnitTypes.aquas,60f * 20f, PayloadStack.list(TantrosUnitTypes.delegate, 1)){{
                    itemReq = with(Items.metaglass, 30);
                }}
        ).addChild(
                roachPlan = new BranchableUnitAssembler.BranchedAssemblerUnitPlan(TantrosUnitTypes.roach,60f * 20f, PayloadStack.list(TantrosUnitTypes.delegate, 1)){{
                    itemReq = with(Items.coal, 20, Items.graphite, 10);
                    liquidReq = LiquidStack.with(Liquids.ozone, 3f/60f);
                }}
        );

        smallMechAssemblyModule = new BranchedUnitAssemblerModule("small-mech-assembly-module"){{
            requirements(Category.units, with(Items.copper, 20, Items.metaglass, 35, Items.silicon, 20));
            tier = 1;
            plan = aquasPlan;
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
        }};

    }
}
