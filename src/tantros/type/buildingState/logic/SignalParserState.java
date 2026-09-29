package tantros.type.buildingState.logic;

import arc.util.io.Reads;
import arc.util.io.Writes;
import arc.util.pooling.Pools;
import mindustry.content.Items;
import mindustry.io.TypeIO;
import tantros.type.buildConfig.SetParseType;
import tantros.type.buildingState.BuildingState;
import tantros.world.blocks.BlockExtended;

public class SignalParserState implements BuildingState {

    private Object parseType = Items.copper;
    public boolean dirty = false;

    public Object prevValue = null;


    public Object getParseType() {
        if(parseType instanceof TypeIO.Boxed<?> boxed){
            parseType = boxed.unbox();
        }
        return parseType;
    }

    public void setParseType(Object parseType) {
        this.parseType = parseType;
    }

    @Override
    public <E> void onConfig(BlockExtended.BuildExtended owner, E config) {
        if(config instanceof SetParseType parse){
            setParseType(parse.getParseType());
            dirty = true;
            Pools.free(parse);
        }
    }

    @Override
    public boolean isTransient() {
        return false;
    }

    @Override
    public String getName() {
        return "SignalParserState";
    }

    @Override
    public int getVersion() {
        return 0;
    }

    @Override
    public void write(Writes write) {
        TypeIO.writeObject(write, this.getParseType());
    }

    @Override
    public void read(Reads read) {
         this.setParseType(TypeIO.readObjectBoxed(read, true));
    }

    @Override
    public void reset() {
        setParseType(Items.copper);
    }
}
