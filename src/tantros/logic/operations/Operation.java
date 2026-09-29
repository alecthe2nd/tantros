package tantros.logic.operations;


import arc.struct.Seq;

public abstract class Operation {

    public final String name;

    public final int params;

    public int placement = 0;

    public Operation(String name, int params){
        this.name = name;
        this.params = params;
        Operations.all.add(this);
        Operations.namedAll.put(name, this);
    }

    public abstract boolean acceptsParams(Seq<Object> params);

    public abstract Object operate(Seq<Object> params);

    @Override
    public String toString() {
        return name;
    }

}
