package fuzs.fantasticwings.init;

import fuzs.fantasticwings.FantasticWings;
import fuzs.fantasticwings.flight.FlightCapability;
import fuzs.fantasticwings.flight.apparatus.FlightApparatus;
import fuzs.fantasticwings.world.item.BottledWingsItem;
import fuzs.fantasticwings.world.item.WithDescriptionItem;
import fuzs.fantasticwings.world.item.consume_effects.GrantWingsConsumeEffect;
import fuzs.fantasticwings.world.item.consume_effects.TakeWingsConsumeEffect;
import fuzs.puzzleslib.api.capability.v3.CapabilityController;
import fuzs.puzzleslib.api.capability.v3.data.EntityCapabilityKey;
import fuzs.puzzleslib.api.capability.v3.data.SyncStrategy;
import fuzs.puzzleslib.api.init.v3.registry.RegistryManager;
import fuzs.puzzleslib.api.init.v3.tags.TagFactory;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.consume_effects.ConsumeEffect;

public class ModRegistry {
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
                HolderLookup.RegistryLookup<FlightApparatus> wingsLookup = itemDisplayParameters.holders()
                        .lookupOrThrow(FlightApparatus.REGISTRY_KEY);
                wingsLookup.listElements().forEach((Holder.Reference<FlightApparatus> holder) -> {
                    ItemStack itemStack = new ItemStack(BOTTLED_WINGS_ITEM);
                    itemStack.set(DataComponents.ITEM_MODEL, holder.key().location());
                    itemStack.set(DataComponents.CONSUMABLE,
                            Consumables.defaultDrink().onConsume(new GrantWingsConsumeEffect(holder)).build());
                    output.accept(itemStack);
                });
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

    public static final ResourceKey<FlightApparatus> ANGEL_FLIGHT_APPARATUS = REGISTRIES.makeResourceKey(FlightApparatus.REGISTRY_KEY,
            "angel");
    public static final ResourceKey<FlightApparatus> PARROT_FLIGHT_APPARATUS = REGISTRIES.makeResourceKey(
            FlightApparatus.REGISTRY_KEY,
            "parrot");
    public static final ResourceKey<FlightApparatus> SLIME_FLIGHT_APPARATUS = REGISTRIES.makeResourceKey(FlightApparatus.REGISTRY_KEY,
            "slime");
    public static final ResourceKey<FlightApparatus> BLUE_BUTTERFLY_FLIGHT_APPARATUS = REGISTRIES.makeResourceKey(
            FlightApparatus.REGISTRY_KEY,
            "blue_butterfly");
    public static final ResourceKey<FlightApparatus> MONARCH_BUTTERFLY_FLIGHT_APPARATUS = REGISTRIES.makeResourceKey(
            FlightApparatus.REGISTRY_KEY,
            "monarch_butterfly");
    public static final ResourceKey<FlightApparatus> FIRE_FLIGHT_APPARATUS = REGISTRIES.makeResourceKey(FlightApparatus.REGISTRY_KEY,
            "fire");
    public static final ResourceKey<FlightApparatus> BAT_FLIGHT_APPARATUS = REGISTRIES.makeResourceKey(FlightApparatus.REGISTRY_KEY,
            "bat");
    public static final ResourceKey<FlightApparatus> FAIRY_FLIGHT_APPARATUS = REGISTRIES.makeResourceKey(FlightApparatus.REGISTRY_KEY,
            "fairy");
    public static final ResourceKey<FlightApparatus> EVIL_FLIGHT_APPARATUS = REGISTRIES.makeResourceKey(FlightApparatus.REGISTRY_KEY,
            "evil");
    public static final ResourceKey<FlightApparatus> DRAGON_FLIGHT_APPARATUS = REGISTRIES.makeResourceKey(
            FlightApparatus.REGISTRY_KEY,
            "dragon");
    public static final ResourceKey<FlightApparatus> METALLIC_FLIGHT_APPARATUS = REGISTRIES.makeResourceKey(
            FlightApparatus.REGISTRY_KEY,
            "metallic");

    static final TagFactory TAGS = TagFactory.make(FantasticWings.MOD_ID);
    public static final TagKey<Item> WING_OBSTRUCTIONS = TAGS.registerItemTag("wing_obstructions");

    static final CapabilityController CAPABILITIES = CapabilityController.from(FantasticWings.MOD_ID);
    public static final EntityCapabilityKey<Player, FlightCapability> FLIGHT_CAPABILITY = CAPABILITIES.registerEntityCapability(
            "flight",
            FlightCapability.class,
            FlightCapability::new,
            Player.class).setSyncStrategy(SyncStrategy.TRACKING);

    public static void bootstrap() {
        // NO-OP
    }

    static Item.Properties bottledItemProperties() {
        return new Item.Properties().stacksTo(1)
                .craftRemainder(Items.GLASS_BOTTLE)
                .usingConvertsTo(Items.GLASS_BOTTLE)
                .useCooldown(5.0F)
                .component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, Boolean.TRUE);
    }
}
