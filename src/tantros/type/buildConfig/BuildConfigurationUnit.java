package tantros.type.buildConfig;

import arc.struct.IntMap;
import arc.struct.ObjectMap;
import arc.struct.StringMap;
import arc.util.Log;
import arc.util.Nullable;
import arc.util.io.Reads;
import arc.util.io.Writes;
import arc.util.pooling.Pool;
import arc.util.pooling.Pools;
import mindustry.world.blocks.logic.LogicBlock;
import tantros.util.io.ReadContext;
import tantros.util.io.WriteContext;

import java.io.*;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

public abstract class BuildConfigurationUnit implements Pool.Poolable {

    public static final ObjectMap<String, Class<? extends BuildConfigurationUnit>> registeredUnits = new ObjectMap<>();

    static {
        register(AddUnitConfig.class);
        register(SetItemConfig.class);
        register(ClearQueueConfig.class);
        register(ClearUnitsConfig.class);
    }

    public BuildConfigurationUnit(){
        register();
    }

    public void register(){
        registeredUnits.put(this.getClass().getName(), this.getClass());
    }

    public static void register(Class<? extends BuildConfigurationUnit> type){
        registeredUnits.put(type.getName(), type);
    }

    public void read(Reads read){
    }

    public void write(Writes write){
    }

    public static <E extends BuildConfigurationUnit> E get(Class<E> type){
        return Pools.obtain(type, ()->BuildConfigurationUnit.newFrom(type));
    }

    public static <E extends BuildConfigurationUnit> E newFrom(Class<E> type){
        try{
            return type.getDeclaredConstructor().newInstance();
        }catch(Exception e){
            return null;
        }
    }

    public byte[] toByteArray(){
        try{
            var baos = new ByteArrayOutputStream();
            var writes = new Writes(new DataOutputStream(new DeflaterOutputStream(baos)));


            writes.str(this.getClass().getName());
            this.write(writes);

            writes.close();

            if(baos.size() > 40000){
                Log.err("Failed to send building config unit: size greater than 40000");
            }

            return baos.toByteArray();
        }catch(Exception e){
            Log.err("Failed to send building config unit.", e);
        }
        return new byte[]{};
    }

    @Nullable
    public static BuildConfigurationUnit fromByteArray(byte[] in){
        try(Reads reads = new Reads(new DataInputStream(new InflaterInputStream(new ByteArrayInputStream(in))))) {

            String name = reads.str();

            BuildConfigurationUnit unit = get(registeredUnits.get(name));
            unit.read(reads);
            return unit;

        }catch(Exception ignored){
            Log.err("Failed to receive building config unit.", ignored);
        }
        return null;

    }

}
