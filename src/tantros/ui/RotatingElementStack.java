package tantros.ui;

import arc.func.Boolf;
import arc.scene.Element;
import arc.scene.ui.layout.Stack;
import arc.struct.Seq;
import arc.util.Time;
import mindustry.ui.ReqImage;

/*
* A Element stack that selects one child element for display every displayTime.
* A particular element can be forced to be displayed via the priorityCondition.
* */
public class RotatingElementStack  extends Stack {
    private Seq<Element> displays = new Seq<>();
    private float time;

    public Boolf<Element> priorityCondition = (e)-> false;
    public float displayTime = 60f;

    @Override
    public void add(Element display){
        displays.add(display);
        super.add(display);
    }

    @Override
    public void act(float delta){
        super.act(delta);

        time += Time.delta / displayTime;

        displays.each(req -> req.visible = false);

        Element valid = displays.find(priorityCondition);
        if(valid != null){
            valid.visible = true;
        }else{
            if(displays.size > 0){
                displays.get((int)time % displays.size).visible = true;
            }
        }
    }
}