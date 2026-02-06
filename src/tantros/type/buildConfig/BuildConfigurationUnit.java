package tantros.type.buildConfig;

import arc.struct.IntMap;
import arc.util.io.Reads;
import arc.util.io.Writes;
import arc.util.pooling.Pool;
import arc.util.pooling.Pools;
import tantros.util.io.ReadContext;
import tantros.util.io.WriteContext;

public abstract class BuildConfigurationUnit implements Pool.Poolable {

    public static final IntMap<Class<? extends BuildConfigurationUnit>> seenHashes = new IntMap<>();
    private static int nameHash = 0;

    public static ReadContext readContext = new ReadContext();
    public static WriteContext writeContext = new WriteContext();

    public BuildConfigurationUnit(){
        this.getNameHash();
    }

    public int getNameHash(){
        if(nameHash == 0){
            nameHash = generateNameHash(this);
            if(seenHashes.containsKey(nameHash)) {
                throw new RuntimeException("Duplicate Name Hash " + nameHash + "found in class " + seenHashes.get(nameHash) + " when trying to add class " + this.getClass());
            }
            seenHashes.put(nameHash, this.getClass());
        }
        return nameHash;
    }

    private int generateNameHash(BuildConfigurationUnit configUnit){
        String name = configUnit.getClass().getName();
        int h = 0;
        char[] chars = name.toCharArray();
        for (char aChar : chars) {
            h = 31 * h + aChar;
        }
        return h;
    }

    public void read(Reads read){
        readContext.init(read);
        this.read(readContext);
        readContext.flush(this::reset);
    }

    public void read(ReadContext read){

    }

    public void write(Writes write){
        writeContext.init(write);
        this.write(writeContext);
        writeContext.flush();
    }

    public void write(WriteContext write){

    }

    public static BuildConfigurationUnit get(int hash){
        Class<? extends BuildConfigurationUnit> type = BuildConfigurationUnit.seenHashes.get(hash);
        return (type == null) ? null: get(type);
    }

    public static <E extends BuildConfigurationUnit> BuildConfigurationUnit get(Class<E> type){
        return Pools.obtain(type, ()->BuildConfigurationUnit.newFrom(type));
    }

    public static <E extends BuildConfigurationUnit> E newFrom(Class<E> type){
        try{
            return type.getDeclaredConstructor().newInstance();
        }catch(Exception e){
            return null;
        }
    }

}
