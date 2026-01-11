package tantros.world.draw.wallDrill;

import arc.Core;
import arc.graphics.Blending;
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
import mindustry.graphics.Drawf;
import mindustry.graphics.Layer;
import mindustry.type.Item;
import mindustry.world.Block;
import mindustry.world.Tile;
import mindustry.world.draw.DrawBlock;
import tantros.type.buildingState.LaserState;
import tantros.world.blocks.production.ProductionBlock;

import static mindustry.Vars.tilesize;

public class DrawBoreBeams extends DrawBlock {

    public float laserWidth = 0.65f;

    public Color sparkColor = Color.valueOf("fd9e81"), glowColor = Color.white;
    public float glowIntensity = 0.2f, pulseIntensity = 0.07f;
    public float glowScl = 3f;
    public int sparks = 7;
    public float sparkRange = 10f, sparkLife = 27f, sparkRecurrence = 4f, sparkSpread = 45f, sparkSize = 3.5f;

    public Color boostHeatColor = Color.sky.cpy().mul(0.87f);
    public Color heatColor = new Color(1f, 0.35f, 0.35f, 0.9f);
    public float heatPulse = 0.3f, heatPulseScl = 7f;


    public TextureRegion laser;
    public TextureRegion laserEnd;
    public TextureRegion laserCenter;

    public TextureRegion laserBoost;
    public TextureRegion laserEndBoost;
    public TextureRegion laserCenterBoost;

    public TextureRegion topRegion;
    public TextureRegion glowRegion;

    private static Tile[] tempFacing;
    private static Point2[] tempLasers;

    @Override
    public void load(Block block) {
        super.load(block);
        laser = Core.atlas.find(block.name + "-beam");
        laserEnd = Core.atlas.find(block.name + "-beam-end");
        laserCenter = Core.atlas.find(block.name + "-beam-center");
        laserBoost = Core.atlas.find(block.name + "-beam-boost");
        laserEndBoost = Core.atlas.find(block.name + "-beam-boost-end");
        laserCenterBoost = Core.atlas.find(block.name + "-beam-boost-center");
        topRegion = Core.atlas.find(block.name + "-top");
        glowRegion = Core.atlas.find(block.name + "-glow");
    }

    @Override
    public void draw(Building build) {
        super.draw(build);
        LaserState laserState;
        if(build instanceof ProductionBlock.ProductionBuild prod && (laserState = prod.getState(LaserState.class)) != null){
            tempFacing = laserState.facing;
            tempLasers = laserState.lasers;
        } else {
            if(tempFacing == null){
                tempFacing = new Tile[build.block.size];
            }
            if(tempLasers == null){
                tempLasers = new Point2[build.block.size];
            }
            for(int i = 0; i< build.block.size; i++){
                tempFacing[i] = null;
                tempLasers[i] = null;
            }
        }
        float boostWarmup = 0;

        Draw.rect(topRegion, build.x, build.y, build.rotdeg());

        if(build.isPayload()) return;

        var dir = Geometry.d4(build.rotation);
        int ddx = Geometry.d4x(build.rotation + 1), ddy = Geometry.d4y(build.rotation + 1);

        for(int i = 0; i < build.block.size; i++){

            Tile face = tempFacing[i];
            if(face != null){
                Item drop = face.wallDrop();

                if(drop == null) continue;
                Point2 p = tempLasers[i];
                float lx = face.worldx() - (dir.x/2f)*tilesize, ly = face.worldy() - (dir.y/2f)*tilesize;

                float width = (laserWidth + Mathf.absin(Time.time + i*5 + (build.id % 9)*9, glowScl, pulseIntensity)) * build.warmup();

                Draw.z(Layer.power - 1);
                Draw.mixcol(glowColor, Mathf.absin(Time.time + i*5 + build.id*9, glowScl, glowIntensity));
                if(Math.abs(p.x - face.x) + Math.abs(p.y - face.y) == 0){
                    Draw.scl(width);

                    if(boostWarmup < 0.99f){
                        Draw.alpha(1f - boostWarmup);
                        Draw.rect(laserCenter, lx, ly);
                    }

                    if(boostWarmup > 0.01f){
                        Draw.alpha(boostWarmup);
                        Draw.rect(laserCenterBoost, lx, ly);
                    }

                    Draw.scl();
                }else{
                    float lsx = (p.x - dir.x/2f) * tilesize, lsy = (p.y - dir.y/2f) * tilesize;

                    if(boostWarmup < 0.99f){
                        Draw.alpha(1f - boostWarmup);
                        Drawf.laser(laser, laserEnd, lsx, lsy, lx, ly, width);
                    }

                    if(boostWarmup > 0.001f){
                        Draw.alpha(boostWarmup);
                        Drawf.laser(laserBoost, laserEndBoost, lsx, lsy, lx, ly, width);
                    }
                }
                Draw.color();
                Draw.mixcol();

                Draw.z(Layer.effect);
                Lines.stroke(build.warmup());
                rand.setState(i, build.id);
                Color col = drop.color;
                Color spark = Tmp.c3.set(sparkColor).lerp(boostHeatColor, boostWarmup);
                for(int j = 0; j < sparks; j++){
                    float fin = (Time.time / sparkLife + rand.random(sparkRecurrence + 1f)) % sparkRecurrence;
                    float or = rand.range(2f);
                    Tmp.v1.set(sparkRange * fin, 0).rotate(build.rotdeg() + rand.range(sparkSpread));

                    Draw.color(spark, col, fin);
                    float px = Tmp.v1.x, py = Tmp.v1.y;
                    if(fin <= 1f) Lines.lineAngle(lx + px + or * ddx, ly + py + or * ddy, Angles.angle(px, py), Mathf.slope(fin) * sparkSize);
                }
                Draw.reset();
            }
        }

        if(glowRegion.found()){
            Draw.z(Layer.blockAdditive);
            Draw.blend(Blending.additive);
            Draw.color(Tmp.c1.set(heatColor).lerp(boostHeatColor, /*boostWarmup*/ 1), build.warmup() * (heatColor.a * (1f - heatPulse + Mathf.absin(heatPulseScl, heatPulse))));
            Draw.rect(glowRegion, build.x, build.y, build.rotdeg());
            Draw.blend();
            Draw.color();
        }

        Draw.blend();
        Draw.reset();

    }
}
