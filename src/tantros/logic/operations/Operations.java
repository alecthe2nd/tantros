package tantros.logic.operations;

import arc.struct.ObjectMap;
import arc.struct.Seq;
import arc.util.Log;
import mindustry.logic.LogicOp;

public class Operations {

    public static final Seq<Operation> all = new Seq<>();

    public static final ObjectMap<String, Operation> namedAll = new ObjectMap<>();

    static {
        for(LogicOp op: LogicOp.all){
            Operation operation = new VanillaOperation(op);
        }
    }
    public static final Operation

            none = new AnyOperation("none", (p)->null),

            add = namedAll.get("+"),
            sub = namedAll.get("-"),
            mul = namedAll.get("*"),
            div = namedAll.get("/"),
            idiv = namedAll.get("//"),
            mod = namedAll.get("%"),
            emod = namedAll.get("%%"),
            pow = namedAll.get("^"),

            equal = namedAll.get("=="),
            notEqual = namedAll.get("not"),
            land = namedAll.get("and"),
            lessThan = namedAll.get("<"),
            lessThanEq = namedAll.get("<="),
            greaterThan = namedAll.get(">"),
            greaterThanEq = namedAll.get(">="),
            strictEqual = namedAll.get("==="),
            shl = namedAll.get("<<"),
            shr = namedAll.get(">>"),
            ushr = namedAll.get(">>>"),
            or = namedAll.get("or"),
            and = namedAll.get("b-and"),
            xor = namedAll.get("xor"),
            not = namedAll.get("flip"),

            max = namedAll.get("max"),
            min = namedAll.get("min"),
            angle = namedAll.get("angle"),
            angleDiff = namedAll.get("anglediff"),
            len = namedAll.get("len"),
            noise = namedAll.get("noise"),
            abs = namedAll.get("abs"),
            sign = namedAll.get("sign"),
            log = namedAll.get("log"),
            logn = namedAll.get("logn"),
            log10 = namedAll.get("log10"),
            floor = namedAll.get("floor"),
            ceil = namedAll.get("ceil"),
            round = namedAll.get("round"),
            sqrt = namedAll.get("sqrt"),
            rand = namedAll.get("rand"),

            sin = namedAll.get("sin"),
            cos = namedAll.get("cos"),
            tan = namedAll.get("tan"),

            asin = namedAll.get("asin"),
            acos = namedAll.get("acos"),
            atan = namedAll.get("atan")
    ;

}
