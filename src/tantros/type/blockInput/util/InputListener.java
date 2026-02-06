package tantros.type.blockInput.util;

import arc.Events;
import arc.func.Cons;
import arc.func.Cons2;
import arc.util.pooling.Pool;
import arc.util.pooling.Pools;
import tantros.world.blocks.BlockExtended;

public class InputListener<E> implements Pool.Poolable, Cons<E> {

    private final Cons2<BlockExtended.BuildExtended, E> DEFAULT_HANDLER = ( b, e)->{};
    public Class<E> type;
    public Cons2<BlockExtended.BuildExtended, E> handler = DEFAULT_HANDLER;
    public BlockExtended.BuildExtended owner;
    public boolean active = false;

    @Override
    public void reset() {
        handler = DEFAULT_HANDLER;
        owner = null;
        this.active = false;
    }

    public void prepare( Class<E> type, Cons2<BlockExtended.BuildExtended, E> handler, BlockExtended.BuildExtended owner){
        this.handler = handler;
        this.type = type;
        this.owner = owner;
        this.active = true;
        Events.on(type, this);
    }

    public void detach(){
        if(this.active) {
            Pools.free(this);
            Events.remove(type,this);
        }
    }

    @Override
    public void get(E e) {
        if(owner != null) {
            handler.get(owner, e);
        }
    }
}
