package fuzs.fantasticwings.world.item.consume_effects;

import com.mojang.serialization.MapCodec;
import fuzs.fantasticwings.flight.FlightCapability;
import fuzs.fantasticwings.flight.apparatus.FlightApparatus;
import fuzs.fantasticwings.init.ModRegistry;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.consume_effects.ConsumeEffect;
import net.minecraft.world.level.Level;

public record GrantWingsConsumeEffect(Holder<FlightApparatus> holder) implements ConsumeEffect {
    public static final MapCodec<GrantWingsConsumeEffect> CODEC = FlightApparatus.CODEC.fieldOf("wings")
            .xmap(GrantWingsConsumeEffect::new, GrantWingsConsumeEffect::holder);
    public static final StreamCodec<RegistryFriendlyByteBuf, GrantWingsConsumeEffect> STREAM_CODEC = FlightApparatus.STREAM_CODEC.map(
            GrantWingsConsumeEffect::new,
            GrantWingsConsumeEffect::holder);

    @Override
    public Type<? extends ConsumeEffect> getType() {
        return ModRegistry.GRANT_WINGS_CONSUME_EFFECT_TYPE.value();
    }

    @Override
    public boolean apply(Level level, ItemStack stack, LivingEntity entity) {
        return entity instanceof ServerPlayer serverPlayer && giveWings(serverPlayer, this.holder);
    }

    public static boolean giveWings(ServerPlayer serverPlayer, Holder<FlightApparatus> flightApparatus) {
        FlightCapability flightCapability = ModRegistry.FLIGHT_CAPABILITY.get(serverPlayer);
        if (!flightCapability.is(flightApparatus)) {
            flightCapability.setWings(flightApparatus);
            return true;
        } else {
            return false;
        }
    }
}
