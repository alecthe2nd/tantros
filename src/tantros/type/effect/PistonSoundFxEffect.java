package tantros.type.effect;

import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.util.Tmp;
import mindustry.content.Fx;
import mindustry.entities.Effect;
import mindustry.entities.effect.SoundEffect;
import mindustry.gen.Sounds;
import tantros.world.blocks.BlockExtended;

public class PistonSoundFxEffect implements BlockEffect {

    public SoundEffect sfx = new SoundEffect(Sounds.none, Fx.none);
    public float sinMag = 4f, sinScl = 6f, sinOffset = 50f, sideOffset = 0f, lenOffset = -1f, horiOffset = 0f, angleOffset = 0f;
    public int sides = 4;

    public float angle = 45;
    public float length = 8;
    public float effectSensitivity = 0.005f;

    @Override
    public void update(BlockExtended.BuildExtended build) {
        for(int i = 0; i < sides; i++){
            float len = Mathf.absin(build.totalProgress() + sinOffset + sideOffset * sinScl * i, sinScl, sinMag) + lenOffset;
            float angle = angleOffset + i * 360f / sides;


            Tmp.v1.trns(angle, len, -horiOffset);
            if(Mathf.equal(len, sinMag + lenOffset, effectSensitivity * build.efficiency)) {
                sfx.at(build.x + Tmp.v1.x, build.y + Tmp.v1.y);
            }
        }
    }
}
