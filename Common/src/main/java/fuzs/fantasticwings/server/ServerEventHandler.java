package fuzs.fantasticwings.server;

import com.mojang.brigadier.CommandDispatcher;
import fuzs.fantasticwings.init.ModCapabilities;
import fuzs.fantasticwings.init.ModMobEffects;
import fuzs.fantasticwings.server.asm.GetLivingHeadLimitEvent;
import fuzs.fantasticwings.server.command.WingsCommand;
import fuzs.fantasticwings.server.flight.FlightCapability;
import fuzs.fantasticwings.server.flight.Flights;
import fuzs.puzzleslib.api.core.v1.CommonAbstractions;
import fuzs.puzzleslib.api.event.v1.core.EventResult;
import fuzs.puzzleslib.api.event.v1.data.MutableInt;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;

public class ServerEventHandler {

    public static EventResult onAttackEntity(Player player, Level level, InteractionHand interactionHand, Entity entity) {
        ItemStack itemInHand = player.getItemInHand(interactionHand);
        if (entity.getType() == EntityType.BAT && itemInHand.is(Items.GLASS_BOTTLE)) {
            level.playSound(
                player,
                player.getX(), player.getY(), player.getZ(),
                SoundEvents.BOTTLE_FILL,
                SoundSource.NEUTRAL,
                1.0F,
                1.0F
            );
            ItemStack itemStack = itemInHand.copy();
            if (!player.getAbilities().instabuild) {
                itemInHand.shrink(1);
            }
            player.awardStat(Stats.ITEM_USED.get(Items.GLASS_BOTTLE));
            ItemStack batBlood = PotionUtils.setPotion(Items.POTION.getDefaultInstance(),
                    ModMobEffects.BAT_BLOOD_POTION.value()
            );
            if (itemInHand.isEmpty()) {
                CommonAbstractions.INSTANCE.onPlayerDestroyItem(player, itemStack, interactionHand);
                player.setItemInHand(interactionHand, batBlood);
            } else if (!player.getInventory().add(batBlood)) {
                player.drop(batBlood, false);
            }
        }
        return EventResult.PASS;
    }

    public static EventResult onStartRiding(Level level, Entity rider, Entity vehicle) {
        return ModCapabilities.FLIGHT_CAPABILITY.getIfProvided(rider).filter(FlightCapability::isFlying).isPresent() ? EventResult.INTERRUPT : EventResult.PASS;
    }

    public static void onEndPlayerTick(Player player) {
        ModCapabilities.FLIGHT_CAPABILITY.get(player).tick();
    }

    public static void onGetLivingHeadLimit(GetLivingHeadLimitEvent event) {
        Flights.ifPlayer(event.getEntityLiving(), (player, flight) -> {
            if (flight.isFlying()) {
                event.setHardLimit(50.0F);
                event.disableSoftLimit();
            }
        });
    }

    public static void onRegisterCommands(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context, Commands.CommandSelection environment) {
        WingsCommand.register(dispatcher);
    }

    public static EventResult onUseItemStart(LivingEntity entity, ItemStack stack, MutableInt remainingUseDuration) {
        MobEffect mobEffect = PotionUtils.getPotion(stack).getEffects().get(0).getEffect();
        if (mobEffect == ModMobEffects.GROW_WINGS_MOB_EFFECT.value() || mobEffect == ModMobEffects.SHED_WINGS_MOB_EFFECT.value()) {
            remainingUseDuration.accept(40);
        }
        return EventResult.PASS;
    }
}
