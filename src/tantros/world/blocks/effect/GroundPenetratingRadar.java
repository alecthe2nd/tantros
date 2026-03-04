package tantros.world.blocks.effect;

import arc.Core;
import arc.func.Floatp;
import arc.func.Prov;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.math.geom.Rect;
import arc.math.geom.Vec2;
import arc.util.Log;
import mindustry.Vars;
import mindustry.game.Team;
import mindustry.world.blocks.defense.Radar;
import tantros.graphics.TantrosLayers;
import tantros.graphics.overlays.SonarTracking;

public class GroundPenetratingRadar extends Radar {

    TextureRegion scanRegion;

    public GroundPenetratingRadar(String name) {
        super(name);
    }

    @Override
    public void load() {
        super.load();
        scanRegion = Core.atlas.find("circle-shadow");
    }

    public class GroundPenetratingRadarBuild extends RadarBuild {

        protected final Vec2 tempPos = new Vec2();

        public SonarWrapper wrapper;

        @Override
        public void created() {
            super.created();
            wrapper = new SonarWrapper(
                    () -> tempPos.set(this.x, this.y),
                    () -> fogRadius() * Vars.tilesize,
                    this.team,
                    block.fogRadius * Vars.tilesize
            );
        }



        @Override
        public void updateTile() {
            super.updateTile();
            wrapper.team = this.team;
            wrapper.checkDirty();
        }

        @Override
        public void draw() {
            super.draw();
            Draw.draw(TantrosLayers.radarMaskLayer, ()->{
                Draw.color(outlineColor);
                Draw.scl(fogRadius() / 2f);
                Draw.rect(scanRegion, this.x, this.y);
                Draw.reset();
                Draw.color();
            });
        }


    }
}
