package fuzs.fantasticwings.init;

import fuzs.fantasticwings.FantasticWings;
import fuzs.fantasticwings.flight.Flight;
import fuzs.fantasticwings.flight.apparatus.FlightApparatus;
import fuzs.fantasticwings.world.item.BottledWingsItem;
import fuzs.fantasticwings.world.item.WithDescriptionItem;
import fuzs.fantasticwings.world.item.consume_effects.GrantWingsConsumeEffect;
import fuzs.fantasticwings.world.item.consume_effects.TakeWingsConsumeEffect;
import fuzs.puzzleslib.common.api.attachment.v4.DataAttachmentRegistry;
import fuzs.puzzleslib.common.api.attachment.v4.DataAttachmentType;
import fuzs.puzzleslib.common.api.init.v3.registry.RegistryManager;
import fuzs.puzzleslib.common.api.init.v3.tags.TagFactory;
import fuzs.puzzleslib.common.api.network.v4.PlayerSet;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.consume_effects.ConsumeEffect;

public class ModRegistry {
    public static final RegistrySetBuilder REGISTRY_SET_BUILDER = new RegistrySetBuilder().add(FlightApparatus.REGISTRY_KEY,
            FlightApparatuses::bootstrap);
    static final RegistryManager REGISTRIES = RegistryManager.from(FantasticWings.MOD_ID);
    public static final Holder.Reference<Item> BOTTLED_WINGS_ITEM = REGISTRIES.registerItem("bottled_wings",
            BottledWingsItem::new,
            ModRegistry::bottledItemProperties);
    public static final Holder.Reference<Item> BOTTLED_BAT_BLOOD_ITEM = REGISTRIES.registerItem("bottled_bat_blood",
            WithDescriptionItem::new,
            () -> bottledItemProperties().component(DataComponents.CONSUMABLE,
                    Consumables.defaultDrink().onConsume(new TakeWingsConsumeEffect()).build()));
    public static final Holder.Reference<CreativeModeTab> CREATIVE_MODE_TAB = REGISTRIES.registerCreativeModeTab("main",
            () -> new ItemStack(BOTTLED_BAT_BLOOD_ITEM),
            (CreativeModeTab.ItemDisplayParameters itemDisplayParameters, CreativeModeTab.Output output) -> {
                output.accept(BOTTLED_BAT_BLOOD_ITEM.value());
                itemDisplayParameters.holders()
                        .lookupOrThrow(FlightApparatus.REGISTRY_KEY)
                        .listElements()
                        .map(BottledWingsItem::createItemStack)
                        .forEach(output::accept);
            },
            false);
    public static final Holder.Reference<ConsumeEffect.Type<GrantWingsConsumeEffect>> GRANT_WINGS_CONSUME_EFFECT_TYPE = REGISTRIES.register(
            Registries.CONSUME_EFFECT_TYPE,
            "grant_wings",
            () -> new ConsumeEffect.Type<>(GrantWingsConsumeEffect.CODEC, GrantWingsConsumeEffect.STREAM_CODEC));
    public static final Holder.Reference<ConsumeEffect.Type<TakeWingsConsumeEffect>> TAKE_WINGS_CONSUME_EFFECT_TYPE = REGISTRIES.register(
            Registries.CONSUME_EFFECT_TYPE,
            "take_wings",
            () -> new ConsumeEffect.Type<>(TakeWingsConsumeEffect.CODEC, TakeWingsConsumeEffect.STREAM_CODEC));
    public static final Holder.Reference<SoundEvent> ITEM_ARMOR_EQUIP_WINGS = REGISTRIES.registerSoundEvent(
            "item.armor.equip_wings");
    public static final Holder.Reference<SoundEvent> ITEM_WINGS_FLYING = REGISTRIES.registerSoundEvent(
            "item.wings.flying");

    static final TagFactory TAGS = TagFactory.make(FantasticWings.MOD_ID);
    public static final TagKey<Item> WING_OBSTRUCTIONS = TAGS.registerItemTag("wing_obstructions");

    public static final DataAttachmentType<Entity, Flight> FLIGHT_ATTACHMENT_TYPE = DataAttachmentRegistry.<Flight>entityBuilder()
            .defaultValue(EntityType.PLAYER, Flight.VOID)
            .persistent(Flight.CODEC)
            .networkSynchronized(Flight.STREAM_CODEC, PlayerSet::nearEntity)
            .build(FantasticWings.id("flight"));

    public static void bootstrap() {
        // NO-OP
    }

    private static Item.Properties bottledItemProperties() {
        return new Item.Properties().stacksTo(1).craftRemainder(Items.GLASS_BOTTLE).usingConvertsTo(Items.GLASS_BOTTLE);
    }
}
