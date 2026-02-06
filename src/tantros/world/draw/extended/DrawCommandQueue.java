package tantros.world.draw.extended;

import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.math.geom.Position;
import arc.math.geom.Vec2;
import arc.util.Tmp;
import mindustry.Vars;
import mindustry.ai.UnitCommand;
import mindustry.ai.types.CommandAI;
import mindustry.ai.types.LogicAI;
import mindustry.game.Team;
import mindustry.gen.Icon;
import mindustry.gen.Payloadc;
import mindustry.gen.Unit;
import mindustry.graphics.Drawf;
import mindustry.graphics.Layer;
import mindustry.graphics.Pal;
import tantros.type.buildingState.logic.UnitCommandQueueState;
import tantros.type.buildingState.logic.UnitLinks;
import tantros.world.blocks.BlockExtended;

import static mindustry.Vars.world;

public class DrawCommandQueue extends DrawBlockExtended{

    @Override
    public void drawConfigure(BlockExtended.BuildExtended build) {
        Draw.draw(Layer.plans, () -> {
            drawCommanded(build, true);
        });

        Draw.draw(Layer.groundUnit - 1, () -> {
            drawCommanded(build, false);
        });

        Draw.draw(Layer.overlayUI, () -> {
            drawCommandedTargets(build);
            drawQueue(build);
        });
    }

    public void drawQueue(BlockExtended.BuildExtended build){
        Color color = Team.malis.color;
        float lineLimit = 6.5f;
        float alpha = 0.5f;
        UnitCommandQueueState queueState = build.getState(UnitCommandQueueState.class);
        Position lastPos = null;
        for(Position next : queueState.commandQueue){
            if(lastPos != null) {
                Drawf.limitLine(lastPos, next, lineLimit, lineLimit, color.write(Tmp.c1).a(alpha));
            }
            lastPos = next;

            if(next instanceof Vec2 vec){
                Drawf.square(vec.x, vec.y, 3.5f, color.write(Tmp.c1).a(alpha));
            }else{
                Drawf.target(next.getX(), next.getY(), 6f, Pal.remove);
            }
        }
    }

    public void drawCommandedTargets(BlockExtended.BuildExtended build){
        UnitLinks links = build.getState(UnitLinks.class);
        for(Unit unit : links.unitLinks){
            if(unit.controller() instanceof CommandAI ai){
                var cmd =  ai.currentCommand();

                if(ai.attackTarget != null && cmd.drawTarget){
                    Drawf.target(ai.attackTarget.getX(), ai.attackTarget.getY(), 6f, Pal.remove);
                }
            }
        }
    }

    public void drawCommanded(BlockExtended.BuildExtended owner, boolean flying){
        UnitLinks links = owner.getState(UnitLinks.class);
        float lineLimit = 6.5f;
        int sides = 6;
        float alpha = 0.5f;

        for(Unit unit : links.unitLinks){

            Color color = unit.controller() instanceof LogicAI ? Team.malis.color : Pal.accent;

            Position lastPos = null;

            if(unit.controller() instanceof CommandAI ai){
                var cmd =  ai.currentCommand();
                lastPos = ai.attackTarget != null ? ai.attackTarget : ai.targetPos;

                if((unit.isFlying() || unit.type.allowLegStep) != flying) continue;

                //draw target line
                if(ai.targetPos != null && cmd.drawTarget){
                    Position lineDest = ai.attackTarget != null ? ai.attackTarget : ai.targetPos;
                    Drawf.limitLine(unit, lineDest, unit.hitSize / 2f, lineLimit, color.write(Tmp.c1).a(alpha));

                    if(ai.attackTarget == null){
                        Drawf.square(lineDest.getX(), lineDest.getY(), 3.5f, color.write(Tmp.c1).a(alpha));

                        if(cmd == UnitCommand.enterPayloadCommand){
                            var build = world.buildWorld(lineDest.getX(), lineDest.getY());
                            if(build != null && build.block.acceptsUnitPayloads && build.team == unit.team){
                                Drawf.selected(build, color);
                            }else{
                                Drawf.cross(lineDest.getX(), lineDest.getY(), 7f, Pal.remove);
                            }
                        }
                    }
                }
            }

            float rad = unit.hitSize / 2f;

            Fill.lightInner(unit.x, unit.y, sides,
                    Math.max(0f, rad * 0.8f),
                    rad,
                    0f,
                    Tmp.c3.set(color).a(0f),
                    Tmp.c2.set(color).a(0.7f)
            );

            Lines.stroke(1f);
            Draw.color(color);
            Lines.poly(unit.x, unit.y, sides, rad + 0.5f);
            //uncomment for a dark border
            //Draw.color(Pal.gray);
            //Lines.poly(unit.x, unit.y, sides, rad + 1.5f);
            Draw.reset();

            if(lastPos == null){
                lastPos = unit;
            }

            if(unit.controller() instanceof CommandAI ai){
                //draw command queue
                if(ai.currentCommand().drawTarget && ai.commandQueue.size > 0){
                    for(var next : ai.commandQueue){
                        Drawf.limitLine(lastPos, next, lineLimit, lineLimit, color.write(Tmp.c1).a(alpha));
                        lastPos = next;

                        if(next instanceof Vec2 vec){
                            Drawf.square(vec.x, vec.y, 3.5f, color.write(Tmp.c1).a(alpha));
                        }else{
                            Drawf.target(next.getX(), next.getY(), 6f, Pal.remove);
                        }
                    }
                }

                if(ai.targetPos != null && ai.currentCommand() == UnitCommand.loopPayloadCommand && unit instanceof Payloadc pay){
                    Draw.color(color, 0.4f + Mathf.absin(5f, 0.5f));
                    TextureRegion region = pay.hasPayload() ? Icon.download.getRegion() : Icon.upload.getRegion();
                    float offset = 11f;
                    float size = 8f;
                    Draw.rect(region, ai.targetPos.x, ai.targetPos.y + offset, size, size / region.ratio());

                    if(ai.commandQueue.size > 0){
                        region = !pay.hasPayload() ? Icon.download.getRegion() : Icon.upload.getRegion();
                        Draw.rect(region, ai.commandQueue.first().getX(), ai.commandQueue.first().getY() + offset, size, size / region.ratio());
                    }
                    Draw.color();
                }
            }
        }

        Draw.reset();

    }
}
