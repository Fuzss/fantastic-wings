package fuzs.fantasticwings.server.asm;

import fuzs.fantasticwings.capability.client.FlightViewCapability;
import fuzs.fantasticwings.client.init.ModClientCapabilities;
import fuzs.fantasticwings.init.ModCapabilities;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;

public class WingsHooks {

    public static boolean onFlightCheck(LivingEntity living, boolean defaultValue) {
        return living instanceof Player && WingsHooks.onFlightCheck((Player) living, defaultValue);
    }

    public static boolean onFlightCheck(Player player, boolean defaultValue) {
        if (!defaultValue) {
            return ModCapabilities.FLIGHT_CAPABILITY.get(player).isFlying();
        } else {
            return true;
        }
    }

    public static float onGetCameraEyeHeight(Entity entity, float eyeHeight) {
        if (entity instanceof LocalPlayer localPlayer) {
            FlightViewCapability flightViewCapability = ModClientCapabilities.FLIGHT_VIEW_CAPABILITY.get(localPlayer);
            return flightViewCapability.tickEyeHeight(eyeHeight);
        } else {
            return eyeHeight;
        }
    }

    public static boolean onUpdateBodyRotation(LivingEntity living, float movementYaw) {
        GetLivingHeadLimitEvent ev = GetLivingHeadLimitEvent.create(living);
        MinecraftForge.EVENT_BUS.post(ev);
        if (!ev.isVanilla()) {
            living.yBodyRot += Mth.wrapDegrees(movementYaw - living.yBodyRot) * 0.3F;
            float hLimit = ev.getHardLimit();
            float sLimit = ev.getSoftLimit();
            float theta = Mth.clamp(Mth.wrapDegrees(living.getYRot() - living.yBodyRot), -hLimit, hLimit);
            living.yBodyRot = living.getYRot() - theta;
            if (theta * theta > sLimit * sLimit) {
                living.yBodyRot += theta * 0.2F;
            }
            return true;
        } else {
            return false;
        }
    }

    public static void onAddFlown(Player player, double x, double y, double z) {
        ModCapabilities.FLIGHT_CAPABILITY.get(player).onFlown(new Vec3(x, y, z));
    }

    public static boolean onReplaceItemSlotCheck(Item item, ItemStack stack) {
        return item instanceof ElytraItem || item.getEquipmentSlot(stack) != null;
    }
}
