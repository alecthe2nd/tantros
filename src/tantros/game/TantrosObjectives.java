package tantros.game;

import arc.Core;
import mindustry.ctype.UnlockableContent;
import mindustry.game.Objectives;
import tantros.type.Recipe;

public class TantrosObjectives {

    public static class RecipeUnlocked implements Objectives.Objective{

        public Recipe content;

        public RecipeUnlocked(Recipe content){
            this.content = content;
        }

        protected RecipeUnlocked(){}

        @Override
        public boolean complete(){
            return true;//content.unlockedHost();
        }

        @Override
        public String display(){
            return "a recipe";
            //return Core.bundle.format("requirement.recipe",
            //        content.unlockedHost() ? (content.emoji() + " " + content.localizedName) : "???");
        }
    }
}
