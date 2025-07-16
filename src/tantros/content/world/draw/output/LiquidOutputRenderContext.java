package tantros.content.world.draw.output;

import mindustry.type.Liquid;

public class LiquidOutputRenderContext{

    private final Liquid liquid;

    private final int side;

    public LiquidOutputRenderContext(Liquid liquid, int side){
        this.liquid = liquid;
        this.side = side;
    }

    public Liquid liquid(){
        return liquid;
    }

    public int side(){
        return side;
    }

}
