package fuzs.fantasticwings.common.init;

import fuzs.fantasticwings.common.FantasticWings;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;

public class ModTags {
    public static class Items {
        public static final TagKey<Item> WING_OBSTRUCTIONS_ITEM_TAG = register("wing_obstructions");

        private static TagKey<Item> register(String name) {
            return TagKey.create(Registries.ITEM, FantasticWings.id(name));
        }
    }

    public static class EntityTypes {
        public static final TagKey<EntityType<?>> BAT_BLOOD_TARGETS_ENTITY_TYPE_TAG = register("bat_blood_targets");

        private static TagKey<EntityType<?>> register(String name) {
            return TagKey.create(Registries.ENTITY_TYPE, FantasticWings.id(name));
        }
    }
}
