package tantros.content.blocks;

import arc.struct.Seq;
import mindustry.content.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.units.UnitAssembler;
import mindustry.world.blocks.units.UnitAssemblerModule;
import mindustry.world.blocks.units.UnitFactory;
import tantros.content.TantrosUnitTypes;
import tantros.world.blocks.distribution.payload.SealedPayloadConveyor;
import tantros.world.blocks.payload.FrameConstructor;
import tantros.world.blocks.units.unitAssembly.BranchableUnitAssembler;

import static mindustry.type.ItemStack.*;

public class TantrosPayload {

    public static Block
            delegateFabricator,
            smallFrameFabricator,
            smallMechAssemblyModule,
            assemblyExpansionModule,
            smallBenthicAssembler,
            smallSubBenthicAssembler,
            smallSubmarineAssembler,
            sealedPayloadConveyor
            ;
    
    public static void load(){

        delegateFabricator = new UnitFactory("delegate-fabricator"){{
            requirements(Category.units, with(Items.silicon, 55, Items.lead, 45, Items.oxide, 50));
            size = 3;
            configurable = false;
            plans.add(new UnitPlan(TantrosUnitTypes.delegate, 60f * 15f, with(Items.lead, 10, Items.silicon, 15)));
            regionSuffix = "-sealed";
            fogRadius = 3;
            consumePower(.75f);
        }};

        smallFrameFabricator = new FrameConstructor("small-frame-fabricator"){{
            requirements(Category.units, with(Items.metaglass, 40, Items.oxide, 35, Items.silicon, 55));
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
                    TantrosFrame.invadeFrame,
                    TantrosFrame.skimFrame
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

            consumePower(1.5f);
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

        smallSubmarineAssembler = new UnitAssembler("small-submarine-assembler"){{
            requirements(Category.units, with(Items.oxide, 40, Items.copper, 30, Items.metaglass, 45, Items.silicon, 65));
            regionSuffix = "-sealed";
            size = 3;
            plans.add(
                    new UnitAssembler.AssemblerUnitPlan(TantrosUnitTypes.skim, 60f * 20f, PayloadStack.list(TantrosUnitTypes.delegate, 1, TantrosFrame.skimFrame, 1))
            );
            areaSize = 5;
            //researchCostMultiplier = 0.4f;

            consumePower(0.75f);
        }};

        assemblyExpansionModule = new UnitAssemblerModule("assembly-expansion-module"){{
            requirements(Category.units, with(Items.oxide, 30, Items.metaglass, 10, Items.lead, 40));
            regionSuffix = "-sealed";

            size = 2;
        }};

        sealedPayloadConveyor = new SealedPayloadConveyor("sealed-payload-conveyor"){{
            requirements(Category.units, with(Items.copper, 10, Items.metaglass, 25, Items.oxide, 15));
            moveTime = 35f;
            canOverdrive = false;
            health = 800;
            researchCostMultiplier = 4f;
            underBullets = true;
        }};

    }
}
