package tantros.ai.types;

import arc.math.geom.Vec2;
import arc.struct.IntSeq;
import mindustry.core.World;
import mindustry.entities.units.AIController;
import mindustry.gen.Building;

import static mindustry.Vars.controlPath;
import static mindustry.Vars.tilesize;

public abstract class BaseGroundAI extends AIController {

    Vec2 targetPos = new Vec2();
    protected static final Vec2 vecOut = new Vec2(), vecMovePos = new Vec2();
    protected static final boolean[] noFound = {false};

    public IntSeq unreachableBuildings = new IntSeq(8);

    public void pathTo(int tileX, int tileY){
        float engageRange = unit.type.range * 0.65f;
        boolean withinCloseRange = target != null && unit.within(target, engageRange);
        targetPos.set(tileX * tilesize, tileY * tilesize);
        vecOut.set(targetPos);
        vecMovePos.set(targetPos);

        boolean move = true;
        if(withinCloseRange){
            move = true;
            noFound[0] = false;
            vecOut.set(vecMovePos);
        }else{
            move &= controlPath.getPathPosition(unit, vecMovePos, targetPos , vecOut, noFound);
        }

        //if the path is invalid, stop trying and record the end as unreachable
        if(unit.team.isAI() && (noFound[0] || unit.isPathImpassable(World.toTile(vecMovePos.x), World.toTile(vecMovePos.y)))){
            if(target instanceof Building build){
                unreachableBuildings.addUnique(build.pos());
            }
            return;
        }

        if(move){
            moveTo(vecOut,
                    withinCloseRange ? engageRange :
                            unit.isGrounded() ? 0f :
                                    target != null ? engageRange : 0f,
                    unit.isFlying() ? 40f : 100f, false, null, true);
            targetPos.set(vecOut);
        }
    }
}
