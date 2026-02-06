package tantros.util.io;

import arc.struct.Seq;
import arc.util.io.Writes;

public class WriteContext{

    //The Writes object this delegates to
    public Writes cache;

    public Seq<WriteOperation> operations = new Seq<>();

    //The amount of space that will be used should this object write.
    public int space = 0;

    public boolean init = false;

    public void init(Writes writer){
        this.cache = writer;
        operations.clear();
        space = 0;
        init = true;
    }

    public void flush(){
        if(!init) return;
        //store the amount of bytes we will be writing
        cache.i(space);

        //play out the operations in the order they were applied
        for(WriteOperation oper : operations){
            oper.get(cache);
        }
        init = false;
    }

    /** write long */
    public void l(long i){
        if(!init) return;
        space += 8;
        operations.add((w)->w.l(i));
    }

    /** write int */
    public void i(int i){
        if(!init) return;
        space += 4;
        operations.add((w)->w.i(i));
    }

    /** write byte */
    public void b(int i){
        if(!init) return;
        space += 1;
        operations.add((w)->w.b(i));
    }

    /** write bytes */
    public void b(byte[] array, int offset, int length){
        if(!init) return;
        space += length;
        operations.add((w)->w.b(array, offset, length));
    }

    /** write bytes */
    public void b(byte[] array){
        if(!init) return;
        b(array, 0, array.length);
    }

    /** write boolean (writes a byte internally) */
    public void bool(boolean b){
        if(!init) return;
        b(b ? 1 : 0);
    }

    /** write short */
    public void s(int i){
        if(!init) return;
        space += 2;
        operations.add((w)->w.s(i));

    }

    /** write float */
    public void f(float f){
        if(!init) return;
        space += 4;
        operations.add((w)->w.f(f));
    }

    /** write double */
    public void d(double d){
        if(!init) return;
        space += 8;
        operations.add((w)->w.d(d));

    }

    /** writes a string (UTF) */
    public void str(String str){
        throw new RuntimeException("Safe string writing is not implemented.");
    }
}
