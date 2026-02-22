package tantros.world.blocks;

import arc.func.Prov;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.scene.ui.layout.Table;
import arc.struct.ObjectMap;
import arc.struct.OrderedSet;
import arc.struct.Seq;
import arc.util.Eachable;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.Vars;
import mindustry.content.Liquids;
import mindustry.entities.Damage;
import mindustry.entities.Effect;
import mindustry.entities.units.BuildPlan;
import mindustry.gen.Building;
import mindustry.gen.Call;
import mindustry.gen.Player;
import mindustry.gen.Unit;
import mindustry.type.Item;
import mindustry.type.Liquid;
import mindustry.type.LiquidStack;
import mindustry.type.UnitType;
import mindustry.world.Block;
import mindustry.world.consumers.Consume;
import mindustry.world.draw.DrawDefault;
import tantros.net.TantrosCalls;
import tantros.type.blockConfig.BlockConfig;
import tantros.type.blockConfig.ConfigApplier;
import tantros.type.blockInput.BlockInput;
import tantros.type.blockInput.util.InputListener;
import tantros.type.blockUtil.OnDestroyExplosionContext;
import tantros.type.buildConfig.BuildConfigurationUnit;
import tantros.type.buildingState.BuildingState;
import tantros.type.effect.BlockEffect;
import tantros.world.consumers.ExtendedConsume;
import tantros.world.draw.extended.DrawBlockExtended;
import tantros.world.draw.extended.DrawMultiExtended;

import static mindustry.Vars.*;
import static mindustry.Vars.state;

public class BlockExtended extends Block {

    /*
    * List of allowed dynamic state classes and their respective read/write priorities. Do not modify!
    * This allows for dynamically sized states to have a definite order during read/write. Tampering with this order
    * WILL lead to corrupted save data.
    * BuildingStates not given a priority here will be skipped during saving.
     */
    public static OrderedSet<Class<? extends BuildingState>> order = new OrderedSet<>();

    public static final LiquidStack tempLiquid = new LiquidStack(Liquids.water, 0f);
    public static final OnDestroyExplosionContext tempContext = new OnDestroyExplosionContext();

    public DrawBlockExtended drawer = new DrawMultiExtended(new DrawDefault());
    public ObjectMap<Class<? extends BlockConfig>, ? super BlockConfig> blockConfigs = new ObjectMap<>();
    public Seq<ConfigApplier<?,?>> configAppliers = new Seq<>();
    public Seq<Prov<BuildingState>> stateSources = new Seq<>();

    public Seq<BlockEffect> effects =  new Seq<>();

    public Seq<BlockInput> inputs = new Seq<>();

    public BlockExtended(String name) {
        super(name);
    }

    @Override
    public void init() {
        super.init();

        for(BlockEffect effect: effects){
            effect.apply(this);
        }

    }

    @Override
    public void drawPlanRegion(BuildPlan plan, Eachable<BuildPlan> list){
        drawer.drawPlan(this, plan, list);
    }

    public void drawPlace(int x, int y, int rotation, boolean valid){
        drawer.drawPlace(x,y,rotation,valid, this);
    }
    @Override
    public TextureRegion[] icons(){
        return drawer.finalIcons(this);
    }

    @Override
    public void postInit() {

        for(BlockInput input: inputs){
            input.apply(this);
        }

        for(Class<? extends BlockConfig> configType : blockConfigs.keys()){
            BlockConfig config = getBlockConfig(configType);
            if(config != null){
                config.apply(this);
            }
        }
        for(ConfigApplier<?,?> applier: configAppliers){
            applier.apply(this);
        }
        super.postInit();
    }

    @Override
    public void load(){
        super.load();

        drawer.load(this);
    }

    @Override
    public void getRegionsToOutline(Seq<TextureRegion> out){
        drawer.getRegionsToOutline(this, out);
    }

    @SuppressWarnings("unchecked")
    public <T extends BlockConfig> T getBlockConfig(Class<T> type){
        return (T) this.blockConfigs.get(type);
    }

    public <T extends BlockConfig> void putBlockConfig(T state){
        this.blockConfigs.put(state.getClass(), state);
    }

    public class BuildExtended extends Building{
        public ObjectMap<Class<? extends BuildingState>, ? super BuildingState> states = new ObjectMap<>();

        public Seq<InputListener<?>> listeners = new Seq<>();

        @Override
        public void draw(){
            drawer.draw(this);
        }

        @Override
        public void drawLight(){
            super.drawLight();
            drawer.drawLight(this);
        }

        @Override
        public void drawConfigure(){
            super.drawConfigure();

            drawer.drawConfigure(this);
        }

        @SuppressWarnings("unchecked")
        public <state extends BuildingState> state getState(Class<state> type){
            return (state) this.states.get(type);
        }

        public <E> E getStateImplementing(Class<E> type){
            for(Class<? extends BuildingState> stateType : states.keys()){
                if(type.isAssignableFrom(stateType)){
                    return type.cast(this.getState(stateType));
                }
            }
            return null;
        }

        public <T extends BuildingState> void putState(T state){
            this.states.put(state.getClass(), state);
        }

        @Override
        public void created() {
            super.created();
            for(Prov<BuildingState> stateSource : stateSources){
                BuildingState state = stateSource.get();
                putState(state);
            }
            for(Class<? extends BuildingState> type : this.states.keys()){
                getState(type).initState((BlockExtended) this.block, this);
            }

            for(BlockInput input: inputs){
                input.post(this);
            }
        }

        @Override
        public void remove() {

            for(BlockInput input: inputs){
                input.remove(this);
            }

            for(InputListener<?> listener: listeners){
                listener.detach();
            }
            super.remove();
        }

