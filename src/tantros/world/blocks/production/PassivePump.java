package tantros.world.blocks.production;

import arc.math.Mathf;
import arc.util.Time;
import mindustry.type.Liquid;
import mindustry.world.Tile;
import mindustry.world.blocks.production.Pump;

public class PassivePump extends Pump {

    public Liquid result;

    public PassivePump(String name) {
        super(name);
    }

    @Override
    protected boolean canPump(Tile tile){
        return true;
    }

    public class PassivePumpBuild extends PumpBuild {

        @Override
        public boolean shouldConsume() {
            return liquids.get(result) < liquidCapacity - 0.01f && enabled;
        }

        @Override
        public void updateTile(){
            if(efficiency > 0 && result != null){
                float maxPump = Math.min(liquidCapacity - liquids.get(result), pumpAmount * edelta());
                liquids.add(result, maxPump);

                warmup = Mathf.approachDelta(warmup, maxPump > 0.001f ? 1f : 0f, warmupSpeed);
            }else{
                warmup = Mathf.approachDelta(warmup, 0f, warmupSpeed);
            }

            totalProgress += warmup * Time.delta;

            if(result != null){
                dumpLiquid(result);
            }
        }
    }
}
