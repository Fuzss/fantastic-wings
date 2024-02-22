package fuzs.fantasticwings.init;

import fuzs.fantasticwings.FantasticWings;
import fuzs.puzzleslib.api.init.v3.registry.RegistryManager;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;

public class ModSoundEvents {
    static final RegistryManager REGISTRY = RegistryManager.from(FantasticWings.MOD_ID);
    public static final Holder.Reference<SoundEvent> ITEM_ARMOR_EQUIP_WINGS = REGISTRY.registerSoundEvent(
            "item.armor.equip_wings");
    public static final Holder.Reference<SoundEvent> ITEM_WINGS_FLYING = REGISTRY.registerSoundEvent("item.wings.flying");

    public static void touch() {
        // NO-OP
    }
}
