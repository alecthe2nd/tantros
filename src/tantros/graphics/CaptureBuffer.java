package tantros.graphics;

import arc.Core;
import arc.graphics.Color;
import arc.graphics.Gl;
import arc.graphics.Pixmap;
import arc.graphics.gl.FrameBuffer;

public class CaptureBuffer extends FrameBuffer {

    protected boolean capturing = false;

    public CaptureBuffer(){
        super(Pixmap.Format.rgba8888, Core.graphics.getWidth(), Core.graphics.getHeight(), false);
    }

    public void capture(){
        if (!capturing) {
            capturing = true;
            this.begin(Color.clear);
        }
    }

    public void stopCapture(){

        if (capturing) {
            capturing = false;
            this.end();
        }
    }

}
