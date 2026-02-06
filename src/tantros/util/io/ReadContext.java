package tantros.util.io;

import arc.func.Cons;
import arc.util.Log;
import arc.util.io.Reads;
import tantros.type.buildingState.BuildingState;
import tantros.world.blocks.BlockExtended;

import java.io.IOException;

public class ReadContext {

    public Reads cache;

    /*
    * The space to read from in bytes. The context will abort any read beyond the remaining space.
    * If any space remains after reading, or not all space is used, an invalid read will be assumed
    * and a callback will be issued to reset the caller's states and prevent state corruption.
    */
    public int space;

    /*
     * The space used so far.
     */
    public int used;

    /*
     * The read context has reached a reason to abort.
     */
    public boolean abort = true;

    public boolean init = false;

    public void init(Reads reader){
        cache = reader;
        space = reader.i();
        used = 0;
        abort = false;
        init = true;
    }

    public void flush(Runnable reset){
        if(!init) return;
        if(abort || space > used){
            //notify the state to reset themselves
            reset.run();
            //skip any remaining bytes to prevent corruption
            if (space > used) forceSkip(space - used);
        };

        init = false;
    }

    public boolean invalid(int size){
        if(!init) return false;
        if(used + size > space) abort = true;
        return abort;
    }

    /** read long */
    public long l(){
        if(invalid(8)) return 0;
        used += 8;
        return cache.l();
    }

    /** read int */
    public int i(){
        if(invalid(4)) return 0;
        used += 4;
        return cache.i();
    }

    /** read short */
    public short s(){
        if(invalid(2)) return 0;
        used += 2;
        return cache.s();
    }

    /** read unsigned short */
    public int us(){
        if(invalid(2)) return 0;
        used += 2;
        return cache.us();
    }

    /** read byte */
    public byte b(){
        if(invalid(1)) return 0;
        used += 1;
        return cache.b();
    }

    /** allocate & read byte array */
    public byte[] b(int length){
        if(invalid(length)) return new byte[length];
        used += length;
        return cache.b(length);
    }

    /** read byte array */
    public byte[] b(byte[] array){
        if(invalid(array.length)) return array;
        used += array.length;
        return cache.b(array);
    }

    /** read byte array w/ offset */
    public byte[] b(byte[] array, int offset, int length){
        if(invalid(length)) return array;
        used += length;
        return cache.b(array, offset, length);
    }

    /** read unsigned byte */
    public int ub(){
        if(invalid(1)) return 0;
        used += 1;
        return cache.ub();
    }

    /** read boolean */
    public boolean bool(){
        if(invalid(1)) return false;
        used += 1;
        return cache.bool();
    }

    /** read float */
    public float f(){
        if(invalid(4)) return 0;
        used += 4;
        return cache.f();
    }

    /** read double */
    public double d(){
        if(invalid(8)) return 0;
        used += 8;
        return cache.d();
    }

    /** read string (UTF) */
    public String str(){
        throw new RuntimeException("Safe string reading is not implemented.");
    }

    /** skip bytes */
    public void skip(int amount){
        if(invalid(amount)) return;
        used += amount;
        cache.skip(amount);
    }

    /** skip bytes without checking validity FOR INTERNAL USE ONLY */
    private void forceSkip(int amount){
        cache.skip(amount);
    }
}
