package tantros.type.buildConfig;

import arc.util.io.Reads;
import arc.util.io.Writes;
import tantros.logic.operations.Operation;
import tantros.logic.operations.Operations;

public class SetOperation extends BuildConfigurationUnit{

    public Operation operation = Operations.none;

    public int[] paramPriority = new int[4];

    @Override
    public void reset() {
        this.operation = Operations.none;
    }

    @Override
    public void read(Reads read) {
        this.operation = Operations.namedAll.get(read.str());
        if (this.operation == null){
            this.operation = Operations.none;
        }
        int size = read.b();
        for(int i = 0; i < size; i++){
            paramPriority[i] = read.b();
        }

    }

    @Override
    public void write(Writes write) {
        write.str(this.operation.name);

        write.b(paramPriority.length);
        for (int j : paramPriority) {
            write.b(j);
        }
    }
}
