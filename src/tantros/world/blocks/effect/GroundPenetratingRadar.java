package tantros.world.blocks.effect;

import arc.Core;
import arc.func.Cons;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.util.Time;
import mindustry.Vars;
import mindustry.world.Tile;
import mindustry.world.blocks.defense.Radar;
import tantros.TantrosVars;
import tantros.graphics.TantrosLayers;
import tantros.world.blocks.environment.DeepOreBlock;

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

    public class GroundPenetratingRadarBuild extends RadarBuild{

        public boolean init = false;

        @Override
        public void onRemoved() {
            super.onRemoved();
            this.applyInRange(Tile::recache, fogRadius);
        }

        @Override
        public void created() {
            super.created();
            this.applyInRange(Tile::recache, fogRadius);
        }

        @Override
        public void updateTile() {

            if(efficiency < 0.01 && fogRadius() > 1){
                applyInRange(Tile::recache, fogRadius);
            } else if (fogRadius() < block.fogRadius) {
                applyInRange(Tile::recache);
            } else if (!init){
                init = true;
                applyInRange(Tile::recache, fogRadius);
            }

            super.updateTile();

        }

        public void applyInRange(Cons<Tile> effect){
            applyInRange(effect, fogRadius());
        }

        public void applyInRange(Cons<Tile> effect, float radius){
            int range = Mathf.ceil(radius);
            for (int i = -range; i < range + 1; i++) {
                for (int j = -range; j < range + 1; j++) {
                    Tile tile = Vars.world.tile(this.tileX() + i, this.tileY() + j);
                    if (tile != null) {
                        if ( tile.dst(this) < range * Vars.tilesize && tile.overlay() != null && tile.overlay() instanceof DeepOreBlock ore) {
                            effect.get(tile);
                        }
                    }
                }
            }
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
            //Draw.color(outlineColor);
            /*Draw.alpha(0.75f);
            int range = Mathf.ceil(fogRadius());
            if (Vars.player.unit() != null && Vars.player.unit().dst(this) < range * Vars.tilesize * 2) {
                for (int i = -range; i < range + 1; i++) {
                    for (int j = -range; j < range + 1; j++) {
                        Tile tile = Vars.world.tile(this.tileX() + i, this.tileY() + j);
                        if (tile != null) {
                            if (TantrosVars.sonarTracking.ores.contains(tile.pos())) continue;

                            if ( tile.dst(this) < range * Vars.tilesize && tile.overlay() != null && tile.overlay() instanceof DeepOreBlock ore) {

                                ore.drawDeep(tile);
                                TantrosVars.sonarTracking.ores.add(tile.pos());
                            }
                        }
                    }
                }
            }
            Draw.reset();*/
        }
    }
}
