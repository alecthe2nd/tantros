package tantros.world.blocks.environment;

import arc.graphics.g2d.Draw;
import mindustry.Vars;
import mindustry.graphics.Layer;
import mindustry.type.Item;
import mindustry.type.Liquid;
import mindustry.world.Tile;
import mindustry.world.blocks.environment.OverlayFloor;
import tantros.TantrosVars;
import tantros.world.blocks.effect.GroundPenetratingRadar;

public class DeepOreBlock extends OverlayFloor {

    public Item deepDrop;
    public Liquid deepWell;

    public DeepOreBlock(String name, Item ore, Liquid well) {
        super(name +
                ((ore != null)?("-" + ore.name): "") +
                ((well != null)?("-" + well.name): ""));
        deepDrop = ore;
        deepWell = well;
    }

    public DeepOreBlock(String name, Item ore) {
        this(name, ore, null);
    }

    public DeepOreBlock(String name, Liquid well) {
        this(name, null, well);
    }

    public DeepOreBlock(Item ore, Liquid well) {
        this("deep-ore", ore, well);
    }

    public DeepOreBlock(Item ore) {
        this(ore, null);
    }

    public DeepOreBlock(Liquid well) {
        this((Item)null, well);
    }

    public DeepOreBlock(String name) {
        super(name);
        deepDrop = null;
    }

    @Override
    public boolean updateRender(Tile tile) {
        return super.updateRender(tile);
    }

    @Override
    public void drawBase(Tile tile) {
        //if(tile instanceof EditorTile){
        boolean revealed = TantrosVars.sonarTracking.get(Vars.player.team(), tile.worldx(), tile.worldy());
        if(revealed){
            super.drawBase(tile);
        }
        //}
    }

    public void drawDeep(Tile tile){
        Draw.z(Layer.floor);
        super.drawBase(tile);
    }
}
