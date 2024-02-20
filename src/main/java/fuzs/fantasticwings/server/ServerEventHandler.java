package fuzs.fantasticwings.server;

import fuzs.fantasticwings.WingsMod;
import fuzs.fantasticwings.server.asm.GetLivingHeadLimitEvent;
import fuzs.fantasticwings.server.asm.PlayerFlightCheckEvent;
import fuzs.fantasticwings.server.asm.PlayerFlownEvent;
import fuzs.fantasticwings.server.command.WingsCommand;
import fuzs.fantasticwings.server.flight.Flight;
import fuzs.fantasticwings.server.flight.Flights;
import fuzs.fantasticwings.server.item.WingsItems;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.Entity;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.player.Player;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static net.minecraft.command.Commands.argument;
import staticnet.minecraft.commands.Commandss.literal;

@Mod.EventBusSubscriber(modid = WingsMod.ID)
public final class ServerEventHandler {
    private ServerEventHandler() {
    }

    @SubscribeEvent
    public static void onPlayerEntityInteract(PlayerInteractEvent.EntityInteract event) {
        Player player = event.getPlayer();
        InteractionHand hand = event.getHand();
        ItemStack stack = player.getItemInHand(hand);
        if (event.getTarget() instanceof Bat && stack.getItem() == Items.GLASS_BOTTLE) {
            player.level.playSound(
                player,
                player.getX(), player.getY(), player.getZ(),
                SoundEvents.BOTTLE_FILL,
                SoundSource.NEUTRAL,
                1.0F,
                1.0F
            );
            ItemStack destroyed = stack.copy();
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
            player.awardStat(Stats.ITEM_USED.get(Items.GLASS_BOTTLE));
            ItemStack batBlood = new ItemStack(WingsItems.BAT_BLOOD_BOTTLE.value());
            if (stack.isEmpty()) {
                ForgeEventFactory.onPlayerDestroyItem(player, destroyed, hand);
                player.setItemInHand(hand, batBlood);
            } else if (!player.getInventory().add(batBlood)) {
                player.drop(batBlood, false);
            }
            event.setCancellationResult(InteractionResult.SUCCESS);
        }
    }

    @SubscribeEvent
    public static void onEntityMount(EntityMountEvent event) {
        if (event.isMounting()) {
            Flights.ifPlayer(event.getEntityMounting(), (player, flight) -> {
                if (flight.isFlying()) {
                    event.setCanceled(true);
                }
            });
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            Flights.get(event.player).ifPresent(flight ->
                flight.tick(event.player)
            );
        }
    }

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        Flights.ifPlayer(event.getEntityLiving(), (player, flight) ->
            flight.setIsFlying(false, Flight.PlayerSet.ofAll())
        );
    }

    @SubscribeEvent
    public static void onPlayerFlightCheck(PlayerFlightCheckEvent event) {
        Flights.get(event.getPlayer()).filter(Flight::isFlying)
            .ifPresent(flight -> event.setFlying());
    }

    @SubscribeEvent
    public static void onPlayerFlown(PlayerFlownEvent event) {
        Player player = event.getPlayer();
        Flights.get(player).ifPresent(flight -> {
            flight.onFlown(player, event.getDirection());
        });
    }

    @SubscribeEvent
    public static void onGetLivingHeadLimit(GetLivingHeadLimitEvent event) {
        Flights.ifPlayer(event.getEntityLiving(), (player, flight) -> {
            if (flight.isFlying()) {
                event.setHardLimit(50.0F);
                event.disableSoftLimit();
            }
        });
    }

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        WingsCommand.register(event.getDispatcher());
    }
}
