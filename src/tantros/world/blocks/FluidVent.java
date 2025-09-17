package tantros.world.blocks;

import mindustry.gen.Building;
import mindustry.type.Liquid;
import mindustry.world.blocks.production.GenericCrafter;

public class FluidVent extends GenericCrafter {

    public float openThreshold = 0.95f, closedThreshold = 0.9f;

    public FluidVent(String name) {
        super(name);
        rotate = true;
    }

    public class FluidVentBuild extends GenericCrafterBuild{

        boolean open = false;

        @Override
        public boolean shouldConsume() {
            return super.shouldConsume() && open;
        }

        @Override
        public void updateTile() {
            float fill = back().liquids.currentAmount() / back().block.liquidCapacity;
            if( fill > openThreshold ){
                this.open = true;
            } else if( fill < closedThreshold ) {
                this.open = false;
            }

            super.updateTile();
        }


    }
}
