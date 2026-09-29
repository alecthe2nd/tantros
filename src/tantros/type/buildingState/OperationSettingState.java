package tantros.type.buildingState;

import arc.struct.IntSeq;
import arc.struct.IntSet;
import arc.util.io.Reads;
import arc.util.io.Writes;
import arc.util.pooling.Pools;
import tantros.logic.operations.Operation;
import tantros.logic.operations.Operations;
import tantros.type.blockConfig.OperationConfig;
import tantros.type.buildConfig.SetOperation;
import tantros.world.blocks.BlockExtended;

public class OperationSettingState implements BuildingState{

    public static IntSet tmpSet = new IntSet();

    public OperationConfig config;

    public Operation currentOperation = Operations.none;

    public int[] paramPriority = new int[4];

    public boolean dirty = false;

    public OperationSettingState(OperationConfig config){
        this.config = config;
    }

    @Override
    public <E> void onConfig(BlockExtended.BuildExtended owner, E config) {
        if( config instanceof SetOperation setOp){
            this.currentOperation = setOp.operation;
            this.dirty = true;
            this.paramPriority = setOp.paramPriority;
            ensure();
            Pools.free(setOp);
        }
    }

    @Override
    public boolean isTransient() {
        return false;
    }

    @Override
    public String getName() {
        return "OperationSettingState";
    }

    @Override
    public int getVersion() {
        return 2;
    }

    @Override
    public void write(Writes write) {
        write.bool(this.dirty);
        write.str(this.currentOperation.name);
        write.b(paramPriority.length);
        for (int j : paramPriority) {
            write.b(j);
        }
    }

    @Override
    public void read(Reads read) {
        this.dirty = read.bool();
        this.currentOperation = Operations.namedAll.get(read.str());
        if (this.currentOperation == null){
            this.currentOperation = Operations.none;
        }
        int size = read.b();
        for(int i = 0; i < size; i++){
            paramPriority[i] = read.b();
        }
        ensure();
    }

    @Override
    public void reset() {
        this.dirty = true;
        this.currentOperation = Operations.none;
        paramPriority = new int[]{0,1,2,3};
        ensure();
    }

    public void ensure(){
        tmpSet.clear();
        for(int i = 0; i < paramPriority.length; i++){
            int prio = paramPriority[i];
            if(!tmpSet.contains(prio)){
                tmpSet.add(prio);
            } else {
                paramPriority[i] = -1;
            }
        }
        int next = 0;
        for(int i = 0; i < paramPriority.length; i++){
            int prio = paramPriority[i];
            if(prio == -1){
                while(tmpSet.contains(next)){
                    next++;
                }
                paramPriority[i] = next;
                tmpSet.add(next);
            }
        }
    }
}
