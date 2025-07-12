package tantros.content.world.blocks.effect;

import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.math.Mathf;
import mindustry.Vars;
import mindustry.world.Tile;
import mindustry.world.blocks.defense.Radar;
import tantros.Tantros;
import tantros.content.world.blocks.environment.DeepOreBlock;

public class GroundPenetratingRadar extends Radar {


    public GroundPenetratingRadar(String name) {
        super(name);
    }

    public class GroundPenetratingRadarBuild extends RadarBuild{
        @Override
        public void draw() {
            super.draw();

            //Draw.color(outlineColor);
            Draw.alpha(0.75f);
            int range = Mathf.ceil(fogRadius());
            if (Vars.player.unit() != null && Vars.player.unit().dst(this) < range * Vars.tilesize * 2) {
                for (int i = -range; i < range + 1; i++) {
                    for (int j = -range; j < range + 1; j++) {
                        Tile tile = Vars.world.tile(this.tileX() + i, this.tileY() + j);
                        if (tile != null) {
                            if (Tantros.sonarTracking.ores.contains(tile.pos())) continue;

                            if ( tile.dst(this) < range * Vars.tilesize && tile.overlay() != null && tile.overlay() instanceof DeepOreBlock ore) {

                                ore.drawDeep(tile);
                                Tantros.sonarTracking.ores.add(tile.pos());
                            }
                        }
                    }
                }
            }
            Draw.reset();
        }
    }
}
