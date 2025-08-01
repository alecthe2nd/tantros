package tantros.maps.planet;

import arc.graphics.*;
import arc.math.*;
import arc.math.geom.*;
import arc.util.*;
import arc.util.noise.*;
import mindustry.content.*;
import mindustry.game.*;
import mindustry.maps.generators.*;
import mindustry.type.*;
import mindustry.world.*;

import static mindustry.Vars.*;

/**
 * A customized copy of Anuke's tantros planet generator.
*/
public class TantrosPlanetGenerator extends PlanetGenerator{
    Color c1 = Color.valueOf("5057a6"), c2 = Color.valueOf("272766");

    Block[][] arr = {
            {Blocks.shale, Blocks.shale, Blocks.sand, Blocks.stone, Blocks.redmat, Blocks.redmat},
            {Blocks.shale, Blocks.sand, Blocks.sand, Blocks.redmat, Blocks.redmat, Blocks.stone},
            {Blocks.shale, Blocks.sand, Blocks.bluemat, Blocks.redmat, Blocks.sand, Blocks.stone},
            {Blocks.shale, Blocks.bluemat, Blocks.bluemat, Blocks.sand, Blocks.sand, Blocks.ice},
            {Blocks.bluemat, Blocks.bluemat, Blocks.shale, Blocks.sand, Blocks.ice, Blocks.ice}
    };

    {
        baseSeed = 1;
    }

    @Override
    public float getHeight(Vec3 position){
        return 0;
    }

    @Override
    public void getColor(Vec3 position, Color out){
        float depth = Simplex.noise3d(seed, 2, 0.56, 1.7f, position.x, position.y, position.z) / 2f;
        out.set(c1).lerp(c2, Mathf.clamp(Mathf.round(depth, 0.15f))).a(1f - 0.2f).toFloatBits();
    }

    @Override
    public float getSizeScl(){
        return 2000;
    }

    @Override
    public void addWeather(Sector sector, Rules rules){
        //no weather... yet
    }

    @Override
    public void genTile(Vec3 position, TileGen tile){
        tile.floor = getBlock(position);

        if(tile.floor == Blocks.redmat && rand.chance(0.1)){
            tile.block = Blocks.redweed;
        }

        if(tile.floor == Blocks.bluemat && rand.chance(0.03)){
            tile.block = Blocks.purbush;
        }

        if(tile.floor == Blocks.bluemat && rand.chance(0.002)){
            tile.block = Blocks.yellowCoral;
        }

        if(Ridged.noise3d(seed + 1, position.x, position.y, position.z, 4, 14f) > 0.31){
            tile.block = tile.floor.asFloor().wall;
        }
    }

    @Override
    protected void generate(){
        pass((x, y) -> {
            float max = 0;
            for(Point2 p : Geometry.d8){
                max = Math.max(max, world.getDarkness(x + p.x, y + p.y));
            }
            if(max > 0){
                block = floor.asFloor().wall;
            }

            if(noise(x, y, 40f, 1f) > 0.9){
                //block = Blocks.coralChunk;
            }
        });

        cells(4);

        int spawnX = width / 2,
                spawnY = height / 2;

        erase(spawnX, spawnY, 15);

        inverseFloodFill(tiles.getn(spawnX, spawnY));

        Schematics.placeLaunchLoadout(spawnX, spawnY);


    }

    float rawHeight(Vec3 position){
        return Simplex.noise3d(seed, 8, 0.7f, 1f, position.x, position.y, position.z);
    }

    Block getBlock(Vec3 position){
        float height = rawHeight(position);
        Tmp.v31.set(position);
        position = Tmp.v33.set(position).scl(2f);
        float temp = Simplex.noise3d(seed, 8, 0.6, 1f/2f, position.x, position.y + 99f, position.z);
        height *= 1.2f;
        height = Mathf.clamp(height);

        //float tar = (float)noise.octaveNoise3D(4, 0.55f, 1f/2f, position.x, position.y + 999f, position.z) * 0.3f + Tmp.v31.dst(0, 0, 1f) * 0.2f;

        return arr[Mathf.clamp((int)(temp * arr.length), 0, arr[0].length - 1)][Mathf.clamp((int)(height * arr[0].length), 0, arr[0].length - 1)];
    }
}
