package tantros.world.blocks.distribution.payload;

import arc.Core;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.geom.Geometry;
import arc.util.Tmp;
import mindustry.Vars;
import mindustry.graphics.Layer;
import mindustry.world.blocks.payloads.PayloadConveyor;

public class SealedPayloadConveyor extends PayloadConveyor {

    public TextureRegion topsideRegion;
    public TextureRegion sealRegion;

    public SealedPayloadConveyor(String name) {
        super(name);
    }

    @Override
    public void load() {
        super.load();
        topsideRegion = Core.atlas.find(name + "-topside");
        sealRegion = Core.atlas.find(name + "-seal");
    }

    public class SealedPayloadBuild extends PayloadConveyorBuild{

        @Override
        public void draw() {
            super.draw();
            float z = Draw.z();
            Draw.z(Layer.blockOver+2);
            for(int i = 0; i < 4; i++){
                if(!blends(i)){
                    Draw.rect(topsideRegion, x, y, i * 90);
                } else {
                    Tmp.v1.set(x, y).add(Geometry.d4x(i) * Vars.tilesize * size / 2f, Geometry.d4y(i) * Vars.tilesize * size / 2f);
                    Draw.rect(sealRegion, Tmp.v1.x, Tmp.v1.y, i * 90);
                }
            }
            Draw.z(z);
        }
    }
}
