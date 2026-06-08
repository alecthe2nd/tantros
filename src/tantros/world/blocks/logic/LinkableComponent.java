package tantros.world.blocks.logic;

import arc.struct.Seq;
import mindustry.gen.Building;
import mindustry.gen.Player;
import mindustry.graphics.Drawf;
import mindustry.graphics.Pal;
import mindustry.logic.Ranged;
import mindustry.world.blocks.ConstructBlock;
import mindustry.world.meta.BlockGroup;
import tantros.type.effect.projector.range.RangeConfig;
import tantros.world.blocks.BlockExtended;

import static mindustry.Vars.tilesize;
import static mindustry.Vars.world;

public class LinkableComponent extends BlockExtended {

    public RangeConfig range = new RangeConfig(5 * tilesize);

    public LinkableComponent(String name) {
        super(name);
        update = true;
        configurable = true;
        group = BlockGroup.logic;
        underBullets = true;
        config(Integer.class, (LinkableComponentBuild entity, Integer pos) -> {

            //if there is no valid link in the first place, nobody cares
            if(!entity.validLink(world.build(pos))) return;
            var lbuild = world.build(pos);
            int x = lbuild.tileX(), y = lbuild.tileY();

            Link link = entity.links.find(l -> l.x == x && l.y == y);

            if(link != null){
                entity.links.remove(link);
            }else{
                entity.links.remove(l -> world.build(l.x, l.y) == lbuild);
                entity.links.add(new Link(x, y, true));
            }
        });
    }

    @Override
    public void init() {
        super.init();
        putBlockConfig(range);
    }

    public class LinkableComponentBuild extends BuildExtended implements Ranged {

        public Seq<Link> links = new Seq<>();

        public boolean validLink(Building other){
            return other != null && other.isValid() && (privileged || (!other.block.privileged && other.team == team && other.within(this, range.maxScale + other.block.size*tilesize/2f))) && !(other instanceof ConstructBlock.ConstructBuild);
        }

        @Override
        public float range() {
            return range.maxScale;
        }

        @Override
        public void drawConfigure(){
            super.drawConfigure();

            if(!privileged){
                Drawf.circles(x, y, range.maxScale);
            }

            for(Link l : links){
                Building build = world.build(l.x, l.y);
                if(validLink(build)){
                    Drawf.square(build.x, build.y, build.block.size * tilesize / 2f + 1f, Pal.place);
                }
            }
        }

        @Override
        public boolean onConfigureBuildTapped(Building other){
            if(this == other){
                deselect();
                return false;
            }

            if(validLink(other)){
                configure(other.pos());
                return false;
            }

            return super.onConfigureBuildTapped(other);
        }

        @Override
        public boolean shouldShowConfigure(Player player){
            return true;
        }
    }

    public class Link{
        public int x,y;
        public boolean valid;
        public Building build;

        public Link(int x, int y, boolean valid){
            this.x = x;
            this.y = y;
            this.valid = valid;
        }

        public Link copy(){
            return new Link(x, y, valid);
        }
    }
}
