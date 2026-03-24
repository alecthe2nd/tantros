package tantros.type.buildingState;

import arc.Core;
import arc.math.Mathf;
import arc.scene.ui.layout.Table;
import arc.struct.IntSet;
import arc.util.Nullable;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.Vars;
import mindustry.gen.Building;
import mindustry.graphics.Pal;
import mindustry.ui.Bar;
import mindustry.world.Block;
import mindustry.world.blocks.heat.HeatBlock;
import mindustry.world.blocks.heat.HeatConductor;
import tantros.type.blockConfig.HeatConsumptionConfig;
import tantros.ui.UIUtil;
import tantros.world.blocks.BlockExtended;

import java.util.Arrays;

public class InputHeatState implements BuildingState{

    @Nullable
    public HeatConsumptionConfig consumptionConfig;

    public float heat = 0;
    public float[] sideHeat = new float[4];
    public IntSet cameFrom = null;

    public long lastHeatUpdate;


    @Override
    public void initState(BlockExtended ownerType, BlockExtended.BuildExtended owner) {
        this.reset();
        consumptionConfig = ownerType.getBlockConfig(HeatConsumptionConfig.class);
    }

    @Override
    public void update(BlockExtended ownerType, BlockExtended.BuildExtended owner) {
        updateHeat(owner);
    }

    @Override
    public void onProximity(BlockExtended ownerType, BlockExtended.BuildExtended owner) {

    }

    @Override
    public void displayBars(BlockExtended.BuildExtended build, Table table) {
        if(consumptionConfig == null) {
            UIUtil.addBar(table, new Bar(() ->
                    Core.bundle.format("bar.heatamount", (int) (this.heat + 0.01f)),
                    () -> Pal.lightOrange,
                    () -> Mathf.clamp(this.heat))
            );
        } else {
            UIUtil.addBar(table, new Bar(() ->
                    Core.bundle.format("bar.heatpercent", (int) (this.heat + 0.01f), (int)(consumptionConfig.computeEfficiency(this.heat) * 100 + 0.01f)),
                    () -> Pal.lightOrange,
                    () -> this.heat / consumptionConfig.heatPerEfficiency)
            );
        }
    }

    @Override
    public void write(Writes write) {
        write.f(heat);
        for(int i = 0; i < 4; i++){
            write.f(sideHeat[i]);
        }
    }

    @Override
    public void read(Reads read) {
        heat = read.f();
        for(int i = 0; i < 4; i++){
            sideHeat[i] = read.f();
        }
    }

    @Override
    public void reset() {
        this.heat = 0f;
        for(int i = 0; i < 4; i++){
            sideHeat[i] = 0f;
        }
    }

    @Override
    public boolean isTransient() {
        return false;
    }

    @Override
    public String getName() {
        return "InputHeatState";
    }

    @Override
    public int getVersion() {
        return 0;
    }

    public void updateHeat(BlockExtended.BuildExtended owner){
        if(this.lastHeatUpdate == Vars.state.updateId) return;
        float add = 0f;
        this.lastHeatUpdate = Vars.state.updateId;
        for(var build : owner.proximity) {
            Arrays.fill(sideHeat, 0f);
            if(cameFrom != null) cameFrom.clear();

            if(build instanceof BlockExtended.BuildExtended){
                add += calculateHeat(owner, (BlockExtended.BuildExtended)build);
            } else {
                add += calculateHeatVanilla(owner, build);
            }

        }
        this.heat = add;
    }

    //calculates heat using the vanilla method, for vanilla blocks.
    public float calculateHeatVanilla(BlockExtended.BuildExtended owner, Building build) {
        float heat = 0.0F;
        OutputHeatState outputHeat = owner.getState(OutputHeatState.class);

        if (build != null && build.team == owner.team && build instanceof HeatBlock) {
            HeatBlock heater;
            boolean var10000;
            label59: {
                heater = (HeatBlock)build;
                Block var9 = build.block;
                if (var9 instanceof HeatConductor cond) {
                    if (cond.splitHeat) {
                        var10000 = true;
                        break label59;
                    }
                }

                var10000 = false;
            }

            boolean split = var10000;
            if (!build.block.rotate || !split && (owner.relativeTo(build) + 2) % 4 == build.rotation || split && owner.relativeTo(build) != build.rotation) {
                float add;
                label70: {
                    if (build instanceof HeatConductor.HeatConductorBuild hc) {

                        if (hc.cameFrom.contains(owner.id()) && outputHeat != null && outputHeat.heat > 0) {
                            break label70;
                        }
                    }

                    add = computeHeatContact(owner, build, heater.heat());
                    if (split) {
                        add /= 3.0F;
                    }

                    int var10001 = Mathf.mod(owner.relativeTo(build), 4);
                    sideHeat[var10001] += add;
                    heat += add;
                }

                if (cameFrom != null) {
                    cameFrom.add(build.id);
                    if (build instanceof HeatConductor.HeatConductorBuild hc) {
                        cameFrom.addAll(hc.cameFrom);
                    }
                }

                if (heater instanceof HeatConductor.HeatConductorBuild) {
                    HeatConductor.HeatConductorBuild cond = (HeatConductor.HeatConductorBuild)heater;
                    cond.updateHeat();
                }
            }
        }

        return heat;
    }

    //calculates heat for BuildExtended blocks
    public float calculateHeat(BlockExtended.BuildExtended owner, BlockExtended.BuildExtended build) {
        float heat = 0.0F;
        OutputHeatState outputHeat = owner.getState(OutputHeatState.class);
        InputHeatState otherHeatInput = build.getState(InputHeatState.class);
        OutputHeatState otherHeatOutput = build.getState(OutputHeatState.class);

        if (build.team == owner.team && otherHeatOutput != null) {
            int dir = Mathf.mod(owner.relativeTo(build), 4);
            float add;
            label70: {
                if (otherHeatInput != null
                        && otherHeatInput.cameFrom.contains(owner.id())
                        && outputHeat != null
                        && outputHeat.heat > 0) {
                    break label70;
                }

                add = computeHeatContact(owner, build, otherHeatOutput.sideHeat[Mathf.mod(dir + 2, 4)]);
                sideHeat[dir] += add;
                heat += add;
            }

            if (cameFrom != null) {
                cameFrom.add(build.id);
                if (otherHeatInput != null) {
                    cameFrom.addAll(otherHeatInput.cameFrom);
                }
            }

            if (otherHeatInput != null) {
                otherHeatInput.updateHeat(build);
            }
        }

        return heat;
    }

    public float computeHeatContact(Building owner, Building build, float heat){
        float diff = Math.min(Math.abs(build.x - owner.x), Math.abs(build.y - owner.y)) / 8.0F;
        int contactPoints = Math.min((int)((float)owner.block.size / 2.0F + (float)build.block.size / 2.0F - diff), Math.min(build.block.size, owner.block.size));
        return heat / (float)build.block.size * (float)contactPoints;
    }
}
