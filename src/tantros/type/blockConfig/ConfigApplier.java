package tantros.type.blockConfig;

import arc.util.Log;
import tantros.type.buildingState.BuildingState;
import tantros.world.blocks.BlockExtended;

public class ConfigApplier<State extends BuildingState, T> implements BlockConfig{

    public Class<State> stateType;
    public Class<T> configType;

    public ConfigApplier(Class<State> stateType, Class<T> configType){
        this.stateType = stateType;
        this.configType = configType;
    }

    @Override
    public void apply(BlockExtended block) {
        block.config(this.configType, this::onConfig);
        Log.info("Loaded config of " + stateType + " to " + this.configType);
    }

    public void onConfig(BlockExtended.BuildExtended build, T value){
        State state = build.getState(stateType);
        if(state != null){
            state.onConfig(build, value);
        } else {
            Log.warn("Failed to pass a config to a state object of class " + stateType + " because it does not exist in building " + build + ". Config value is " + value );
        }
    }
}
