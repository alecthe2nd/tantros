package tantros.type.buildConfig;

import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.io.TypeIO;

public class SetParseType extends BuildConfigurationUnit{

    private Object parseType;

    public Object getParseType(){
        if(parseType instanceof TypeIO.Boxed<?> boxed){
            parseType = boxed.unbox();
        }
        return parseType;
    }

    public void setParseType(Object object){
        this.parseType = object;
    }

    @Override
    public void reset() {
        parseType = null;
    }

    @Override
    public void write(Writes write) {
        TypeIO.writeObject(write, this.getParseType());
    }

    @Override
    public void read(Reads read) {
        this.setParseType(TypeIO.readObjectBoxed(read, true));
    }
}
