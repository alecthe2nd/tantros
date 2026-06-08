package tantros.type.effect.logic;

import arc.func.Prov;
import arc.math.geom.Rect;
import arc.struct.Seq;
import arc.util.pooling.Pools;
import mindustry.gen.Building;
import mindustry.gen.Unit;
import tantros.type.blockConfig.ConfigApplier;
import tantros.type.effect.projector.range.RangeConfig;
import tantros.type.blockInput.LinkInput;
import tantros.type.buildConfig.AddUnitConfig;
import tantros.type.buildingState.BuildingState;
import tantros.type.buildingState.logic.Link;
import tantros.type.buildingState.logic.Links;
import tantros.type.buildingState.logic.OneLink;
import tantros.type.effect.BlockEffect;
import tantros.world.blocks.BlockExtended;

import static mindustry.Vars.*;

public class UnitLinkSetter<E extends BuildingState & Links> implements BlockEffect {
    protected final static Rect rect = new Rect();
    protected final static Seq<Unit> units = new Seq<>();

    public int updateTimer;

    public float checkInterval = 20f;

    public Class<E> linksType;
    public Prov<E> newInstance;
    public RangeConfig range;

    public UnitLinkSetter(Class<E> linksType, Prov<E> newInstance, RangeConfig range){
        this.linksType = linksType;
        this.newInstance = newInstance;
        this.range = range;
    }

    @Override
    public void update(BlockExtended.BuildExtended build) {
        Links links = build.getState(linksType);
        if(build.timer(updateTimer, checkInterval)){
            units.clear();
            build.hitbox(rect);
            build.team.data().tree().intersect(rect, units);

            for (Link link : links.getLinks()) {

                Building other = world.build(link.x, link.y);
                //Log.info(other.block.configurations.containsKey(Unit.class));
                if(other != null && other.block.configurations.containsKey(AddUnitConfig.class)){
                    if(units.any()){
                        //Log.info(units);
                        AddUnitConfig config = Pools.obtain(AddUnitConfig.class, AddUnitConfig::new);
                        config.unit = units.first();
                        other.configureAny(config);
                    }
                }
            }
        }
    }

    @Override
    public void apply(BlockExtended block) {
        updateTimer = block.timers++;

        block.stateSources.add(()-> newInstance.get());
        block.putBlockConfig(range);
        block.putBlockConfig(new ConfigApplier<>(linksType, Integer.class));
        block.inputs.add(new LinkInput<>(OneLink.class));
    }
}
