package tantros.type.blockConfig;

public class HeatConsumptionConfig implements BlockConfig{

    public float minimumHeat = 0;

    public float heatPerEfficiency = 1;

    public float maximumHeat = 4;

    public float overheatScale = 1;

    public HeatConsumptionConfig(float heatPerEfficiency) {
        this(heatPerEfficiency, 4);
    }

    public HeatConsumptionConfig(float heatPerEfficiency, float maxEfficiency) {
        this(0, heatPerEfficiency, heatPerEfficiency * maxEfficiency);
    }

    public HeatConsumptionConfig(float minimumHeat, float heatPerEfficiency, float maximumHeat) {
        this.minimumHeat = minimumHeat;
        this.heatPerEfficiency = heatPerEfficiency;
        this.maximumHeat = maximumHeat;
    }

    public float heatRequired(){
        return heatPerEfficiency;
    }

    public float computeEfficiency(float heat){
        if(heat < minimumHeat) return 0;
        if(heat > maximumHeat) return computeEfficiency(maximumHeat);
        if(heat > heatPerEfficiency){
            float over = Math.max(heat - heatPerEfficiency, 0f);
            return computeEfficiency(heatPerEfficiency) + over / heatPerEfficiency * overheatScale;
        }
        return (heat/heatPerEfficiency);
    }
}
