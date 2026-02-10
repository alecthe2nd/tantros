package tantros.type.blockUtil;



public class OnDestroyExplosionContext{
    public float flammability = 0f;
    public float explosiveness = 0f;
    public float radius = 0f;

    public void clear(){
        flammability = 0;
        explosiveness = 0;
        radius = 0f;
    }

    public void with(OnDestroyExplosionContext context){
        this.flammability = context.flammability;
        this.explosiveness = context.explosiveness;
        this.radius = context.radius;
    }
}
