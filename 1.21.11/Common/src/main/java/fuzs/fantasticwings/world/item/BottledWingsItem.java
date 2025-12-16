package fuzs.fantasticwings.world.item;

import fuzs.fantasticwings.flight.apparatus.FlightApparatus;
import fuzs.fantasticwings.init.ModRegistry;
import fuzs.fantasticwings.world.item.consume_effects.GrantWingsConsumeEffect;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.consume_effects.ConsumeEffect;

import java.util.Optional;

public class BottledWingsItem extends WithDescriptionItem {

    public BottledWingsItem(Properties properties) {
        super(properties);
    }

    public static ItemStack createItemStack(Holder.Reference<FlightApparatus> holder) {
        ItemStack itemStack = new ItemStack(ModRegistry.BOTTLED_WINGS_ITEM);
        itemStack.set(DataComponents.CONSUMABLE, createComponent(holder));
        return itemStack;
    }

    public static Consumable createComponent(Holder.Reference<FlightApparatus> holder) {
        return Consumables.defaultDrink().onConsume(new GrantWingsConsumeEffect(holder)).build();
    }

    @Override
    public Component getName(ItemStack itemStack) {
        return getFlightApparatus(itemStack).flatMap(Holder::unwrapKey)
                .map(this::getWingsComponent)
                .orElseGet(() -> super.getName(itemStack));
    }

    public Component getWingsComponent(ResourceKey<FlightApparatus> resourceKey) {
        return Component.translatable(this.getDescriptionId() + ".wings." + resourceKey.identifier().toLanguageKey());
    }

    public static Optional<Holder<FlightApparatus>> getFlightApparatus(ItemStack itemStack) {
        Consumable consumable = itemStack.get(DataComponents.CONSUMABLE);
        if (consumable != null) {
            for (ConsumeEffect consumeEffect : consumable.onConsumeEffects()) {
                if (consumeEffect instanceof GrantWingsConsumeEffect(
                        Holder<FlightApparatus> holder
                )) {
                    return Optional.of(holder);
                }
            }
        }

        return Optional.empty();
    }
}
