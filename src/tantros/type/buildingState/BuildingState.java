package tantros.type.buildingState;

import tantros.world.blocks.production.ProductionBlock;

/** A state-holding device for buildings. Allows building types to store additional state as needed.
 * Do not overuse. If there is a possibility of doing something statelessly, do it that way.*/
public interface BuildingState {
    public void initState(ProductionBlock ownerType, ProductionBlock.ProductionBuild owner);
    public void update(ProductionBlock ownerType, ProductionBlock.ProductionBuild owner);
    public void onProximity(ProductionBlock ownerType, ProductionBlock.ProductionBuild owner);
}
