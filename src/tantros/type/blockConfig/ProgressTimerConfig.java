package tantros.type.blockConfig;

public class ProgressTimerConfig implements BlockConfig {

    public float warmupSpeed = 0.005f;
    public float progressTime;

    public ProgressTimerConfig(float progressTime){
        this.progressTime = progressTime;
    }

    public ProgressTimerConfig(float progressTime, float warmupSpeed){
        this(progressTime);
        this.warmupSpeed = warmupSpeed;
    }

}
