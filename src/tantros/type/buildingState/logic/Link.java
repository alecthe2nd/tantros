package tantros.type.buildingState.logic;


import mindustry.gen.Building;
import tantros.world.blocks.logic.LinkableComponent;

public class Link{
    public int x,y;
    public boolean valid;
    public Building build;

    public Link(int x, int y, boolean valid){
        this.x = x;
        this.y = y;
        this.valid = valid;
    }

    public Link copy(){
        return new Link(x, y, valid);
    }
}
