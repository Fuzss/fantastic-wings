package fuzs.fantasticwings.common.init;

import fuzs.fantasticwings.common.FantasticWings;
import fuzs.fantasticwings.common.flight.Flight;
import fuzs.fantasticwings.common.flight.apparatus.FlightApparatus;
import fuzs.fantasticwings.common.world.item.BottledWingsItem;
import fuzs.puzzleslib.common.api.attachment.v4.DataAttachmentRegistry;
import fuzs.puzzleslib.common.api.attachment.v4.DataAttachmentType;
import fuzs.puzzleslib.common.api.init.v3.registry.RegistryManager;
import fuzs.puzzleslib.common.api.network.v4.PlayerSet;
import fuzs.puzzleslib.common.api.util.v1.CommonHelper;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityTypeIds;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModRegistry {
    static final RegistryManager REGISTRIES = RegistryManager.from(FantasticWings.MOD_ID);
    public static final Holder.Reference<CreativeModeTab> CREATIVE_MODE_TAB = REGISTRIES.registerCreativeModeTab(() -> {
        RegistryAccess registryAccess = CommonHelper.getRegistryAccess();
        if (registryAccess != null) {
            Holder.Reference<FlightApparatus> holder = registryAccess.lookupOrThrow(FlightApparatus.REGISTRY_KEY)
                    .getOrThrow(FlightApparatuses.MONARCH_BUTTERFLY_FLIGHT_APPARATUS);
            return BottledWingsItem.createItemStack(holder);
        } else {
            return new ItemStack(ModItems.BOTTLED_WINGS_ITEM);
        }
    }, (CreativeModeTab.DisplayItemsGenerator generator) -> {
        return (CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output output) -> {
            output.accept(ModItems.BOTTLED_BAT_BLOOD_ITEM.value());
            parameters.holders()
                    .lookupOrThrow(FlightApparatus.REGISTRY_KEY)
                    .listElements()
                    .map(BottledWingsItem::createItemStack)
                    .forEach(output::accept);
        };
    });

    public static final DataAttachmentType<Entity, Flight> FLIGHT_ATTACHMENT_TYPE = DataAttachmentRegistry.<Flight>entityBuilder()
            .defaultValue(EntityTypeIds.PLAYER, Flight.VOID)
            .persistent(Flight.CODEC)
            .networkSynchronized(Flight.STREAM_CODEC, PlayerSet::nearEntity)
            .build(FantasticWings.id("flight"));

    public static void bootstrap() {
        ModConsumeEffectTypes.bootstrap();
        ModItems.bootstrap();
        ModSoundEvents.bootstrap();
    }
}
