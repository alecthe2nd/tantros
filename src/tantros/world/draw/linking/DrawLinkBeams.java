package tantros.world.draw.linking;

import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.math.geom.Geometry;
import mindustry.core.Renderer;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.graphics.Drawf;
import mindustry.graphics.Layer;
import mindustry.world.Block;
import mindustry.world.blocks.power.BeamNode;
import tantros.type.blockConfig.BeamLinkConfig;
import tantros.type.buildingState.logic.signalling.BeamLinkState;
import tantros.type.buildingState.logic.signalling.SignalInputState;
import tantros.world.blocks.BlockExtended;
import tantros.world.draw.extended.DrawBlockExtended;

import static mindustry.Vars.tilesize;

public class DrawLinkBeams extends DrawBlockExtended {

    public Color laserColor1 = Color.white;
    public Color laserColor2 = Color.valueOf("ffd9c2");
    public float pulseScl = 7, pulseMag = 0.05f;
    public float laserWidth = 0.4f;

    BeamLinkConfig beamConfig;

    TextureRegion laser, laserEnd;

    public DrawLinkBeams(BeamLinkConfig beamConfig){
        this.beamConfig = beamConfig;
    }

    @Override
    public void load(Block block) {
        laser = Core.atlas.find(block.name + "-beam", "tantros-default-beam");
        laserEnd = Core.atlas.find(block.name + "-beam-end", "tantros-default-beam-end");
    }

    @Override
    public void draw(BlockExtended.BuildExtended build) {

        BeamLinkState beamState = build.getState(BeamLinkState.class, beamConfig.beamStateName);

        if(beamState == null) return;

        if(Mathf.zero(Renderer.laserOpacity) || build.team == Team.derelict) return;

        Draw.z(Layer.power);
        //Draw.color(laserColor1, laserColor2, (1f - build.power.graph.getSatisfaction()) * 0.86f + Mathf.absin(3f, 0.1f));
        Draw.color(laserColor1, laserColor2, Mathf.absin(3f, 0.1f));
        Draw.alpha(Renderer.laserOpacity);
        float w = laserWidth + Mathf.absin(pulseScl, pulseMag);

        for(int i = 0; i < 4; i ++){
            if(canDrawBeam(build, beamState, i)){

                int dst = Math.max(Math.abs(beamState.dests[i].x - build.tile.x),  Math.abs(beamState.dests[i].y - build.tile.y));
                //don't draw lasers for adjacent blocks
                if(dst > 1 + build.block.size/2){
                    var point = Geometry.d4[i];
                    float poff = tilesize/2f;
                    Drawf.laser(laser, laserEnd, build.x + poff*build.block.size*point.x, build.y + poff*build.block.size*point.y, beamState.dests[i].worldx() - poff*point.x, beamState.dests[i].worldy() - poff*point.y, w);
                }
            }
        }

        Draw.reset();
    }

    public boolean canDrawBeam(BlockExtended.BuildExtended build, BeamLinkState beamState, int dir){
        if(beamState.beamOn(build, dir) && beamState.dests[dir] != null && beamState.links[dir] != null && beamState.links[dir].wasVisible){
            Building other_raw = beamState.links[dir];

            if(!(other_raw instanceof BlockExtended.BuildExtended other)) return false;
            SignalInputState otherSignalInput = other.getState(SignalInputState.class);
            if(otherSignalInput == null) return false;
            BeamLinkState otherBeamState = other.getState(BeamLinkState.class);
            if(otherBeamState == null) return true;

            if(otherBeamState.beamOn(other, (dir + 2)%4)){
                if(beamState.config.range > otherBeamState.config.range){
                    return true;
                } else if (beamState.config.range == otherBeamState.config.range){
                    return build.id > other.id;
                }
            } else {
                return true;
            }
        }
        return false;
    }
}
