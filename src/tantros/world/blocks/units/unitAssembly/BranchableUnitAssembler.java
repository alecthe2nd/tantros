package tantros.world.blocks.units.unitAssembly;

import arc.Core;
import arc.Graphics;
import arc.graphics.Color;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.scene.ui.ButtonGroup;
import arc.scene.ui.ImageButton;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import arc.util.Nullable;
import arc.util.Scaling;
import arc.util.Strings;
import mindustry.ai.UnitCommand;
import mindustry.content.Fx;
import mindustry.entities.Units;
import mindustry.gen.Icon;
import mindustry.gen.Player;
import mindustry.graphics.Pal;
import mindustry.type.LiquidStack;
import mindustry.type.PayloadStack;
import mindustry.type.UnitType;
import mindustry.ui.Styles;
import mindustry.world.blocks.payloads.PayloadBlock;
import mindustry.world.blocks.payloads.UnitPayload;
import mindustry.world.blocks.units.*;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatUnit;
import mindustry.world.meta.StatValues;

import static mindustry.Vars.net;

public class BranchableUnitAssembler extends UnitAssembler {

    public BranchedAssemblerUnitPlan planTree;

    public BranchableUnitAssembler(String name) {
        super(name);
        configurable = true;
        config(UnitCommand.class, (BranchableUnitAssemblerBuild build, UnitCommand command) -> build.command = command);
        configClear((Reconstructor.ReconstructorBuild build) -> build.command = null);
    }

    @Override
    public void setStats(){

        stats.add(Stat.size, "@x@", size, size);

        if(synthetic()){
            stats.add(Stat.health, health, StatUnit.none);
            if(armor > 0){
                stats.add(Stat.armor, armor, StatUnit.none);
            }
        }

        if(canBeBuilt() && requirements.length > 0){
            stats.add(Stat.buildTime, buildTime / 60, StatUnit.seconds);
            stats.add(Stat.buildCost, StatValues.items(false, requirements));
        }

        if(instantTransfer){
            stats.add(Stat.maxConsecutive, 2, StatUnit.none);
        }

        for(var c : consumers){
            c.display(stats);
        }

        //Note: Power stats are added by the consumers.
        if(hasLiquids) stats.add(Stat.liquidCapacity, liquidCapacity, StatUnit.liquidUnits);
        if(hasItems && itemCapacity > 0) stats.add(Stat.itemCapacity, itemCapacity, StatUnit.items);

        stats.add(Stat.output, table -> {
            table.row();

            int tier = 0;
            for(var plan : plans){
                int ttier = (plan instanceof BranchedAssemblerUnitPlan branch)? branch.tier(): tier;
                table.table(Styles.grayPanel, t -> {

                    if(plan.unit.isBanned()){
                        t.image(Icon.cancel).color(Pal.remove).size(40).pad(10);
                        return;
                    }

                    if(plan.unit.unlockedNow()){
                        t.image(plan.unit.uiIcon).scaling(Scaling.fit).size(40).pad(10f).left().with(i -> StatValues.withTooltip(i, plan.unit));
                        t.table(info -> {
                            info.defaults().left();
                            info.add(plan.unit.localizedName);
                            info.row();
                            info.add(Strings.autoFixed(plan.time / 60f, 1) + " " + Core.bundle.get("unit.seconds")).color(Color.lightGray);
                            if(ttier > 0){
                                info.row();
                                info.add(Stat.moduleTier.localized() + ": " + ttier).color(Color.lightGray);
                            }
                        }).left();

                        t.table(req -> {
                            req.add().grow(); //it refuses to go to the right unless I do this. please help.

                            req.table(solid -> {
                                int length = 0;
                                if(plan.itemReq != null){
                                    for(int i = 0; i < plan.itemReq.length; i++){
                                        if(length % 6 == 0){
                                            solid.row();
                                        }
                                        solid.add(StatValues.stack(plan.itemReq[i])).pad(5);
                                        length++;
                                    }
                                }

                                for(int i = 0; i < plan.requirements.size; i++){
                                    if(length % 6 == 0){
                                        solid.row();
                                    }
                                    solid.add(StatValues.stack(plan.requirements.get(i))).pad(5);
                                    length++;
                                }
                            }).right();

                            LiquidStack[] stacks = plan.liquidReq;
                            if(stacks != null){
                                for(int i = 0; i < plan.liquidReq.length; i++){
                                    req.row();

                                    req.add().grow(); //another one.

                                    req.add(StatValues.displayLiquid(stacks[i].liquid, stacks[i].amount * 60f, true)).right();
                                }
                            }
                        }).grow().pad(10f);
                    }else{
                        t.image(Icon.lock).color(Pal.darkerGray).size(40).pad(10);
                    }
                }).growX().pad(5);
                table.row();
                if (!(plan instanceof BranchedAssemblerUnitPlan)){
                    tier++;
                }
            }
        });
    }

