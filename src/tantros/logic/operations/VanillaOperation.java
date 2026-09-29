package tantros.logic.operations;

import arc.func.Func;
import arc.func.Func2;
import arc.struct.Seq;
import arc.util.Log;
import mindustry.logic.LogicOp;

import java.lang.reflect.Method;

public class VanillaOperation extends Operation {

    public LogicOp operation;

    public Func<Double, Double> opLambda1;

    public Func2<Double, Double, Double> opLambda2;

    public Func2<Object, Object, Double> opObjLambda2;

    public VanillaOperation(LogicOp op){
        super(op.symbol, op.unary?1:2);
        this.operation = op;
        opLambda1 = jailbreakDouble1(op);
        opLambda2 = jailbreakDouble2(op);
        opObjLambda2 = jailbreakObject2(op);
        this.placement = (!op.unary && op.func)?0:1;
    }

    @Override
    public boolean acceptsParams(Seq<Object> params) {
        if(operation.unary && operation.function1 != null){
            return params.size == 1 && (params.firstOpt() instanceof Double);
        } else if(operation.function2 != null){
            return params.size == 2 && (params.get(0) instanceof Double) && (params.get(1) instanceof Double);
        } else if(operation.objFunction2 != null){
            return params.size == 2 && (params.get(0) != null) && (params.get(1) != null);
        }
        return false;
    }

    @Override
    public Object operate(Seq<Object> params) {
        if(!acceptsParams(params)) return null;
        if(operation.unary && operation.function1 != null){
            if(params.size == 1 && (params.firstOpt() instanceof Double d)){
                return this.opLambda1.get(d);
            };
        } else if(operation.function2 != null){
            if(params.size == 2 && (params.get(0) instanceof Double d1) && (params.get(1) instanceof Double d2)){
                return this.opLambda2.get(d1, d2);
            }
        } else if(operation.objFunction2 != null){
            if(params.size == 2){
                return this.opObjLambda2.get(params.get(0), params.get(1));
            }
        }
        return null;
    }

    public Func<Double, Double> jailbreakDouble1(LogicOp operation){

        return (d1)->{
            try{
                Log.info(((Object)(operation.function1)).getClass());
                Method method = ((Object)(operation.function1)).getClass().getDeclaredMethod("get", double.class);
                method.setAccessible(true);
                return (Double) method.invoke(operation.function1, d1);
            }catch(Exception e){
                Log.err("Failed to Jailbreak double 1, returning null", e);
                return null;
            }
        };
    }

    public Func2<Double, Double, Double> jailbreakDouble2(LogicOp operation){

        return (d1, d2)->{
            try{
                Log.info(((Object)(operation.function2)).getClass());
                Method method = ((Object)(operation.function2)).getClass().getDeclaredMethod("get", double.class, double.class);
                method.setAccessible(true);
                return (Double) method.invoke(operation.function2, d1, d2);
            }catch(Exception e){
                Log.err("Failed to Jailbreak double 2, returning null", e);
                return null;
            }
        };
    }

    public Func2<Object, Object, Double> jailbreakObject2(LogicOp operation){

        return (d1, d2)->{
            try{
                Log.info(((Object)(operation.objFunction2)).getClass());
                Method method = ((Object)(operation.objFunction2)).getClass().getDeclaredMethod("get", Object.class, Object.class);
                method.setAccessible(true);
                return (Double) method.invoke(operation.objFunction2, d1, d2);
            }catch(Exception e){
                Log.err("Failed to Jailbreak object 2, returning null", e);
                return null;
            }
        };
    }
}
