package tantros.type;

import arc.Core;
import arc.scene.ui.layout.Table;
import mindustry.ctype.ContentType;
import mindustry.ctype.UnlockableContent;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatUnit;
import mindustry.world.meta.StatValues;
import tantros.world.meta.TantrosStats;

public class Recipe extends UnlockableContent {

    public Resource cost;

    public Resource output;

    public float craftTime = 1;

    /** The heat required for each level of overheat.
     * Set to 0 to disable overheat.
     * */
    public float overheat;

    /** After heat meets the minimum requirement, excess heat will be scaled by this number. */
    public float overheatScale = 1f;

    /** Maximum possible efficiency after overheat. */
    public float maxEfficiency = 4f;

    public Recipe(String name){
        super(name);
        hideDatabase = true;
    }

    @Override
    public ContentType getContentType(){
        return ContentType.error;
    }

    @Override
    public void setStats(){
        stats.add(Stat.productionTime, craftTime / 60f, StatUnit.seconds);
        stats.add(Stat.input, (t) -> {
            TantrosStats.displayResource(t, this.cost, this.craftTime);
        });
        stats.add(Stat.output, (t) -> {
            TantrosStats.displayResource(t, this.output, this.craftTime);
        });

        stats.add(Stat.input, this::displayOverheat);
    }

    public void displayOverheat(Table t){
        if(this.overheat > 0) {
            t.table((t_extra) -> {
                t_extra.table((t_overheat) -> {
                    t_overheat.add(Core.bundle.get("recipe.generic.overheat.title")).left();
                    t_overheat.row();
                    StatValues.number(this.overheat, StatUnit.heatUnits, true).display(t_overheat);
                    t_overheat.add(
                            Core.bundle.get("recipe.generic.efficiency-boost.format")
                                    .replace("{0}", StatValues.fixValue(this.overheatScale * 100))
                                    .replace("{1}", StatValues.fixValue(this.maxEfficiency * 100))
                    ).left();
                }).left();
            }).left();
        }
    }

    /*@Override
    public void loadIcon(){
        super.loadIcon();

        //animation code ""borrowed"" from Project Unity - original implementation by GlennFolker and sk7725
        if(frames > 0){
            TextureRegion[] regions = new TextureRegion[frames * (transitionFrames + 1)];

            if(transitionFrames <= 0){
                for(int i = 1; i <= frames; i++){
                    regions[i - 1] = Core.atlas.find(name + i);
                }
            }else{
                for(int i = 0; i < frames; i++){
                    regions[i * (transitionFrames + 1)] = Core.atlas.find(name + (i + 1));
                    for(int j = 1; j <= transitionFrames; j++){
                        int index = i * (transitionFrames + 1) + j;
                        regions[index] = Core.atlas.find(name + "-t" + index);
                    }
                }
            }

            fullIcon = new TextureRegion(fullIcon);
            uiIcon = new TextureRegion(uiIcon);

            Events.run(EventType.Trigger.update, () -> {
                int frame = (int)(Time.globalTime / frameTime) % regions.length;

                fullIcon.set(regions[frame]);
                uiIcon.set(regions[frame]);
            });
        }
    }

    @Override
    public String toString(){
        return this.localizedName;
    }
*/
    /*
    @Override
    public void createIcons(MultiPacker packer){
        super.createIcons(packer);

        //create transitions
        if(frames > 0 && transitionFrames > 0){
            var pixmaps = new PixmapRegion[frames];

            for(int i = 0; i < frames; i++){
                pixmaps[i] = Core.atlas.getPixmap(name + (i + 1));
            }

            for(int i = 0; i < frames; i++){
                for(int j = 1; j <= transitionFrames; j++){
                    float f = (float)j / (transitionFrames + 1);
                    int index = i * (transitionFrames + 1) + j;

                    Pixmap res = Pixmaps.blend(pixmaps[i], pixmaps[(i + 1) % frames], f);
                    packer.add(MultiPacker.PageType.main, name + "-t" + index, res);
                    res.dispose();
                }
            }
        }
    }
    */

}

