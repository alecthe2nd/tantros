package tantros.graphics;

import arc.Events;
import arc.graphics.Gl;
import arc.graphics.g2d.Draw;
import mindustry.game.EventType;
import mindustry.graphics.Shaders;

public class RadarShader extends Shaders.LoadShader {

    CaptureBuffer objectBuffer;
    CaptureBuffer scannerBuffer;

    public RadarShader() {
        super("radar", "screenspace");
        objectBuffer = new CaptureBuffer();
        scannerBuffer = new CaptureBuffer();
        Events.run(EventType.Trigger.draw, this::draw);
    }

    @Override
    public void apply() {
        this.setUniformi("u_texture1", 1);
    }

    public void draw() {
        Draw.drawRange(TantrosLayers.radarObjectLayer, objectBuffer::capture, objectBuffer::stopCapture);
        Draw.drawRange(TantrosLayers.radarMaskLayer, scannerBuffer::capture, scannerBuffer::stopCapture);
        Draw.draw(TantrosLayers.radarEffectLayer,()->{
            scannerBuffer.getTexture().bind(1);
            objectBuffer.blit(this);
        });
    }

    @Override
    public void dispose() {
        try {
            objectBuffer.dispose();

            super.dispose();
        } catch (Throwable ignored) {
        }
    }
}
