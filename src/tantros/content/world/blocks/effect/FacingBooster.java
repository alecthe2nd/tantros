package tantros.content.world.blocks.effect;

import arc.Core;
import arc.math.Mathf;
import arc.struct.EnumSet;
import arc.struct.Seq;
import mindustry.gen.Building;
import mindustry.gen.Sounds;
import mindustry.graphics.Pal;
import mindustry.ui.Bar;
import mindustry.world.Block;
import mindustry.world.consumers.ConsumeItems;
import mindustry.world.meta.BlockFlag;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatUnit;
import mindustry.world.meta.StatValues;
import tantros.content.world.blocks.distribution.BoostDuct;

import static mindustry.Vars.tilesize;

public class FacingBooster extends Block {

    public Seq<Block> boostables = null;

    public float boost = 1f;

    public float reload = 60f;

    public FacingBooster(String name) {
        super(name);
        update = true;
        solid = true;
        ambientSound = Sounds.machine;
        sync = true;
        ambientSoundVolume = 0.03f;
        drawArrow = true;
        rotate = true;
    }

    @Override
    public void setStats(){
        super.setStats();

        stats.add(Stat.speedIncrease, "+" + (int)(boost * 100f - 100) + "%");
    }

    @Override
    public void setBars(){
        super.setBars();
        addBar("boost", (FacingBooster.FacingBoosterBuild entity) -> new Bar(() -> Core.bundle.format("bar.boost", Mathf.round(Math.max((entity.realBoost() * 100 - 100), 0))), () -> Pal.accent, () -> entity.realBoost() / (boost)));
    }

    public class FacingBoosterBuild extends Building{

        public float realBoost(){
            return boost * efficiency;
        }

        @Override
        public void updateTile() {
            if (efficiency > 0) {
                Building front = this.front();
                if (front!= null && front.block.canOverdrive && (boostables == null || boostables.contains(front.block))) {
                    front.applyBoost(realBoost(), reload + 1f);
                    consume();
                }
                super.updateTile();
            }
        }
    }

}