        @Override
        public void updateTile() {
            super.updateTile();
            if(efficiency > 0){
                for(BlockEffect effect: effects){
                    effect.update(this);
                }
            }
        }

        @Override
        public void configure(Object value) {
            if(value instanceof BuildConfigurationUnit buildConfig){
                this.block.lastConfig = buildConfig;
                TantrosCalls.tileConfig(Vars.player, this, buildConfig);
                return;
            }
            super.configure(value);
        }

        @Override
        public void configureAny(Object value) {
            if(value instanceof BuildConfigurationUnit buildConfig){
                TantrosCalls.tileConfig((Player)null, this, buildConfig);
            }
            super.configureAny(value);
        }

        @Override
        @SuppressWarnings("unchecked")
        public void configured(Unit builder, Object value) {
            Class<?> type = value == null ? Void.TYPE : (value.getClass().isAnonymousClass() ? value.getClass().getSuperclass() : value.getClass());
            if (value instanceof Item) {
                type = Item.class;
            }

            if (value instanceof Block) {
                type = Block.class;
            }

            if (value instanceof Liquid) {
                type = Liquid.class;
            }

            if (value instanceof UnitType) {
                type = UnitType.class;
            }

            if (value instanceof Unit) {
                type = Unit.class;
            }

            if (builder != null && builder.isPlayer()) {
                this.updateLastAccess(builder.getPlayer());
            }

            if (this.block.configurations.containsKey(type)) {
                this.block.configurations.get(type).get(this, value);
            } else if (value instanceof Building) {
                Building build = (Building)value;
                Object conf = build.config();
                if (conf != null && !(conf instanceof Building)) {
                    this.configured(builder, conf);
                }
            }
        }

        @Override
        public void handleLiquid(Building source, Liquid liquid, float amount) {
            if(amount > liquidCapacity - liquids.get(liquid)){
                float overflow = amount - (liquidCapacity - liquids.get(liquid));
                amount = amount - overflow;
                tempLiquid.set(liquid, overflow);
                for(Class<? extends BuildingState> type: this.states.keys()){
                    BuildingState state = getState(type);
                    state.onOverflow(tempLiquid);
                }
            }
            super.handleLiquid(source, liquid, amount);
        }

        /** Called when the block is destroyed. The tile is still intact at this stage. */
        @Override
        public void onDestroyed(){
            float explosiveness = block.baseExplosiveness;
            float flammability = 0f;
            float power = 0f;

            if(block.hasItems){
                for(Item item : content.items()){
                    int amount = Math.min(items.get(item), explosionItemCap());
                    explosiveness += item.explosiveness * amount;
                    flammability += item.flammability * amount;
                    power += item.charge * Mathf.pow(amount, 1.1f) * 150f;
                }
            }

            if(block.hasLiquids){
                flammability += liquids.sum((liquid, amount) -> liquid.flammability * amount / 2f);
                explosiveness += liquids.sum((liquid, amount) -> liquid.explosiveness * amount / 2f);
            }

            if(block.consPower != null && block.consPower.buffered){
                power += this.power.status * block.consPower.capacity;
            }

            if(block.hasLiquids && state.rules.damageExplosions){
                liquids.each(this::splashLiquid);
            }
            tempContext.clear();
            tempContext.explosiveness = explosiveness * 3.5f * block.explosivenessScale;
            tempContext.flammability = flammability * block.flammabilityScale;
            tempContext.radius = tilesize * block.size / 2f;

            for(BlockEffect effect : effects){
                effect.onDestroyedExplosion(this, tempContext);
            }

            //cap explosiveness so fluid tanks/vaults don't instakill units
            Damage.dynamicExplosion(x, y,tempContext.flammability, tempContext.explosiveness, power, tempContext.radius, state.rules.damageExplosions, block.destroyEffect, block.baseShake);

            if(block.createRubble && !floor().solid && !floor().isLiquid){
                Effect.rubble(x, y, block.size);
            }
        }

        @Override
        public void write(Writes write){
            super.write(write);
            int count = 0;
            for(Class<? extends BuildingState> stateType : order){
                BuildingState state = getState(stateType);
                if( state != null && !state.isTransient()){
                    count++;
                }
            }
            write.i(count);
            for(Class<? extends BuildingState> stateType : order){
                BuildingState state = getState(stateType);
                if( state != null && !state.isTransient()){
                    state.write(write);
                }
            }
        }

        @Override
        public void read(Reads read, byte revision) {
            super.read(read, revision);
            int expectedCount = read.i();
            int count = 0;
            for(Class<? extends BuildingState> stateType : order){
                BuildingState state = getState(stateType);
                if( state != null && !state.isTransient()){
                    count++;
                }
            }
            if(count == expectedCount) {
                for (Class<? extends BuildingState> stateType : order) {
                    BuildingState state = getState(stateType);
                    if (state != null && !state.isTransient()) {
                        state.read(read);
                    }
                }
            }
        }

        public BlockExtended getBlock(){
            return (BlockExtended) block;
        }

        @Override
        public boolean onConfigureBuildTapped(Building other) {
            boolean unconsumed = true;
            for(BlockInput input: inputs){
                unconsumed &= input.onConfigureBuildTapped(this, other);
            }
            return unconsumed && super.onConfigureBuildTapped(other);
        }

        @Override
        public void buildConfiguration(Table table) {
            for(BlockInput input: inputs){
                input.buildConfiguration(this, table);
            }
        }

        @Override
        public void displayBars(Table table) {
            super.displayBars(table);
            for(Class<? extends BuildingState> type : this.states.keys()){
                getState(type).displayBars(this, table);
            }
        }

        @Override
        public float efficiencyScale() {
            float scale = 1f;
            for(Consume consume: consumers){
                scale *= consume.efficiencyMultiplier(this);
            }
            return scale;
        }
    }

}
