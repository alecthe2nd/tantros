package tantros.world.blocks.units.unitAssembly;

import mindustry.world.blocks.units.UnitAssemblerModule;

public class BranchedUnitAssemblerModule extends UnitAssemblerModule {

    public BranchableUnitAssembler.BranchedAssemblerUnitPlan plan;

    @Override
    public void init() {
        super.init();
        this.tier = plan.tier;
    }

    public BranchedUnitAssemblerModule(String name) {
        super(name);
    }

    public class BranchedUnitAssemblerModuleBuild extends UnitAssemblerModuleBuild{

        public BranchableUnitAssembler.BranchedAssemblerUnitPlan plan(){
            return plan;
        }
    }
}
