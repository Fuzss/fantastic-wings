package fuzs.fantasticwings.world.item.consume_effects;

import com.mojang.serialization.MapCodec;
import fuzs.fantasticwings.flight.FlightCapability;
import fuzs.fantasticwings.flight.apparatus.FlightApparatus;
import fuzs.fantasticwings.init.ModRegistry;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.consume_effects.ConsumeEffect;
import net.minecraft.world.level.Level;

import java.util.Optional;

public record TakeWingsConsumeEffect(Optional<Holder<FlightApparatus>> holder) implements ConsumeEffect {
    public static final MapCodec<TakeWingsConsumeEffect> CODEC = FlightApparatus.CODEC.optionalFieldOf("wings")
            .xmap(TakeWingsConsumeEffect::new, TakeWingsConsumeEffect::holder);
    public static final StreamCodec<RegistryFriendlyByteBuf, TakeWingsConsumeEffect> STREAM_CODEC = FlightApparatus.STREAM_CODEC.apply(
            ByteBufCodecs::optional).map(TakeWingsConsumeEffect::new, TakeWingsConsumeEffect::holder);

    public TakeWingsConsumeEffect() {
        this(Optional.empty());
    }

    @Override
    public Type<? extends ConsumeEffect> getType() {
        return ModRegistry.TAKE_WINGS_CONSUME_EFFECT_TYPE.value();
    }

    @Override
    public boolean apply(Level level, ItemStack stack, LivingEntity entity) {
        return entity instanceof ServerPlayer serverPlayer && takeWings(serverPlayer, this.holder);
    }

    public static boolean takeWings(ServerPlayer serverPlayer, Optional<Holder<FlightApparatus>> flightApparatus) {
        FlightCapability flightCapability = ModRegistry.FLIGHT_CAPABILITY.get(serverPlayer);
        if ((flightApparatus.isEmpty() || flightCapability.is(flightApparatus.get()))) {
            if (flightCapability.setWings(null)) {
                serverPlayer.serverLevel()
                        .playSound(null,
                                serverPlayer.getX(),
                                serverPlayer.getY(),
                                serverPlayer.getZ(),
                                ModRegistry.ITEM_ARMOR_EQUIP_WINGS.value(),
                                SoundSource.PLAYERS,
                                1.0F,
                                0.8F);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
