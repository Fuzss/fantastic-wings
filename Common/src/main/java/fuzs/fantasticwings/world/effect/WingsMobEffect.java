package fuzs.fantasticwings.world.effect;

import fuzs.fantasticwings.init.ModCapabilities;
import fuzs.fantasticwings.init.ModSoundEvents;
import fuzs.fantasticwings.flight.FlightCapability;
import fuzs.fantasticwings.flight.apparatus.FlightApparatus;
import fuzs.fantasticwings.flight.apparatus.FlightApparatusImpl;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.InstantenousMobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class WingsMobEffect extends InstantenousMobEffect {

    public WingsMobEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void applyEffectTick(LivingEntity livingEntity, int amplifier) {
        super.applyEffectTick(livingEntity, amplifier);
        if (!livingEntity.level().isClientSide && livingEntity instanceof ServerPlayer serverPlayer) {
            if (this.isBeneficial() ?
                    giveWings(serverPlayer, amplifier) : takeWings(serverPlayer)) {
                serverPlayer.level()
                        .playSound(null,
                                serverPlayer.getX(),
                                serverPlayer.getY(),
                                serverPlayer.getZ(),
                                ModSoundEvents.ITEM_ARMOR_EQUIP_WINGS.value(),
                                SoundSource.PLAYERS,
                                1.0F,
                                this.isBeneficial() ? 1.0F : 0.8F
                        );
            }
        }
    }

    public static boolean giveWings(ServerPlayer serverPlayer, int wingsTypeId) {
        return giveWings(serverPlayer, FlightApparatusImpl.byId(wingsTypeId).holder());
    }

    public static boolean giveWings(ServerPlayer serverPlayer, FlightApparatus.Holder flightApparatus) {
        FlightCapability flightCapability = ModCapabilities.FLIGHT_CAPABILITY.get(serverPlayer);
        if (!flightCapability.getWings().is(flightApparatus)) {
            flightCapability.setWings(flightApparatus);
            return true;
        } else {
            return false;
        }
    }

    public static boolean takeWings(ServerPlayer serverPlayer) {
        return takeWings(serverPlayer, FlightApparatus.Holder.empty());
    }

    public static boolean takeWings(ServerPlayer serverPlayer, FlightApparatus.Holder flightApparatus) {
        FlightCapability flightCapability = ModCapabilities.FLIGHT_CAPABILITY.get(serverPlayer);
        if (flightApparatus.isEmpty() || flightCapability.getWings().is(flightApparatus)) {
            flightCapability.setWings(flightApparatus);
            return true;
        } else {
            return false;
        }
    }
}
