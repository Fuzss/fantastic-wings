package me.paulf.wings.util;

import com.google.common.collect.ImmutableList;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;

public interface ItemPlacing<T extends ICapabilityProvider> {
    void enumerate(T provider, ImmutableList.Builder<HandlerSlot> handlers);

    static <T extends LivingEntity> ItemPlacing<T> forArmor(EquipmentSlot slot) {
        return (provider, handlers) -> provider.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.EAST)
            .ifPresent(handler -> handlers.add(HandlerSlot.create(handler, slot.getIndex())));
    }
}
