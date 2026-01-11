package tantros.world.draw.wallDrill;

import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.graphics.g2d.TextureRegion;
import arc.math.Angles;
import arc.math.Mathf;
import arc.math.geom.Geometry;
import arc.math.geom.Point2;
import arc.util.Time;
import arc.util.Tmp;
import mindustry.gen.Building;
import mindustry.graphics.Layer;
import mindustry.type.Item;
import mindustry.world.Block;
import mindustry.world.Tile;
import mindustry.world.draw.DrawBlock;
import tantros.type.buildingState.CooldownState;
import tantros.type.buildingState.LaserState;
import tantros.world.blocks.production.ProductionBlock;

import static mindustry.Vars.tilesize;

public class DrawBoreBit extends DrawBlock {

    public TextureRegion cap;
    public TextureRegion head;
    public TextureRegion axle;
    public TextureRegion fin;
    public TextureRegion spike;

    public float headWidth = 2f;
    public float forwardThrust = 0.25f;
    public float rotationSpeed = 1f;
    public int finCount = 6;
    public int spikeCount = 2;
    public float spikeCircleFraction = 0.5f;
    public float spikeYOffset = 2f;

    public Color sparkColor = Color.valueOf("fd9e81");
    public int sparks = 7;
    public float sparkRange = 10f, sparkLife = 27f, sparkRecurrence = 4f, sparkSpread = 45f, sparkSize = 3.5f;

    private static Tile[] tempFacing;
    private static Point2[] tempLasers;

    public void load(Block block){
        cap = Core.atlas.find(block.name + "-cap");
        head = Core.atlas.find(block.name + "-head");
        axle = Core.atlas.find(block.name + "-axle");
        fin = Core.atlas.find(block.name + "-fin");
        spike = Core.atlas.find(block.name + "-spike");
    }

    public void draw(Building build) {
        int range = 2;
        float cooldown = 0;
        float productionTime = 1;
        float progressThisTick = 1;
        if (build instanceof ProductionBlock.ProductionBuild prod){
            LaserState laserState;
            if ((laserState = prod.getState(LaserState.class)) != null) {
                tempFacing = laserState.facing;
                tempLasers = laserState.lasers;
                range = laserState.range;
            } else {
                return;
            }
            CooldownState cooldownState;
            if ((cooldownState = prod.getState(CooldownState.class)) != null) {
                cooldown = cooldownState.cooldown;
            } else {
                return;
            }
            productionTime = prod.currentProductionTime;
            progressThisTick = prod.progressThisTick;
        }
        float z1 = Draw.z();
        float mainZ = Layer.block - 0.1f;
        Draw.z(mainZ);
        var dir = Geometry.d4(build.rotation);

        for(int i = 0; i < build.block.size; i++){
            float offset = Mathf.absin((build.totalProgress()*productionTime + i*5 + (build.id % 9)*9), 6f, range) * forwardThrust;

            Tile face = tempFacing[i];
            Point2 p = tempLasers[i];
            if(face == null || p == null) continue;
            Item drop = face.wallDrop();
            if(drop == null) continue;
            float reach = Math.abs((face.worldx()-p.x * tilesize)*dir.x) + Math.abs((face.worldy()-p.y * tilesize)*dir.y);
            float retraction = (1-cooldown) * reach;


            float targetx = face.worldx() - (dir.x/2f)*tilesize + dir.x *(offset-retraction), targety = face.worldy() - (dir.y/2f)*tilesize + dir.y *(offset-retraction);
            float homex = (p.x - dir.x/2f) * tilesize + dir.x *(offset-(1-cooldown) * tilesize), homey = (p.y - dir.y/2f) * tilesize + dir.y *(offset-(1-cooldown) * tilesize);

            int ddx = Geometry.d4x(build.rotation + 1), ddy = Geometry.d4y(build.rotation + 1);
            Lines.stroke(2f);
            Lines.line(axle, targetx, targety, homex, homey, false);
            Lines.stroke(1f);
            Draw.rect(head, targetx, targety, build.rotdeg());
            for(int j = 0; j < finCount; j++){

                var finDir = Geometry.d4(build.rotation + 1);
                float vx = Mathf.cosDeg(build.totalProgress()*productionTime * rotationSpeed + (360f/finCount)*j) * headWidth,
                        vy = Mathf.sinDeg(build.totalProgress()*productionTime * rotationSpeed + (360f/finCount)*j) * headWidth;
                float z2 = Draw.z();
                if(vy < 0){
                    Draw.z(z2 - 0.1f);
                }
                Draw.rect(fin, targetx + (finDir.x)*vx, targety, build.rotdeg());
                Draw.z(z2);
            }
            for(int k = 0; k < spikeCount; k++){
                var finDir = Geometry.d4(build.rotation + 1);
                float vx = Mathf.cosDeg(build.totalProgress()*productionTime * rotationSpeed + (360f/spikeCount)*k) * headWidth * spikeCircleFraction,
                        vy = Mathf.sinDeg(build.totalProgress()*productionTime * rotationSpeed + (360f/spikeCount)*k) * headWidth * spikeCircleFraction;
                float z2 = Draw.z();
                if(vy < 0){
                    Draw.z(z2 - 0.1f);
                }
                Draw.rect(spike, targetx + (finDir.x)*vx + spikeYOffset*dir.x, targety + (finDir.y)*vx + spikeYOffset*dir.y, build.rotdeg());
                Draw.z(z2);
            }

            Draw.rect(cap, homex,homey, build.rotdeg());
            Draw.z(Layer.effect);
            Lines.stroke(build.warmup());
            rand.setState(i, build.id);
            Color col = drop.color;
            Color spark = Tmp.c3.set(sparkColor);
            for(int j = 0; j < sparks; j++){
                float fin = (build.totalProgress()*productionTime / sparkLife + rand.random(sparkRecurrence + 1f)) % sparkRecurrence;
                float or = rand.range(2f);
                Tmp.v1.set(sparkRange * fin, 0).rotate(build.rotdeg() + rand.range(sparkSpread));

                Draw.color(spark, col, fin);
                float px = Tmp.v1.x, py = Tmp.v1.y;
                if(Math.abs(fin) <= 1f && Mathf.clamp(progressThisTick) > 0) Lines.lineAngle(targetx + px + or * ddx, targety + py + or * ddy, Angles.angle(px, py), Mathf.slope(fin) * sparkSize);
            }
            Draw.reset();
            Draw.z(mainZ);
        }
        Draw.z(z1);
    }

}
