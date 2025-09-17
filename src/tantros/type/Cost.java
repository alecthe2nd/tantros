package tantros.type;

import arc.struct.Seq;
import arc.util.io.Writes;
import mindustry.type.*;

public class Cost {

    public Seq<ItemStack> items = new Seq<>();

    public Seq<LiquidStack> liquids = new Seq<>();

    public float power;

    public float heat;

    public Seq<PayloadStack> payloads = new Seq<>();

    public Cost(){}

    public Cost withItems(ItemStack... stacks){
        this.items.add(stacks);
        return this;
    }

    public Cost withLiquids(LiquidStack... stacks){
        this.liquids.add(stacks);
        return this;
    }

    public Cost withPayloads(PayloadStack... stacks){
        this.payloads.add(stacks);
        return this;
    }

    public Cost withPower(float power){
        this.power = power;
        return this;
    }

    public Cost withHeat(float heat){
        this.heat = heat;
        return this;
    }
}
