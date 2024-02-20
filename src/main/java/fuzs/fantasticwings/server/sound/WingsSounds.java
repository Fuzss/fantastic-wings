package fuzs.fantasticwings.server.sound;

import fuzs.fantasticwings.WingsMod;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = WingsMod.ID)
public final class WingsSounds {
    private WingsSounds() {
    }

    public static final DeferredRegister<SoundEvent> REG = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, WingsMod.ID);

    public static final Holder.Reference<SoundEvent> ITEM_ARMOR_EQUIP_WINGS = create("item.armor.equip_wings");

    public static final Holder.Reference<SoundEvent> ITEM_WINGS_FLYING = create("item.wings.flying");

    private static Holder.Reference<SoundEvent> create(String name) {
        return REG.register(name, () -> new SoundEvent(new ResourceLocation(WingsMod.ID, name)));
    }
}
