package tantros.world.draw.extended;

import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.util.Eachable;
import arc.util.Nullable;
import mindustry.entities.units.BuildPlan;
import mindustry.gen.Building;
import mindustry.graphics.Drawf;
import mindustry.world.Block;
import tantros.world.blocks.BlockExtended;

public class DrawRegionFlipped extends DrawBlockExtended{
    public TextureRegion region;
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

    public DrawRegionFlipped(String suffix){
        this.suffix = suffix;
    }

    public DrawRegionFlipped(String suffix, float rotateSpeed){
        this.suffix = suffix;
        this.rotateSpeed = rotateSpeed;
    }

    public DrawRegionFlipped(String suffix, float rotateSpeed, boolean spinSprite){
        this.suffix = suffix;
        this.spinSprite = spinSprite;
        this.rotateSpeed = rotateSpeed;
    }

    public DrawRegionFlipped(){
    }

    @Override
    public void draw(BlockExtended.BuildExtended build){
        float z = Draw.z();
        if(layer > 0) Draw.z(layer);
        if(color != null) Draw.color(color);
        if(spinSprite){
            Drawf.spinSprite(region, build.x + x, build.y + y, build.totalProgress() * rotateSpeed + rotation + (buildingRotate ? build.rotdeg() : 0));
        }else{
            Draw.rect(region, build.x + x, build.y + y, build.totalProgress() * rotateSpeed + rotation + (buildingRotate ? build.rotdeg() : 0));
        }
        if(color != null) Draw.color();
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
