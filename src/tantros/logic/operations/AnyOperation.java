package tantros.logic.operations;

import arc.func.Func;
import arc.struct.Seq;

public class AnyOperation extends Operation{

    public Func<Seq<Object>, Object> operation;

    public AnyOperation(String name, Func<Seq<Object>, Object> op) {
        super(name, 0);
        this.operation = op;
    }

    @Override
    public boolean acceptsParams(Seq<Object> params) {
        return true;
    }

    @Override
    public Object operate(Seq<Object> params) {
        return operation.get(params);
    }
}
