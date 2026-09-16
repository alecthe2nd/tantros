package tantros.type.buildingState.logic.signalling;

import arc.math.geom.Geometry;
import arc.math.geom.Point2;
import mindustry.Vars;
import mindustry.gen.Building;
import mindustry.world.Tile;
import tantros.type.blockConfig.BeamLinkConfig;
import tantros.type.buildingState.BuildingState;
import tantros.world.blocks.BlockExtended;

public class BeamLinkState implements BuildingState {

    public BeamLinkConfig config;

    public Building[] links = new Building[4];
    public Tile[] dests = new Tile[4];
    public int lastChange = -2;

    public BeamLinkState(BeamLinkConfig config) {
        this.config = config;
    }

    @Override
    public void initState(BlockExtended ownerType, BlockExtended.BuildExtended owner) {

    }

    @Override
    public void update(BlockExtended ownerType, BlockExtended.BuildExtended build) {
        if (this.lastChange != Vars.world.tileChanges) {
            this.lastChange = Vars.world.tileChanges;
            this.updateDirections(build);
        }
    }

    public void updateDirections(BlockExtended.BuildExtended build) {
        for(int i = 0; i < 4; ++i) {
            Building prev = this.links[i];
            Point2 dir = Geometry.d4[i];
            this.links[i] = null;
            this.dests[i] = null;
            int offset = build.block.size / 2;

            for(int j = 1 + offset; j <= this.config.range + offset; ++j) {
                Building other = Vars.world.build(build.tile.x + j * dir.x, build.tile.y + j * dir.y);
                if (other != null && other.isInsulated()) {
                    break;
                }

                if (other != null && other.team == build.team && config.condition.get(build, other)) {
                    this.links[i] = other;
                    this.dests[i] = Vars.world.tile(build.tile.x + j * dir.x, build.tile.y + j * dir.y);
                    break;
                }
            }
        }
    }

    @Override
    public void onProximity(BlockExtended ownerType, BlockExtended.BuildExtended owner) {

    }

    @Override
    public String getName() {
        return "SignalBeamState";
    }

    @Override
    public int getVersion() {
        return 0;
    }

    @Override
    public void reset() {

    }
}
