package me.paulf.wings.server.asm;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;

public final class WingsHooks {
    private WingsHooks() {
    }

    public static boolean onFlightCheck(LivingEntity living, boolean defaultValue) {
        return living instanceof Player && WingsHooks.onFlightCheck((Player) living, defaultValue);
    }

    public static boolean onFlightCheck(Player player, boolean defaultValue) {
        if (defaultValue) return true;
        PlayerFlightCheckEvent ev = new PlayerFlightCheckEvent(player);
        MinecraftForge.EVENT_BUS.post(ev);
        return ev.isFlying();
    }

    public static float onGetCameraEyeHeight(Entity entity, float eyeHeight) {
        GetCameraEyeHeightEvent ev = GetCameraEyeHeightEvent.create(entity, eyeHeight);
        MinecraftForge.EVENT_BUS.post(ev);
        return ev.getValue();
    }

    public static boolean onUpdateBodyRotation(LivingEntity living, float movementYaw) {
        GetLivingHeadLimitEvent ev = GetLivingHeadLimitEvent.create(living);
        MinecraftForge.EVENT_BUS.post(ev);
        if (ev.isVanilla()) return false;
        living.yBodyRot += Mth.wrapDegrees(movementYaw - living.yBodyRot) * 0.3F;
        float hLimit = ev.getHardLimit();
        float sLimit = ev.getSoftLimit();
        float theta = Mth.clamp(
            Mth.wrapDegrees(living.yRot - living.yBodyRot),
            -hLimit,
            hLimit
        );
        living.yBodyRot = living.yRot - theta;
        if (theta * theta > sLimit * sLimit) {
            living.yBodyRot += theta * 0.2F;
        }
        return true;
    }

    public static void onAddFlown(Player player, double x, double y, double z) {
        MinecraftForge.EVENT_BUS.post(new PlayerFlownEvent(player, new Vec3(x, y, z)));
    }

    public static boolean onReplaceItemSlotCheck(Item item, ItemStack stack) {
        return item instanceof ElytraItem || item.getEquipmentSlot(stack) != null;
    }
}
