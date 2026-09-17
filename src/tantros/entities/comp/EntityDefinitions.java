package tantros.entities.comp;

import ent.anno.Annotations.*;
import mindustry.gen.*;
import tantros.gen.*;

public class EntityDefinitions<E> {

    @EntityDef(value = {Unitc.class, Burrowerc.class}, serialize = false) E myUnit;


}
