package tantros.type.buildingState;

import arc.func.Boolf;
import arc.math.geom.Geometry;
import arc.math.geom.Point2;
import arc.util.Log;
import mindustry.type.Item;
import mindustry.world.Tile;
import tantros.world.blocks.production.ProductionBlock;

import static mindustry.Vars.world;

public class LaserState implements BuildingState {
    public Tile[] facing = null;
    public Point2[] lasers = null;
    public int facingAmount = 0;
    public int range = 5;

    public Boolf<Tile> oreCondition = (t)-> true;

    @Override
    public void initState(ProductionBlock ownerType, ProductionBlock.ProductionBuild owner) {
        this.facing = new Tile[ownerType.size];
        this.lasers = new Point2[ownerType.size];
    }

    @Override
    public void update(ProductionBlock ownerType, ProductionBlock.ProductionBuild owner) {
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
    public void onProximity(ProductionBlock ownerType, ProductionBlock.ProductionBuild owner) {
        updateLasers(ownerType,owner);
        updateFacing(ownerType,owner);
    }

    public void updateLasers(ProductionBlock ownerType, ProductionBlock.ProductionBuild owner){
        for(int i = 0; i < ownerType.size; i++){
            if(lasers[i] == null) lasers[i] = new Point2();
            ownerType.nearbySide(owner.tileX(), owner.tileY(), owner.rotation, i, lasers[i]);
        }
    }

    public void updateFacing(ProductionBlock ownerType, ProductionBlock.ProductionBuild owner){

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
