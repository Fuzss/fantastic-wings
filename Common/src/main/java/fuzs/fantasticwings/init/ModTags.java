package fuzs.fantasticwings.init;

import fuzs.fantasticwings.FantasticWings;
import fuzs.puzzleslib.api.init.v3.tags.BoundTagFactory;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ModTags {
    static final BoundTagFactory TAGS = BoundTagFactory.make(FantasticWings.MOD_ID);
    public static final TagKey<Item> WING_OBSTRUCTIONS = TAGS.registerItemTag("wing_obstructions");

    public static void touch() {
        // NO-OP
    }
}
