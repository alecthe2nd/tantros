package tantros.world;

import arc.Events;
import arc.func.Cons;
import arc.func.Cons2;
import arc.func.Prov;
import arc.math.Mathf;
import arc.math.geom.QuadTree;
import arc.math.geom.Rect;
import arc.math.geom.Vec2;
import arc.struct.GridMap;
import arc.struct.ObjectMap;
import arc.util.pooling.Pool;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.world.Tile;
import tantros.TantrosVars;
import tantros.world.blocks.environment.DeepOreBlock;

/*
* An indexer that tracks all tiles enclosed in the range of some area field.
*
* Designed for use by sonar buildings and unit abilities, may be useful in other areas too.
* */
public class RangeIndexer {

    /**
     * A storage structure for Units to retrieve their range holder's by id.
     */
    public ObjectMap<Integer, RangeHolder> unitRanges;
    public GridMap<Boolean> map = new GridMap<>();
    public QuadTree<RangeHolder> tree = new QuadTree<>(new Rect(0,0,Vars.world.unitWidth(), Vars.world.unitHeight()));

    public RangeIndexer(){
        Events.on(EventType.WorldLoadEvent.class, this::onWorldLoadEvent);
        Events.on(EventType.ResetEvent.class, this::onResetEvent);
    }

    public void onWorldLoadEvent(EventType.WorldLoadEvent event){
        this.clear();
    }

    public void onResetEvent(EventType.ResetEvent event){
        this.clear();
    }

    public boolean get(int x, int y){
        return map.get(x, y, false);
    }

    public void set(Tile tile, boolean value ){
        tile.recache();
        map.put(tile.x, tile.y, value);
    }

    public void clear(){
        map.clear();
    }

    public static class RangeHolder implements Pool.Poolable, QuadTree.QuadTreeObject {

        public static Vec2 tempVec = new Vec2();

        float lastRange = 0;
        Vec2 lastPos = new Vec2(-1,-1);
        public Cons<Rect> hitbox;

        public RangeHolder(){
            this((rect)->{});
        }

        public RangeHolder(Cons<Rect> hitbox){
            this.hitbox = hitbox;
        }

        @Override
        public void reset() {
            lastRange = 0;
            lastPos.set(-1,-1);
        }

        public boolean dirty(float range, Vec2 pos){
            float rangeDiff = Math.abs(range - lastRange)/Vars.tilesize;
            float posDiff = tempVec.set(lastPos).dst2(pos) / Vars.tilesize;
            return rangeDiff >= 1 || posDiff >= 1;
        }

        public void apply(RangeIndexer indexer, float radius, Vec2 pos){
            if(!dirty(radius, pos)) return;
            applyInRange(indexer::set, radius, pos);
            lastRange = radius;
            lastPos.set(pos);
        }

        public void clear(RangeIndexer indexer){
            forceApplyInRange((tile)->{
                indexer.set(tile, false);
            });
        }

        public void applyInRange(Cons2<Tile, Boolean> effect, float radius, Vec2 pos){
            int range = Mathf.ceil(radius / Vars.tilesize);
            for (int i = -range; i < range + 1; i++) {
                for (int j = -range; j < range + 1; j++) {
                    Tile prevTile = Vars.world.tileWorld(lastPos.x + (i * Vars.tilesize), lastPos.y + (j * Vars.tilesize));
                    if (prevTile != null) {
                        if (exclusivelyIn(prevTile, lastRange, lastPos, radius, pos)) {
                            effect.get(prevTile, false);
                        }
                    }

                    Tile tile = Vars.world.tileWorld(pos.x + (i * Vars.tilesize), pos.y + (j * Vars.tilesize));
                    if (tile != null) {
                        if ( exclusivelyIn(tile, radius, pos, lastRange, lastPos)) {
                            effect.get(tile, true);
                        }
                    }
                }
            }
        }

        public void forceApplyInRange(Cons<Tile> effect){
            int range = Mathf.ceil(lastRange / Vars.tilesize);
            for (int i = -range; i < range + 1; i++) {
                for (int j = -range; j < range + 1; j++) {
                    Tile prevTile = Vars.world.tileWorld(lastPos.x + (i * Vars.tilesize), lastPos.y + (j * Vars.tilesize));
                    if (prevTile != null) {
                        if (prevTile.within(lastPos, lastRange)) {
                            effect.get(prevTile);
                        }
                    }
                }
            }
        }

        /**
         * Compute both ranges, and determine whether the tile resides in the first range without also being in the second one.
        */
        public static boolean exclusivelyIn(Tile tile, float wantedRange, Vec2 wantedPos, float exclusionRange, Vec2 exclusionPos){
            return tile.within(wantedPos, wantedRange) && !tile.within(exclusionPos, exclusionRange);
        }

        @Override
        public void hitbox(Rect out) {
            this.hitbox.get(out);
        }
    }

}
