package tantros.world.blocks;

import arc.func.Prov;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.scene.ui.layout.Table;
import arc.struct.ObjectMap;
import arc.struct.Seq;
import arc.util.Eachable;
import arc.util.Log;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.Vars;
import mindustry.content.Liquids;
import mindustry.entities.Damage;
import mindustry.entities.Effect;
import mindustry.entities.units.BuildPlan;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.gen.Unit;
import mindustry.type.Item;
import mindustry.type.Liquid;
import mindustry.type.LiquidStack;
import mindustry.type.UnitType;
import mindustry.world.Block;
import mindustry.world.Tile;
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
import tantros.type.production.Produce;
import tantros.world.blocks.production.ProductionBlock;
import tantros.world.consumers.ExtendedConsume;
import tantros.world.draw.extended.DrawBlockExtended;
import tantros.world.draw.extended.DrawMultiExtended;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import static mindustry.Vars.*;
import static mindustry.Vars.state;

public class BlockExtended extends Block {

    public static final LiquidStack tempLiquid = new LiquidStack(Liquids.water, 0f);
    public static final OnDestroyExplosionContext tempContext = new OnDestroyExplosionContext();

    public static final ByteArrayOutputStream writeOutputStream = new ByteArrayOutputStream();

    public static final Writes writeDelegate = new Writes(new DataOutputStream(writeOutputStream));

    public DrawBlockExtended drawer = new DrawMultiExtended(new DrawDefault());
    public ObjectMap<Class<? extends BlockConfig>, ? super BlockConfig> blockConfigs = new ObjectMap<>();
    public Seq<ConfigApplier<?,?>> configAppliers = new Seq<>();
    public Seq<BuildingStateSource> stateSources = new Seq<>();
    public ObjectMap<String, BuildingStateSource> namedSources = new ObjectMap<>();

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

    @Override
    public boolean canPlaceOn(Tile tile, Team team, int rotation){
        boolean allowed = super.canPlaceOn(tile, team, rotation);;
        for(BlockEffect effect : effects){
            allowed &= effect.placementAllowed(this, tile, team, rotation);
        }
        return allowed;
    }

    @SuppressWarnings("unchecked")
    public <T extends BlockConfig> T getBlockConfig(Class<T> type){
        return (T) this.blockConfigs.get(type);
    }

    public <T extends BlockConfig> void putBlockConfig(T state){
        this.blockConfigs.put(state.getClass(), state);
    }

    public class BuildExtended extends Building{
        public ObjectMap<String, BuildingState> states = new ObjectMap<>();
        private final ObjectMap.Values<BuildingState> stateValuesForSearching = new ObjectMap.Values<>(states);

        public Seq<InputListener<?>> listeners = new Seq<>();

        public boolean initializedStates = false;


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

        public <state extends BuildingState> state getState(Class<state> type, String name){
            BuildingState found = this.states.get(name);
            if(type.isInstance(found)){
                return type.cast(found);
            }
            return null;
        }

        public BuildingState getState(String name){
            return this.states.get(name);
        }

        public <state extends BuildingState> state getState(Class<state> type){
            stateValuesForSearching.reset();
            for(BuildingState state : stateValuesForSearching){
                if(type.isInstance(state)){
                    return type.cast(state);
                }
            }
            return null;
        }

        public int computeNumberOfIdenticalStateNames(String name){
            int i = 0;
            for(BuildingState state: states.values()){
                if(state.getName().equals(name)){
                    i++;
                }
            }
            return i;
        }

        /**
        * Adds a state to this building. Returns the name of the building.
        */
        public String putState(BuildingState state){
            String name = state.getName();
            int suffixNum = computeNumberOfIdenticalStateNames(name);
            name += "-" + suffixNum;
            this.putState(state,name, true);
            return name;
        }

        /**
         * Adds a state to this building with the given name. If the name already exists, it gets overridden.
         */
        public void putState(BuildingState state, String name){
            this.putState(state, name, false);
        }

        /**
         * Adds a state to this building with the given name. If the name already exists and generateFallbackName is true, a new name will be generated for it. If the name already exists but generateFallbackName is false, then the old name will be overridden with this state.
         */
        public void putState(BuildingState state, String name, boolean generateFallbackName){
            if(generateFallbackName && this.states.containsKey(name)){
                this.putState(state);
            } else {
                this.states.put(name, state);
            }
        }

        @Override
        public void created() {
            super.created();

            for(ObjectMap.Entry<String, BuildingStateSource> entry: namedSources){
                BuildingState state = entry.value.get();
                putState(state, entry.key, true);
            }

            for(BuildingStateSource stateSource : stateSources){
                BuildingState state = stateSource.get();
                putState(state);
            }

            for(BuildingState state : this.states.values()){
                state.initState((BlockExtended) this.block, this);
            }
            initializedStates = true;

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

            for(BuildingState state : this.states.values()){
                state.update((ProductionBlock) this.block, this);
            }
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
                TantrosCalls.tileConfig(null, this, buildConfig);
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
                for(BuildingState state: this.states.values()){
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
            //step one, count the non-transient states
            int count = 0;
            for(ObjectMap.Entry<String, BuildingState> entry : states.entries()){
                if(!entry.value.isTransient()){
                    count++;
                }
            }

            //step two: perform write operations
            if(count > 255){
                Log.err("There are somehow more than 255 non-transient states on block " + this.block.name +". This is ridiculous.\nTo prevent save corruption, all states past the 255th will be discarded.");
                count = 255;
            }
            write.b(count);

            //guard against ever writing more states than count. This may happen if some building ends up with more than 255 states.
            int guardCount = 0;
            for(ObjectMap.Entry<String, BuildingState> entry : states.entries()){
                if(!entry.value.isTransient() && guardCount < count){
                    write.str(entry.key);
                    write.i(entry.value.getVersion());

                    //prepare buffer to count the size of the data to be written
                    writeOutputStream.reset();

                    //send state data to buffer
                    entry.value.write(writeDelegate);

                    //write the number of bytes in the buffer, then write the contents of the buffer
                    write.i(writeOutputStream.size());
                    write.b(writeOutputStream.toByteArray());

                    guardCount++;
                }
            }
        }

        @Override
        public void read(Reads read, byte revision) {
            super.read(read, revision);
            int numStates = read.b();
            for(int i = 0; i < numStates; i++){
                //read name of the state
                String name = read.str();
                //read version of the state
                int version = read.i();
                //read number of incoming bytes, for later.
                int size = read.i();

                BuildingState state = getState(name);

                if(state != null && state.getVersion() == version){
                    //state is valid, should be safe to read it
                    state.read(read);
                } else {
                    //state is missing or wrong version, all bytes that
                    //   make up its data should be read and dumped
                    read.b(size);
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
            for(BuildingState state : this.states.values()){
                state.displayBars(this, table);
            }

            for(Consume consume: consumers){
                if(consume instanceof ExtendedConsume extend){
                    extend.displayBars(this, table);
                }
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

    public interface BuildingStateSource extends Prov<BuildingState>{

    }
}
