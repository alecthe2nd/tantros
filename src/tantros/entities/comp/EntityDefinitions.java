package tantros.entities.comp;

import ent.anno.Annotations.*;
import mindustry.gen.*;
import tantros.gen.*;

public class EntityDefinitions<E> {

    @EntityDef({Unitc.class, Burrowerc.class}) E myUnit;
}
