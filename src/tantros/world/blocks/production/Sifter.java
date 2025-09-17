package tantros.world.blocks.production;

import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.struct.Seq;
import arc.util.Eachable;
import arc.util.Nullable;
import mindustry.Vars;
import mindustry.entities.units.BuildPlan;
import mindustry.type.Item;
import mindustry.world.Tile;
import mindustry.world.blocks.production.Drill;

import static mindustry.Vars.*;

public class Sifter extends Drill {

    public float sweep_time = 200;

    /** If present, only sifts items from this list */
    public @Nullable Seq<Item> itemsWhiteList;

    public Sifter(String name) {
        super(name);
        drawRim = false;
        drawSpinSprite = false;
    }

    @Override
    public TextureRegion[] icons(){
        return new TextureRegion[]{region, rotatorRegion};
    }

    @Override
    public boolean canMine(Tile tile){
        if(tile == null || tile.block().isStatic()) return false;
        Item drops = tile.floor().itemDrop;
        return drops != null
                && drops.hardness <= tier
                && (blockedItems == null || !blockedItems.contains(drops))
                && (itemsWhiteList == null || itemsWhiteList.contains(drops));
    }

    @Override
    public Item getDrop(Tile tile) {
        return tile.floor().itemDrop;
    }

    public class SifterBuild extends DrillBuild{

        @Override
        public void draw(){

            float sweep = Mathf.sin(timeDrilled * rotateSpeed, sweep_time, Vars.tilesize * size / 3f );
            Draw.rect(rotatorRegion, x + sweep, y);
            Draw.rect(region, x, y);
        }
    }
}
