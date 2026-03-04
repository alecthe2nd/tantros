package tantros.entities.abilities.burrow;

import arc.math.geom.Rect;
import mindustry.entities.abilities.Ability;
import mindustry.game.Team;
import mindustry.gen.Unit;
import tantros.TantrosVars;
import tantros.graphics.overlays.SonarTracking;

public class SonarAbility extends Ability implements SonarTracking.SonarSource {

    public float range = -1;

    protected Rect hitbox;

    protected Team team;

    boolean dirty = true;

    @Override
    public void created(Unit unit) {
        super.created(unit);
        this.team = unit.team;
        if (range <= 0) range = unit.range();
        unit.hitbox(hitbox);
        hitbox.setSize(2 * range);
        TantrosVars.sonarTracking.add(this);
    }

    @Override
    public void update(Unit unit) {
        super.update(unit);
        this.team = unit.team;
        unit.hitbox(hitbox);
        hitbox.setSize(2 * range);
        dirty = unit.moving();
    }

    @Override
    public void death(Unit unit) {
        super.death(unit);
        TantrosVars.sonarTracking.add(this);
    }

    @Override
    public float range() {
        return range;
    }

    @Override
    public Team team() {
        return team;
    }

    @Override
    public boolean dirty() {
        return dirty;
    }

    @Override
    public void dirty(boolean dirty) {
        this.dirty = dirty;
    }

    @Override
    public void hitbox(Rect out) {
        if(hitbox == null) return;
        out.set(hitbox);
    }
}
