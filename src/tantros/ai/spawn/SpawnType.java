package tantros.ai.spawn;

public class SpawnType {

    public static final SpawnType
            ambient = new SpawnType("ambient")
            ;

    public final String name;

    public SpawnType(String name){
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
