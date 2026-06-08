package tantros.ai;

import arc.struct.Seq;
import mindustry.ai.UnitCommand;
import mindustry.ai.UnitStance;
import mindustry.ai.types.BuilderAI;
import mindustry.input.Binding;
import tantros.ai.types.BurrowAI;
import tantros.ai.types.GroundBuilderAI;
import tantros.ai.types.GroundMinerAI;
import tantros.ai.types.GroundRepairAI;

public class TantrosUnitCommands {
    public static UnitCommand
            burrowCommand,
            groundRepairCommand,
            groundMineCommand,
            groundRebuildCommand,
            groundAssistCommand,
            processorCommand
                    ;



    public static void load() {

        burrowCommand = new UnitCommand("burrow", "down", null, u -> new BurrowAI()) {{
            switchToMove = false;
            drawTarget = true;
            resetTarget = false;
        }};

        groundRepairCommand = new UnitCommand("groundRepairCommand", "modeSurvival", null, u -> new GroundRepairAI()) {{
            switchToMove = true;
            drawTarget = false;
            resetTarget = false;

        }};

        groundMineCommand = new UnitCommand("groundMineCommand", "production", null, u -> new GroundMinerAI()) {{
            switchToMove = true;
            drawTarget = false;
            resetTarget = false;
        }};
        groundRebuildCommand = new UnitCommand("groundRebuildCommand", "hammer", Binding.unitCommandRebuild, u -> new GroundBuilderAI());
        groundAssistCommand = new UnitCommand("groundAssistCommand", "players", Binding.unitCommandAssist, u -> {
            var ai = new GroundBuilderAI();
            ai.onlyAssist = true;
            return ai;
        });

        Seq.with(groundRepairCommand, groundRebuildCommand, groundAssistCommand)
                .each(c -> c.extraStances.add(UnitStance.holdPosition));
    }
}
