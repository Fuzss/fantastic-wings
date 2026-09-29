package fuzs.fantasticwings.common.init;

import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;

public class ModSoundEvents {
    public static final Holder.Reference<SoundEvent> ITEM_ARMOR_EQUIP_WINGS = ModRegistry.REGISTRIES.registerSoundEvent(
            "item.armor.equip_wings");
    public static final Holder.Reference<SoundEvent> ITEM_WINGS_FLYING = ModRegistry.REGISTRIES.registerSoundEvent(
            "item.wings.flying");

    public static void bootstrap() {
        // NO-OP
    }
}
