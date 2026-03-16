package tantros.world.draw.extended;

import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.util.Eachable;
import arc.util.Log;
import arc.util.Nullable;
import mindustry.entities.units.BuildPlan;
import mindustry.gen.Building;
import mindustry.graphics.Drawf;
import mindustry.graphics.Layer;
import mindustry.type.UnitType;
import mindustry.world.Block;

public class DrawUnit extends DrawBlockExtended{public TextureRegion region;

    public UnitType unitType;

    public String suffix = "";
    /** If set, overrides the region name. */
    public @Nullable String name;
    public @Nullable Color color;
    public boolean spinSprite = false;
    public boolean drawPlan = true;
    public boolean buildingRotate = false;
    public float rotateSpeed, x, y, rotation;
    /** Any number <=0 disables layer changes. */
    public float layer = -1;

    public DrawUnit(UnitType unitType){
        this.unitType = unitType;
        name = unitType.name;
        buildingRotate = true;
        rotation = -90;
    }

    @Override
    public void draw(Building build){
        float z = Draw.z();

        if(z >= Layer.blockOver && z < Layer.groundUnit -1){
            Draw.z(Layer.blockOver + 1);
        } else {
            if(layer > 0) Draw.z(layer);
        }
        if(color != null) Draw.color(color);
        if(spinSprite){
            Drawf.spinSprite(region, build.x + x, build.y + y, build.totalProgress() * rotateSpeed + rotation + (buildingRotate ? build.rotdeg() : 0) + build.payloadRotation);
        }else{
            Draw.rect(region, build.x + x, build.y + y, build.totalProgress() * rotateSpeed + rotation + (buildingRotate ? build.rotdeg() : 0) + build.payloadRotation);
        }
        if(color != null) Draw.color();
        //Log.info("Drew unit: " + Draw.z() + " | " + z + ";");
        Draw.z(z);
    }

    @Override
    public void drawPlan(Block block, BuildPlan plan, Eachable<BuildPlan> list){
        if(!drawPlan) return;
        if(spinSprite){
            Drawf.spinSprite(region, plan.drawx() + x, plan.drawy() + y, (buildingRotate ? plan.rotation * 90f : 0 + rotation));
        }else{
            Draw.rect(region, plan.drawx()+ x, plan.drawy() + y, (buildingRotate ? plan.rotation * 90f : 0 + rotation));
        }
    }

    @Override
    public TextureRegion[] icons(Block block){
        return new TextureRegion[]{region};
    }

    @Override
    public void load(Block block){
        region = Core.atlas.find(name != null ? name : block.name + suffix);
    }
}
