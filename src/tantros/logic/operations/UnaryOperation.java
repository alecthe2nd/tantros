package tantros.logic.operations;

import arc.func.Func;
import arc.struct.Seq;


public class UnaryOperation<T> extends Operation {

    public final Class<T> type;

    Func<T, Object> operation;

    public UnaryOperation(String name, Class<T> type, Func<T, Object> operation) {
        super(name, 1);
        this.type = type;
        this.operation = operation;

    }

    @Override
    public boolean acceptsParams(Seq<Object> params) {
        return false;
    }

    @Override
    public Object operate(Seq<Object> params) {
        return null;
    }
}
