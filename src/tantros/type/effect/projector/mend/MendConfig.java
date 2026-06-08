package tantros.type.effect.projector.mend;

import arc.audio.Sound;
import arc.graphics.Color;
import mindustry.gen.Sounds;
import tantros.graphics.TantrosPal;
import tantros.type.blockConfig.BlockConfig;


public class MendConfig implements BlockConfig {

    public float heal = 1f;

    public Color mendColor = TantrosPal.mendLight;

    public Sound mendSound = Sounds.healWave;
    public float mendSoundVolume = 0.5f;

    /** The type of mend this is. Used to interpret {@code heal} */
    public MendType mendType = MendType.RELATIVE;

    public MendConfig(){}

    public MendConfig(float heal){
        this.heal = heal;
    }

    public MendConfig(float heal, MendType mendType){
        this.heal = heal;
        this.mendType = mendType;
    }

    public enum MendType{

        /**
         * Heals in terms of absolute health values, I.E. health per trigger
         * */
        ABSOLUTE,
        /**
         * Heals in terms of relative health values, I.E. percent healing
         * */
        RELATIVE
    }

}
