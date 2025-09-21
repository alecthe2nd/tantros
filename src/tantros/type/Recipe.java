package tantros.type;

import tantros.world.blocks.production.RecipeCrafter;

public class Recipe {

    public String name;

    public Resource cost;

    public Resource output;

    public float craftTime = 1;

    /** The heat required for each level of overheat.
     * Set to 0 to disable overheat.
     * */
    public float overheat;

    /** After heat meets the minimum requirement, excess heat will be scaled by this number. */
    public float overheatScale = 1f;

    /** Maximum possible efficiency after overheat. */
    public float maxEfficiency = 4f;

    public Recipe(String name){
        this.name = name;
    }

}
