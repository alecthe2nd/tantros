package tantros.type.buildingState.logic;

import arc.util.io.Reads;
import arc.util.io.Writes;
import tantros.type.buildingState.BuildingState;
import tantros.world.blocks.BlockExtended;

public class MessageState implements BuildingState {

    public StringBuilder message = new StringBuilder();

    public int maxTextLength = 400;
    public int maxNewlines = 24;

    @Override
    public void initState(BlockExtended ownerType, BlockExtended.BuildExtended owner) {

    }

    @Override
    public void update(BlockExtended ownerType, BlockExtended.BuildExtended owner) {

    }

    @Override
    public void onProximity(BlockExtended ownerType, BlockExtended.BuildExtended owner) {

    }

    @Override
    public String getName() {
        return "MessageState";
    }

    @Override
    public int getVersion() {
        return 0;
    }

    @Override
    public boolean isTransient() {
        return false;
    }

    @Override
    public void read(Reads read) {
        message.setLength(0);
        message.append(read.str());
    }

    @Override
    public void write(Writes write) {
        write.str(message.toString());
    }

    @Override
    public void reset() {
        message.setLength(0);
    }

    @Override
    public <E> void onConfig(BlockExtended.BuildExtended owner, E config) {
        if(config instanceof String text){
            if(text.length() > maxTextLength){
                return; //no.
            }

            this.message.ensureCapacity(text.length());
            this.message.setLength(0);

            text = text.trim();
            int count = 0;
            for(int i = 0; i < text.length(); i++){
                char c = text.charAt(i);
                if(c == '\n'){
                    if(count++ <= maxNewlines){
                        this.message.append('\n');
                    }
                }else{
                    this.message.append(c);
                }
            }
        }
    }
}
