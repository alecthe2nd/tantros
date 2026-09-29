package tantros.type.blockConfig;

import arc.func.Boolf;
import arc.struct.Seq;
import tantros.logic.operations.Operation;
import tantros.logic.operations.Operations;


public class OperationConfig implements BlockConfig{

    public String operationStateName = "";

    public Seq<Operation> operations = new Seq<>();

    public void add(Operation op){
        operations.add(op);
    }

    public void add(Seq<Operation> newOperations){
        operations.add(newOperations);
    }

    public void add(Boolf<Operation> filter){
        for(Operation op: Operations.all){
            if(filter.get(op)) {
                operations.add(op);
            }
        }
    }

}
