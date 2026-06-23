package tantros.content.blocks;

import mindustry.content.Items;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.world.Block;
import mindustry.world.blocks.defense.Wall;
import mindustry.world.meta.Env;
import tantros.world.blocks.defense.DoorExtended;
import tantros.world.blocks.defense.WallExtended;
import tantros.world.blocks.defense.modifiers.DamageModifer;
import tantros.world.meta.DamageType;
import tantros.world.meta.DamageTypes;

import static mindustry.type.ItemStack.with;

public class TantrosDefense {

    public static int wallHealthMultiplier = 4;

    public static Block

            //defense
            copperBulkhead, largeCopperBulkhead,
            leadBulkhead, largeLeadBulkhead,
            metaglassBulkhead, largeMetaglassBulkhead,
            largeMetaglassBulkheadDoor
            ;

    public static void load(){

        copperBulkhead = new WallExtended("copper-bulkhead"){{
            requirements(Category.defense, with(Items.copper, 6));
            health = 80 * wallHealthMultiplier;
            damageModifiers.add(new DamageModifer(DamageTypes.energy, 1.5f));
            researchCostMultiplier = 0.1f;
            envEnabled |= Env.underwater;
        }};

        largeCopperBulkhead = new WallExtended("copper-bulkhead-large"){{
            requirements(Category.defense, ItemStack.mult(copperBulkhead.requirements, 4));
            health = copperBulkhead.health * 4;
            damageModifiers.add(new DamageModifer(DamageTypes.energy, 1.5f));
            size = 2;
            envEnabled |= Env.underwater;
        }};

        leadBulkhead = new WallExtended("lead-bulkhead"){{
            requirements(Category.defense, with(Items.lead, 8));
            health = 90 * wallHealthMultiplier;
            buildCostMultiplier *= 2f;
            envEnabled |= Env.underwater;
        }};

        largeLeadBulkhead = new WallExtended("lead-bulkhead-large"){{
            requirements(Category.defense, ItemStack.mult(leadBulkhead.requirements, 4));
            health = leadBulkhead.health * 4;
            buildCostMultiplier = leadBulkhead.buildCostMultiplier;
            size = 2;
            envEnabled |= Env.underwater;
        }};

        metaglassBulkhead = new WallExtended("metaglass-bulkhead"){{
            requirements(Category.defense, with(Items.metaglass, 8, Items.oxide, 4));
            damageModifiers.add(new DamageModifer(DamageTypes.energy, 0.25f));
            health = 100 * wallHealthMultiplier;
            researchCostMultiplier = 0.1f;
            insulated = true;
            absorbLasers = true;
        }};

        largeMetaglassBulkhead = new WallExtended("metaglass-bulkhead-large"){{
            requirements(Category.defense, ItemStack.mult(metaglassBulkhead.requirements, 4));
            damageModifiers.add(new DamageModifer(DamageTypes.energy, 0.25f));
            health = metaglassBulkhead.health * 4;
            size = 2;
            researchCostMultiplier = 0.1f;
            insulated = true;
            absorbLasers = true;
        }};

        largeMetaglassBulkheadDoor = new DoorExtended("metaglass-bulkhead-large-door"){{
            requirements(Category.defense, ItemStack.mult(metaglassBulkhead.requirements, 4));
            damageModifiers.add(new DamageModifer(DamageTypes.energy, 0.25f));
            health = metaglassBulkhead.health * 4 *9/10;
            size = 2;
            researchCostMultiplier = 0.1f;
            insulated = true;
            absorbLasers = true;
        }};
    }
}
