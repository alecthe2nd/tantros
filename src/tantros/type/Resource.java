package tantros.type;

import arc.struct.Seq;
import mindustry.type.*;

public class Resource {

    public Seq<ItemStack> items = new Seq<>();

    public Seq<LiquidStack> liquids = new Seq<>();

    public float power;

    public float heat;

    public Seq<PayloadStack> payloads = new Seq<>();

    public Resource(){}

    public Resource withItems(ItemStack... stacks){
        this.items.add(stacks);
        return this;
    }

    public Resource withLiquids(LiquidStack... stacks){
        this.liquids.add(stacks);
        return this;
    }

    public Resource withPayloads(PayloadStack... stacks){
        this.payloads.add(stacks);
        return this;
    }

    public Resource withPower(float power){
        this.power = power;
        return this;
    }

    public Resource withHeat(float heat){
        this.heat = heat;
        return this;
    }
}
