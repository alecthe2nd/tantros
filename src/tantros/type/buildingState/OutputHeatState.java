package tantros.type.buildingState;

import arc.scene.ui.layout.Table;
import mindustry.Vars;
import mindustry.graphics.Pal;
import mindustry.ui.Bar;
import mindustry.world.blocks.heat.HeatProducer;
import tantros.type.blockConfig.HeatProductionConfig;
import tantros.ui.UIUtil;
import tantros.util.io.ReadContext;
import tantros.util.io.WriteContext;
import tantros.world.blocks.BlockExtended;

public class OutputHeatState implements BuildingState{

    public HeatProductionConfig productionConfig;

    public float heat;
    public float[] sideHeat = new float[4];

    @Override
    public void initState(BlockExtended ownerType, BlockExtended.BuildExtended owner) {
        reset();
        productionConfig = ownerType.getBlockConfig(HeatProductionConfig.class);
    }

    @Override
    public void update(BlockExtended ownerType, BlockExtended.BuildExtended owner) {
        if(owner.efficiency <= 0 ){
            this.heat = 0;
            for(int i = 0; i < 4; i++){
                this.sideHeat[i] = 0;
            }
        }
    }

    @Override
    public void onProximity(BlockExtended ownerType, BlockExtended.BuildExtended owner) {

    }

    @Override
    public boolean isTransient() {
        return false;
    }

    @Override
    public void write(WriteContext write) {
        write.f(this.heat);
        for(int i = 0; i < 4; i++){
            write.f(sideHeat[i]);
        }
    }

    @Override
    public void read(ReadContext read) {
        this.heat = read.f();
        for(int i = 0; i < 4; i++){
            sideHeat[i] = read.f();
        }
    }

    @Override
    public void reset() {
        this.heat = 0;
        for(int i = 0; i < 4; i++){
            this.heat = 0;
        }
    }

    @Override
    public void displayBars(BlockExtended.BuildExtended build, Table table) {

        UIUtil.addBar(table,
                new Bar(
                    "bar.heat",
                    Pal.lightOrange,
                    () -> this.heat / this.productionConfig.heatOutput
                )
        );
    }
}
