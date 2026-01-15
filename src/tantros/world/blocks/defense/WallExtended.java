package tantros.world.blocks.defense;

import arc.Core;
import arc.Events;
import arc.audio.Sound;
import arc.graphics.Blending;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.struct.Seq;
import arc.util.Time;
import mindustry.entities.Lightning;
import mindustry.entities.TargetPriority;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.gen.Bullet;
import mindustry.gen.Sounds;
import mindustry.graphics.Pal;
import mindustry.world.Block;
import mindustry.world.meta.BlockGroup;
import mindustry.world.meta.Env;
import tantros.world.blocks.defense.modifiers.DamageModifer;

import static mindustry.Vars.state;
import static mindustry.Vars.tilesize;

public class WallExtended extends Block {
    public Seq<DamageModifer> damageModifiers = new Seq<>();


    public boolean flashHit;
    public Color flashColor = Color.white;

    public WallExtended(String name) {
        super(name);
        solid = true;
        destructible = true;
        group = BlockGroup.walls;
        buildCostMultiplier = 6f;
        canOverdrive = false;
        drawDisabled = false;
        crushDamageMultiplier = 5f;
        priority = TargetPriority.wall;

        //it's a wall of course it's supported everywhere
        envEnabled = Env.any;
    }

    @Override
    public void setStats(){
        super.setStats();

        /*if(chanceDeflect > 0f) stats.add(Stat.baseDeflectChance, chanceDeflect, StatUnit.none);
        if(lightningChance > 0f){
            stats.add(Stat.lightningChance, lightningChance * 100f, StatUnit.percent);
            stats.add(Stat.lightningDamage, lightningDamage, StatUnit.none);
        }*/
    }

    @Override
    public void init(){
        if(size > 2 && destroySound == Sounds.unset) destroySound = Sounds.blockExplodeWall;
        super.init();
    }

    @Override
    public TextureRegion[] icons(){
        return new TextureRegion[]{Core.atlas.find(Core.atlas.has(name) ? name : name + "1")};
    }

    public class WallExtendedBuild extends Building {
        public float hit;

        @Override
        public void draw(){
            super.draw();

            //draw flashing white overlay if enabled
            if(flashHit){
                if(hit < 0.0001f) return;

                Draw.color(flashColor);
                Draw.alpha(hit * 0.5f);
                Draw.blend(Blending.additive);
                Fill.rect(x, y, tilesize * size, tilesize * size);
                Draw.blend();
                Draw.reset();

                if(!state.isPaused()){
                    hit = Mathf.clamp(hit - Time.delta / 10f);
                }
            }
        }

        @Override
        public boolean collision(Bullet bullet){
            super.collision(bullet);

            hit = 1f;
            boolean collides = true;

            for(DamageModifer modifier : damageModifiers){
                collides &= modifier.applyOnCollision(bullet, this);
            }

            return collides;
        }

        @Override
        public void damage(Bullet bullet, Team source, float damage) {
            for(DamageModifer modifier: damageModifiers){
                damage = modifier.modify(bullet, team, damage);
            }
            super.damage(source, damage);
            Events.fire(bulletDamageEvent.set(this, bullet));
        }
    }
}
