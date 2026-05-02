package fuzs.fantasticwings.common.client.renderer.item.properties.select;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import fuzs.fantasticwings.common.flight.apparatus.FlightApparatus;
import fuzs.fantasticwings.common.world.item.BottledWingsItem;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperty;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public record FlightApparatusProperty() implements SelectItemModelProperty<ResourceKey<FlightApparatus>> {
    public static final SelectItemModelProperty.Type<FlightApparatusProperty, ResourceKey<FlightApparatus>> TYPE = SelectItemModelProperty.Type.create(
            MapCodec.unit(new FlightApparatusProperty()),
            ResourceKey.codec(FlightApparatus.REGISTRY_KEY));

    @Override
    public @Nullable ResourceKey<FlightApparatus> get(ItemStack itemStack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed, ItemDisplayContext displayContext) {
        return BottledWingsItem.getFlightApparatus(itemStack).flatMap(Holder::unwrapKey).orElse(null);
    }

    @Override
    public Codec<ResourceKey<FlightApparatus>> valueCodec() {
        return ResourceKey.codec(FlightApparatus.REGISTRY_KEY);
    }

    @Override
    public Type<? extends SelectItemModelProperty<ResourceKey<FlightApparatus>>, ResourceKey<FlightApparatus>> type() {
        return TYPE;
    }
}
