package tantros.type.buildingState;

import arc.math.Mathf;
import mindustry.type.LiquidStack;
import tantros.type.blockConfig.BoilerConfig;
import tantros.world.blocks.BlockExtended;

public class BoilerPressureBuildup implements BuildingState{

    public BoilerConfig boilerConfig;

    public float pressure = 0;

    public float smoothPressure = 0;

    public float overflow = 0;


    @Override
    public void initState(BlockExtended ownerType, BlockExtended.BuildExtended owner) {
        boilerConfig = ownerType.getBlockConfig(BoilerConfig.class);
    }

    @Override
    public void update(BlockExtended ownerType, BlockExtended.BuildExtended owner) {


        if (overflow < 0.0001f) {
            pressure = Mathf.lerpDelta(pressure, 0, boilerConfig.pressureRelief);
        }

        pressure = Math.min(Math.max(pressure,0), boilerConfig.pressureCapacity);
        smoothPressure = Mathf.lerp(smoothPressure, pressure, 0.05f);


        overflow = 0f;
    }

    @Override
    public void onProximity(BlockExtended ownerType, BlockExtended.BuildExtended owner) {

    }

    @Override
    public void reset() {

    }

    @Override
    public void onOverflow(Object resource) {
        if(resource instanceof LiquidStack liquidOverflow){
            pressure += liquidOverflow.amount;
            overflow += liquidOverflow.amount;
        }
    }
}
