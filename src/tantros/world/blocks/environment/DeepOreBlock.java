package tantros.world.blocks.environment;

import arc.graphics.g2d.Draw;
import arc.util.Time;
import mindustry.Vars;
import mindustry.editor.EditorTile;
import mindustry.graphics.Layer;
import mindustry.graphics.Shaders;
import mindustry.type.Item;
import mindustry.type.Liquid;
import mindustry.world.Tile;
import mindustry.world.blocks.environment.OreBlock;
import mindustry.world.blocks.environment.OverlayFloor;
import tantros.TantrosVars;
import tantros.graphics.TantrosShaders;
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
        GroundPenetratingRadar.GroundPenetratingRadarBuild build = TantrosVars.sonarIndexer.get((b)-> tile.dst(b) < b.fogRadius() * Vars.tilesize);
        if(build != null){
            super.drawBase(tile);
        }
        //}
    }

    public void drawDeep(Tile tile){
        Draw.z(Layer.floor);
        super.drawBase(tile);
    }
}
