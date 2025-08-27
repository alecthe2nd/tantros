package tantros.world.blocks.environment;

import arc.graphics.g2d.Draw;
import mindustry.editor.EditorTile;
import mindustry.game.SpawnGroup;
import mindustry.world.Tile;
import mindustry.world.blocks.environment.SpawnBlock;
import tantros.ai.spawn.SpawnType;

public class GenericSpawnBlock extends SpawnBlock {
    public SpawnGroup group;

    public SpawnType type;

    public GenericSpawnBlock(String name, SpawnGroup group, SpawnType type) {
        super(name);
        this.group = group;
        this.type = type;
    }

    @Override
    public void drawBase(Tile tile) {
        if(tile instanceof EditorTile){
            Draw.rect(group.type.uiIcon, tile.worldx(), tile.worldy());
        }
    }
}
