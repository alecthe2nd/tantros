package tantros.content.world.blocks.production;

import arc.Core;
import arc.math.Mathf;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.entities.Damage;
import mindustry.entities.Effect;
import mindustry.graphics.Pal;
import mindustry.type.Item;
import mindustry.ui.Bar;
import mindustry.world.blocks.production.GenericCrafter;
import tantros.content.world.blocks.distribution.BoostDuct;

import static mindustry.Vars.*;
import static mindustry.Vars.state;

public class Boiler extends GenericCrafter {

    public float stressCapacity = 30f;

    public float stressExplosiveness = 0.5f;

    public float pressureDamage = 1f;

    public Boiler(String name) {
        super(name);
        ignoreLiquidFullness = true;
    }

    @Override
    public void setBars(){
        super.setBars();
        addBar("pressure", (Boiler.BoilerBuild entity) -> new Bar(() -> Core.bundle.format("bar.pressure", Mathf.round(Math.max((entity.pressure), 0))), () -> Pal.lightishGray, () -> entity.pressure / stressCapacity));
    }

    public class BoilerBuild extends GenericCrafterBuild{

        public float pressure = 0.0f;

        /** Called when the block is destroyed. The tile is still intact at this stage. */
        @Override
        public void onDestroyed(){

            float explosiveness = block.baseExplosiveness;
            float flammability = 0f;
            float power = 0f;

            if(block.hasItems){
                for(Item item : content.items()){
                    int amount = Math.min(items.get(item), explosionItemCap());
                    explosiveness += item.explosiveness * amount;
                    flammability += item.flammability * amount;
                    power += item.charge * Mathf.pow(amount, 1.1f) * 150f;
                }
            }

            if(block.hasLiquids){
                flammability += liquids.sum((liquid, amount) -> liquid.flammability * amount / 2f);
                explosiveness += liquids.sum((liquid, amount) -> liquid.explosiveness * amount / 2f);
            }

            if(block.consPower != null && block.consPower.buffered){
                power += this.power.status * block.consPower.capacity;
            }

            if(block.hasLiquids && state.rules.damageExplosions){
                liquids.each(this::splashLiquid);
            }

            explosiveness *= 1 + (stressExplosiveness * (pressure / stressCapacity));

            //cap explosiveness so fluid tanks/vaults don't instakill units
            Damage.dynamicExplosion(x, y, flammability * block.flammabilityScale, explosiveness * 3.5f * block.explosivenessScale, power, tilesize * block.size / 2f, state.rules.damageExplosions, block.destroyEffect, block.baseShake);

            if(block.createRubble && !floor().solid && !floor().isLiquid){
                Effect.rubble(x, y, block.size);
            }
        }

        @Override
        public void updateTile() {
            super.updateTile();
            float overflow;
            boolean didOverflow = false;
            if (efficiency > 0){
                if(outputLiquids != null){
                    float inc = getProgressIncrease(1f);
                    for(var output : outputLiquids){
                        overflow = Math.max((output.amount * inc) - (liquidCapacity - liquids.get(output.liquid)), 0);
                        if (overflow / (output.amount * inc) > 0.25f){
                            pressure += overflow;
                            didOverflow = true;
                        }
                    }
                }
            }
            pressure = Math.min(Math.max(pressure,0), stressCapacity);

            if (pressure >= stressCapacity){
                damage(pressureDamage);
            }
            if (!didOverflow) {
                pressure -= 0.1f;
            }
        }

        @Override
        public void write(Writes write){
            super.write(write);
            write.f(pressure);
        }

        @Override
        public void read(Reads read, byte revision){
            super.read(read, revision);
            pressure = read.f();
        }
    }
}
