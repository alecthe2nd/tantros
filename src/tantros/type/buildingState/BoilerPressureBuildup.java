package tantros.type.buildingState;

import arc.Core;
import arc.math.Mathf;
import arc.scene.ui.layout.Table;
import mindustry.graphics.Pal;
import mindustry.type.LiquidStack;
import mindustry.ui.Bar;
import tantros.type.blockConfig.BoilerConfig;
import tantros.ui.UIUtil;
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
            pressure = Math.min(pressure + liquidOverflow.amount, boilerConfig.pressureCapacity);
            overflow += liquidOverflow.amount;
        }
    }

    public float pressureFrac(){
        return this.pressure / boilerConfig.pressureCapacity;
    }

    @Override
    public void displayBars(BlockExtended.BuildExtended build, Table table) {
        UIUtil.addBar(table,
                new Bar(
                        () -> Core.bundle.format("bar.pressure", Mathf.round(Math.max(this.pressure, 0))),
                        () -> Pal.lightishGray,
                        () -> this.pressure / boilerConfig.pressureCapacity
                )
        );
    }

    @Override
    public String getName() {
        return "BoilerPressureBuildup";
    }

    @Override
    public int getVersion() {
        return 0;
    }
}
