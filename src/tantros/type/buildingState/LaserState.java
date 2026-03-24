package tantros.type.buildingState;

import arc.func.Boolf;
import arc.math.geom.Geometry;
import arc.math.geom.Point2;
import arc.util.Log;
import mindustry.world.Tile;
import tantros.world.blocks.BlockExtended;
import tantros.world.blocks.production.ProductionBlock;

import static mindustry.Vars.world;

public class LaserState implements BuildingState {
    public Tile[] facing = null;
    public Point2[] lasers = null;
    public int facingAmount = 0;
    public int range = 5;
    public int size = 1;

    public Boolf<Tile> oreCondition = (t)-> true;

    @Override
    public void initState(BlockExtended ownerType, BlockExtended.BuildExtended owner) {
        this.size = ownerType.size;
        this.facing = new Tile[ownerType.size];
        this.lasers = new Point2[ownerType.size];
    }

    @Override
    public void update(BlockExtended ownerType, BlockExtended.BuildExtended owner) {
        if(lasers == null || facing == null){
            Log.err("Block " + owner + "tried to update its facing state before initializing it.");
            this.facing = new Tile[ownerType.size];
            this.lasers = new Point2[ownerType.size];
        }
        if(lasers.length > 0 && lasers[0] == null){
            updateLasers(ownerType,owner);
        }
        updateFacing(ownerType,owner);
    }

    @Override
    public void onProximity(BlockExtended ownerType, BlockExtended.BuildExtended owner) {
        if(lasers == null || facing == null){
            Log.err("Block " + owner + "tried to update its facing state before initializing it.");
            this.facing = new Tile[ownerType.size];
            this.lasers = new Point2[ownerType.size];
        }
        updateLasers(ownerType,owner);
        updateFacing(ownerType,owner);
    }

    @Override
    public String getName() {
        return "LaserState";
    }

    @Override
    public int getVersion() {
        return 0;
    }

    @Override
    public void reset() {
        this.facing = new Tile[this.size];
        this.lasers = new Point2[this.size];
    }

    public void updateLasers(BlockExtended ownerType, BlockExtended.BuildExtended owner){
        for(int i = 0; i < ownerType.size; i++){
            if(lasers[i] == null) lasers[i] = new Point2();
            ownerType.nearbySide(owner.tileX(), owner.tileY(), owner.rotation, i, lasers[i]);
        }
    }

    public void updateFacing(BlockExtended ownerType, BlockExtended.BuildExtended owner){
        //lastItem = null;
        boolean multiple = false;
        int dx = Geometry.d4x(owner.rotation), dy = Geometry.d4y(owner.rotation);
        facingAmount = 0;

        //update facing tiles
        for(int p = 0; p < ownerType.size; p++){
            Point2 l = lasers[p];
            Tile dest = null;
            for(int i = 0; i < range; i++){
                int rx = l.x + dx*i, ry = l.y + dy*i;
                Tile other = world.tile(rx, ry);
                if(other != null){
                    if(other.solid()){
                        if(oreCondition.get(other)){
                            facingAmount ++;
                            dest = other;
                        }
                        break;
                    }
                }
            }

            facing[p] = dest;
        }
    }
}