    @Override
    public void init() {
        plans = new Seq<>();
        planTree.loadSeq(plans);
        super.init();
    }



    public static class BranchedAssemblerUnitPlan extends AssemblerUnitPlan{
        public Seq<BranchedAssemblerUnitPlan> children = Seq.with();
        protected int tier = 0;

        public BranchedAssemblerUnitPlan(UnitType unit, float time, Seq<PayloadStack> requirements){
            super(unit, time, requirements);
        }

        public int tier(){
            return tier;
        }

        public BranchedAssemblerUnitPlan addChild(BranchedAssemblerUnitPlan child){
            this.children.addUnique(child);
            child.tier = this.tier + 1;
            return this;
        }

        public boolean hasChild(BranchedAssemblerUnitPlan child){
            return this.children.contains(child);
        }

        public void loadSeq(Seq<AssemblerUnitPlan> out){
            boolean isNew = out.addUnique(this);
            if (isNew){
                for(var child: children){
                    child.loadSeq(out);
                }
            }
        }
    }

    public class BranchableUnitAssemblerBuild extends UnitAssemblerBuild{

        public BranchedAssemblerUnitPlan currentPlan = planTree;
        public @Nullable UnitCommand command;

        @Override
        public void checkTier(){
            if(planTree == null) return;
            modules.sort(UnitAssemblerModule.UnitAssemblerModuleBuild::tier);
            int max = 0;
            BranchedAssemblerUnitPlan lastPlan = planTree;
            for(int i = 0; i < modules.size; i++){
                var mod = modules.get(i);
                if(!(mod instanceof BranchedUnitAssemblerModule.BranchedUnitAssemblerModuleBuild module)) continue;
                var modPlan = module.plan();
                if((module.tier() == max || module.tier() == max + 1) && (modPlan == lastPlan || lastPlan.hasChild(modPlan))){
                    max = module.tier();
                    lastPlan = modPlan;
                }else{
                    //tier gap, TODO warning?
                    break;
                }
            }
            currentTier = max;
            currentPlan = lastPlan;
        }

        @Override
        public AssemblerUnitPlan plan() {
            return currentPlan;
        }



        public boolean canSetCommand(){
            var output = unit();
            return output != null && output.commands.size > 1 && output.allowChangeCommands;
        }

        @Override
        public Graphics.Cursor getCursor(){
            return canSetCommand() ? super.getCursor() : Graphics.Cursor.SystemCursor.arrow;
        }

        @Override
        public boolean shouldShowConfigure(Player player){
            return canSetCommand();
        }

        @Override
        public void buildConfiguration(Table table){
            var unit = unit();

            if(unit == null){
                deselect();
                return;
            }

            var group = new ButtonGroup<ImageButton>();
            group.setMinCheckCount(0);
            int i = 0, columns = 4;

            table.background(Styles.black6);

            var list = unit().commands;
            for(var item : list){
                ImageButton button = table.button(item.getIcon(), Styles.clearNoneTogglei, 40f, () -> {
                    configure(item);
                    deselect();
                }).tooltip(item.localized()).group(group).get();

                button.update(() -> button.setChecked(command == item || (command == null && unit.defaultCommand == item)));

                if(++i % columns == 0){
                    table.row();
                }
            }
        }

        @Override
        public void spawned() {
            var plan = plan();
            Vec2 spawn = getUnitSpawn();
            consume();

            var unit = plan.unit.create(team);
            if(unit.isCommandable()) {

                if (commandPos != null) {
                    unit.command().commandPosition(commandPos);
                }
                unit.command().command(command == null && unit.type.defaultCommand != null ? unit.type.defaultCommand : command);
            }
            unit.set(spawn.x + Mathf.range(0.001f), spawn.y + Mathf.range(0.001f));
            unit.rotation = rotdeg();
            var targetBuild = unit.buildOn();
            //'source' is the target build instead of this building; this is because some blocks only accept things from certain angles, and this is a non-standard payload
            var payload = new UnitPayload(unit);
            if(targetBuild != null && targetBuild.team == team && targetBuild.acceptPayload(targetBuild, payload)){
                targetBuild.handlePayload(targetBuild, payload);
            }else if(!net.client()){
                unit.add();
                Units.notifyUnitSpawn(unit);
            }

            progress = 0f;
            Fx.unitAssemble.at(spawn.x, spawn.y, 0f, plan.unit);
            blocks.clear();
        }
    }
}
