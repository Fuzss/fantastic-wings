package fuzs.fantasticwings.world.effect;

import fuzs.fantasticwings.flight.FlightCapability;
import fuzs.fantasticwings.flight.apparatus.FlightApparatus;
import fuzs.fantasticwings.flight.apparatus.FlightApparatusImpl;
import fuzs.fantasticwings.init.ModRegistry;
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
    public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
        if (!livingEntity.level().isClientSide && livingEntity instanceof ServerPlayer serverPlayer) {
            if (this.isBeneficial() ?
                    giveWings(serverPlayer, amplifier) : takeWings(serverPlayer)) {
                serverPlayer.level()
                        .playSound(null,
                                serverPlayer.getX(),
                                serverPlayer.getY(),
                                serverPlayer.getZ(),
                                ModRegistry.ITEM_ARMOR_EQUIP_WINGS.value(),
                                SoundSource.PLAYERS,
                                1.0F,
                                this.isBeneficial() ? 1.0F : 0.8F
                        );
            }
        }

        return super.applyEffectTick(livingEntity, amplifier);
    }

    public static boolean giveWings(ServerPlayer serverPlayer, int wingsTypeId) {
        return giveWings(serverPlayer, FlightApparatusImpl.byId(wingsTypeId).holder());
    }

    public static boolean giveWings(ServerPlayer serverPlayer, FlightApparatus.FlightApparatusHolder flightApparatus) {
        FlightCapability flightCapability = ModRegistry.FLIGHT_CAPABILITY.get(serverPlayer);
        if (!flightCapability.getWings().is(flightApparatus)) {
            flightCapability.setWings(flightApparatus);
            return true;
        } else {
            return false;
        }
    }

    public static boolean takeWings(ServerPlayer serverPlayer) {
        return takeWings(serverPlayer, FlightApparatus.FlightApparatusHolder.empty());
    }

    public static boolean takeWings(ServerPlayer serverPlayer, FlightApparatus.FlightApparatusHolder flightApparatus) {
        FlightCapability flightCapability = ModRegistry.FLIGHT_CAPABILITY.get(serverPlayer);
        if (!flightCapability.getWings().isEmpty() && (flightApparatus.isEmpty() || flightCapability.getWings().is(flightApparatus))) {
            flightCapability.setWings(FlightApparatus.FlightApparatusHolder.empty());
            return true;
        } else {
            return false;
        }
    }
}
