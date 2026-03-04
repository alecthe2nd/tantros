package tantros.world.blocks.effect;

import arc.func.Floatp;
import arc.func.Prov;
import arc.math.Mathf;
import arc.math.geom.Rect;
import arc.math.geom.Vec2;
import mindustry.Vars;
import mindustry.game.Team;
import tantros.graphics.overlays.SonarTracking;

public class SonarWrapper implements SonarTracking.SonarSource{
    public float lastRadius = 0;
    public Vec2 lastPos = new Vec2();
    public boolean isDirty = true;

    public Prov<Vec2> pos;
    public Floatp range;
    public Team team;
    public float maxRange;
    public SonarWrapper(Prov<Vec2> pos, Floatp range, Team team, float maxRange) {
        this.pos = pos;
        this.range = range;
        this.team = team;
        this.maxRange = maxRange;
    }

    public void checkDirty(){
        float range = this.range.get();
        if(Mathf.equal(range, maxRange, 0.001f) && !Mathf.equal(lastRadius, maxRange, 0.001f)){
            lastRadius = range;
            isDirty = true;
        }
        if(!Mathf.equal(lastRadius, range, Vars.tilesize/2f)){
            lastRadius = range;
            isDirty = true;
        }
        if(!lastPos.within(pos.get(), Vars.tilesize/2f)){
            lastPos.set(pos.get());
            isDirty = true;
        }
    }

    @Override
    public float range() {
        return range.get();
    }

    @Override
    public float maxRange() {
        return maxRange;
    }

    @Override
    public Team team() {
        return team;
    }

    @Override
    public boolean dirty() {
        return isDirty;
    }

    @Override
    public void dirty(boolean dirty) {
        isDirty = dirty;
    }

    @Override
    public void hitbox(Rect out) {
        out.setSize(2*range.get());
        out.setCenter(pos.get());
    }
}