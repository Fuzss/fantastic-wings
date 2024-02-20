package me.paulf.wings;

import me.paulf.wings.server.dreamcatcher.InSomniable;
import me.paulf.wings.server.flight.Flight;
import me.paulf.wings.server.flight.FlightDefault;
import me.paulf.wings.server.item.WingsItems;
import me.paulf.wings.server.net.Network;
import me.paulf.wings.server.net.clientbound.MessageSyncFlight;
import me.paulf.wings.server.potion.PotionMix;
import me.paulf.wings.util.SimpleStorage;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.function.BiConsumer;

public abstract class Proxy {
    protected final Network network = new Network();

    public void init(IEventBus modBus) {
        modBus.addListener(this::setup);
    }

    protected void setup(FMLCommonSetupEvent event) {
        CapabilityManager.INSTANCE.register(Flight.class, SimpleStorage.ofVoid(), FlightDefault::new);
        CapabilityManager.INSTANCE.register(InSomniable.class, SimpleStorage.ofVoid(), InSomniable::new);
        event.enqueueWork(() -> {
            BiConsumer<ItemLike, RegistryObject<Item>> reg = (item, obj) -> {
                BrewingRecipeRegistry.addRecipe(
                    new PotionMix(Potions.SLOW_FALLING, Ingredient.of(item), new ItemStack(obj.get()))
                );
                BrewingRecipeRegistry.addRecipe(
                    new PotionMix(Potions.LONG_SLOW_FALLING, Ingredient.of(item), new ItemStack(obj.get()))
                );
            };
            reg.accept(Items.FEATHER, WingsItems.ANGEL_WINGS_BOTTLE);
            reg.accept(Items.RED_DYE, WingsItems.PARROT_WINGS_BOTTLE);
            reg.accept(WingsItems.BAT_BLOOD_BOTTLE.get(), WingsItems.BAT_WINGS_BOTTLE);
            reg.accept(Items.BLUE_DYE, WingsItems.BLUE_BUTTERFLY_WINGS_BOTTLE);
            reg.accept(Items.LEATHER, WingsItems.DRAGON_WINGS_BOTTLE);
            reg.accept(Items.BONE, WingsItems.EVIL_WINGS_BOTTLE);
            reg.accept(Items.OXEYE_DAISY, WingsItems.FAIRY_WINGS_BOTTLE);
            reg.accept(Items.BLAZE_POWDER, WingsItems.FIRE_WINGS_BOTTLE);
            reg.accept(Items.ORANGE_DYE, WingsItems.MONARCH_BUTTERFLY_WINGS_BOTTLE);
            reg.accept(Items.SLIME_BALL, WingsItems.SLIME_WINGS_BOTTLE);
        });
    }

    public void addFlightListeners(Player player, Flight instance) {
        if (player instanceof ServerPlayer) {
            instance.registerFlyingListener(isFlying -> player.abilities.mayfly = isFlying);
            instance.registerFlyingListener(isFlying -> {
                if (isFlying) {
                    player.removeVehicle();
                }
            });
            Flight.Notifier notifier = Flight.Notifier.of(
                () -> this.network.sendToPlayer(new MessageSyncFlight(player, instance), (ServerPlayer) player),
                p -> this.network.sendToPlayer(new MessageSyncFlight(player, instance), p),
                () -> this.network.sendToAllTracking(new MessageSyncFlight(player, instance), player)
            );
            instance.registerSyncListener(players -> players.notify(notifier));
        }
    }
}
