package tantros.type.buildingState.logic;

import arc.func.Boolf;
import arc.struct.Seq;
import mindustry.Vars;
import mindustry.gen.Building;
import mindustry.world.blocks.ConstructBlock;
import tantros.type.blockConfig.RangeConfig;
import tantros.type.buildingState.BuildingState;
import tantros.world.blocks.BlockExtended;

import static mindustry.Vars.tilesize;
import static mindustry.Vars.world;

public class OneLink implements BuildingState, Links {

    public static Seq<Link> temp = new Seq<>();

    public static RangeConfig defaultRange = new RangeConfig(5 * tilesize);
    public Link link;
    public RangeConfig range;
    public Boolf<Building> buildingCondition = (b)->true;

    public OneLink(){

    }

    public OneLink(Boolf<Building> buildingCondition){
        this.buildingCondition = buildingCondition;
    }

    @Override
    public void initState(BlockExtended ownerType, BlockExtended.BuildExtended owner) {
        range = ownerType.getBlockConfig(RangeConfig.class);
        if(range == null) this.range = defaultRange;
    }

    @Override
    public void update(BlockExtended ownerType, BlockExtended.BuildExtended owner) {
        if(link!= null) {
            Building other = Vars.world.tile(link.x, link.y).build;
            if (!validLink(owner, other)){
                link = null;
            }

        }
    }

    @Override
    public void onProximity(BlockExtended ownerType, BlockExtended.BuildExtended owner) {

    }

    @Override
    public void reset() {

    }

    @Override
    public <E> void onConfig(BlockExtended.BuildExtended owner, E config) {
        if(config instanceof Integer pos){
            Building linked = world.build(pos);
            if(!validLink(owner, linked)) return;
            int x = linked.tileX(), y = linked.tileY();
            boolean alreadyLinked = link != null && link.x == x && link.y == y;
            if(alreadyLinked){
                link = null;
            } else {
                link = new Link(x, y, true);
            }
        }
    }

    @Override
    public String getName() {
        return "OneLink";
    }

    @Override
    public int getVersion() {
        return 0;
    }

    @Override
    public boolean validLink(BlockExtended.BuildExtended owner, Building other){
        return other != null && other.isValid() && buildingCondition.get(other) && (owner.block.privileged || (!other.block.privileged && other.team == owner.team && other.within(owner, range.range + other.block.size*tilesize/2f))) && !(other instanceof ConstructBlock.ConstructBuild);
    }

    @Override
    public Seq<Link> getLinks() {
        temp.clear();
        if(link != null) temp.add(link);
        return temp;
    }
}
