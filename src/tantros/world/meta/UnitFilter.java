package tantros.world.meta;

import arc.func.Boolf;
import mindustry.gen.Unit;
import mindustry.world.blocks.defense.turrets.Turret;

public abstract class UnitFilter implements Boolf<Unit> {

    public Boolf<Unit> chain;

    protected abstract boolean filter(Unit unit);

    @Override
    public boolean get(Unit unit) {
        return filter(unit) && (chain == null || chain.get(unit));
    }

    public UnitFilter withChain(Boolf<Unit> chain){
        this.chain = chain;
        return this;
    }
}
