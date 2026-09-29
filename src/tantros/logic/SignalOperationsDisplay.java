package tantros.logic;

import arc.Core;
import arc.scene.ui.layout.Table;
import arc.struct.Bits;
import arc.struct.Seq;
import mindustry.core.UI;
import mindustry.ui.Styles;
import tantros.logic.operations.Operation;
import tantros.logic.operations.Operations;
import tantros.type.blockConfig.BeamLinkConfig;
import tantros.type.buildingState.OperationSettingState;

public class SignalOperationsDisplay extends BaseDisplay{

    public static String[] facingDirs = new String[]{"front", "left", "back", "right"};

    public static String[] trueDirs = new String[]{"east", "north", "west", "south"};

    public IntString[] bundledDirs = new IntString[4];

    public Seq<Operation> operations = new Seq<>();

    public Operation operation = Operations.none;

    public Bits dirs = new Bits(4);

    public int[] paramPriority = new int[4];

    public boolean rotates = false;

    public SignalOperationsDisplay(){
        for(int i = 0; i < bundledDirs.length; i++){
            bundledDirs[i] = new IntString();
            bundledDirs[i].index = i;
        }
    }

    @Override
    public void build(Table table) {
        rebuild(table);
    }

    public void rebuild(Table table){
        table.clearChildren();
        table.table((ops)->{
            ops.clearChildren();
            table.add(Core.bundle.get("ui.logic.operation.perform")).padRight(6);
            int paramsSoFar = 0;
            if(operation.params > 1) {
                for (int i = 0; i < operation.params && i < 4 && i < operation.placement; i++) {
                    int finalI = i;
                    ops.button(b -> {
                        b.label(() -> bundledDirs[paramPriority[finalI]].string);

                        b.clicked(() -> showSelect(b, bundledDirs, bundledDirs[paramPriority[finalI]], s -> {
                            ensurePriority((s.index) % 4, finalI);
                            rebuild(table);
                        }));
                    }, Styles.logict, ()->{}).height(40f).margin(0).padRight(2).padLeft(2);
                    paramsSoFar++;
                }
            }

            ops.button(b -> {
                    b.label(() -> Core.bundle.format("ui.logic.operation", operation.name));
                    //240
                    b.clicked(() -> showSelect(b, operations, operation, o -> {
                        operation = o;
                        rebuild(table);
                    }));
                }, Styles.logict, () -> {
            }).height(40f).padLeft(-1).padRight((operation.params>0)?3:0).margin(0).color(ops.color);

            if(operation.params > 1) {
                for (int i = paramsSoFar; i < operation.params && i < 4; i++) {
                    int finalI = i;
                    ops.button(b -> {
                        b.label(() -> bundledDirs[paramPriority[finalI]].string);

                        b.clicked(() -> showSelect(b, bundledDirs, bundledDirs[paramPriority[finalI]], s -> {
                            ensurePriority((s.index) % 4, finalI);
                            rebuild(table);
                        }));
                    }, Styles.logict, ()->{}).height(40f).margin(0).padRight(2).padLeft(2);
                }
            }



        //table.add( " " + Core.bundle.get("ui.logic.signal-parser.from"));
        });

    }



    public String directionSelector(int dir){
        String[] names;
        if(rotates){
            names = facingDirs;
        } else{
            names = trueDirs;
        }
        if(dir >= 4){
            return "ohno";
        }
        return UI.formatIcons(Core.bundle.format("ui.logic.operation.params." + names[dir]));
    }

    public void set(OperationSettingState state, BeamLinkConfig beamLinkConfig, boolean rotates){
        this.operation = state.currentOperation;
        if(state.currentOperation == null){
            this.operation = Operations.none;
        }
        this.operations.clear();
        this.operations.add(state.config.operations);
        this.dirs.set(beamLinkConfig.dir);
        this.paramPriority = state.paramPriority;
        this.rotates = rotates;
        for(int i = 0; i < 4; i++){
            bundledDirs[i].string = directionSelector(i);
            bundledDirs[i].index = i;
        }
    }

    public void ensurePriority(int dir, int param){
        int last = this.paramPriority[param];
        for(int i = 0; i < operation.params; i++) {
            if(this.paramPriority[i] == dir){
                this.paramPriority[i] = last;
                break;
            }
        }
        this.paramPriority[param] = dir;
    }

    public class IntString{
        public String string;
        public int index;

        public IntString(){
            this.string = "";
            this.index = -1;
        }

        @Override
        public String toString() {
            return string;
        }
    }
}
