package fuzs.fantasticwings.flight;

import fuzs.fantasticwings.FantasticWings;
import fuzs.fantasticwings.flight.apparatus.FlightApparatus;
import fuzs.fantasticwings.init.ModRegistry;
import fuzs.fantasticwings.network.ServerboundControlFlyingMessage;
import fuzs.fantasticwings.util.CubicBezier;
import fuzs.fantasticwings.util.MathHelper;
import fuzs.puzzleslib.api.capability.v3.data.CapabilityComponent;
import fuzs.puzzleslib.api.network.v4.MessageSender;
import fuzs.puzzleslib.api.network.v4.PlayerSet;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

public final class FlightCapability extends CapabilityComponent<Player> {
    private static final String KEY_IS_FLYING = FantasticWings.id("is_flying").toString();
    private static final String KEY_TIME_FLYING = FantasticWings.id("time_flying").toString();
    private static final String KEY_WING_TYPE = FantasticWings.id("wing_type").toString();
    private static final CubicBezier FLY_AMOUNT_CURVE = new CubicBezier(0.37F, 0.13F, 0.3F, 1.12F);
    private static final int INITIAL_TIME_FLYING = 0;
    private static final int MAX_TIME_FLYING = 20;
    private static final float MIN_SPEED = 0.03F;
    private static final float MAX_SPEED = 0.0715F;
    private static final float Y_BOOST = 0.05F;
    private static final float MANUAL_Y_BOOST = 0.06F;
    private static final float FALL_REDUCTION = 0.9F;
    private static final float PITCH_OFFSET = 30.0F;

    private int prevTimeFlying = INITIAL_TIME_FLYING;
    private int timeFlying = INITIAL_TIME_FLYING;
    private boolean isFlying;
    private Optional<Holder<FlightApparatus>> holder = Optional.empty();

    public void setIsFlying(Player player, boolean isFlying) {
        this.setIsFlying(player, isFlying, null);
    }

    public void setIsFlying(Player player, boolean isFlying, @Nullable PlayerSet playerSet) {
        if (this.isFlying != isFlying) {
            this.isFlying = isFlying;
            if (this.isFlying) {
                player.unRide();
            }
            this.setChanged(playerSet);
        }
    }

    public boolean isFlying() {
        return this.isFlying;
    }

    public void toggleIsFlying(Player player, PlayerSet playerSet) {
        this.setIsFlying(player, !this.isFlying, playerSet);
    }

    public void setTimeFlying(int timeFlying) {
        if (this.timeFlying != timeFlying) {
            this.timeFlying = timeFlying;
            this.setChanged(PlayerSet.ofNone());
        }
    }

    public boolean setWings(@Nullable Holder<FlightApparatus> flightApparatus) {
        if (flightApparatus == null && this.holder.isPresent() ||
                flightApparatus != null && !this.is(flightApparatus)) {
            this.holder = Optional.ofNullable(flightApparatus);
            this.setChanged();
            return true;
        } else {
            return false;
        }
    }

    public Optional<Holder<FlightApparatus>> getWings() {
        return this.holder;
    }

    public boolean isEmpty() {
        return this.holder.isEmpty();
    }

    public boolean is(Holder<FlightApparatus> flightApparatus) {
        Objects.requireNonNull(flightApparatus, "flight apparatus is null");
        return this.holder.filter((Holder<FlightApparatus> holder) -> holder.is(flightApparatus)).isPresent();
    }

    public float getFlyingAmount(float partialTick) {
        return FLY_AMOUNT_CURVE.eval(
                MathHelper.lerp(this.prevTimeFlying, this.timeFlying, partialTick) / MAX_TIME_FLYING);
    }

    public boolean canUseWings(Player player) {
        return !player.getAbilities().flying &&
                !player.getItemBySlot(EquipmentSlot.CHEST).is(ModRegistry.WING_OBSTRUCTIONS);
    }

    public boolean canFly(Player player) {
        return this.canUseWings(player) &&
                this.holder.filter((Holder<FlightApparatus> holder) -> holder.value().isUsableForFlying(player))
                        .isPresent();
    }

    public boolean canSlowlyDescend(Player player) {
        return this.canUseWings(player) && this.holder.filter((Holder<FlightApparatus> holder) -> holder.value()
                .isUsableForSlowlyDescending(player)).isPresent() &&
                (this.isFlying() || !player.isDescending() && !player.getAbilities().mayfly);
    }

    private void onWornUpdate(Player player) {
        if (!player.level().isClientSide && this.isFlying() && !this.canFly(player)) {
            this.setIsFlying(player, false);
        }
        if (player.isEffectiveAi()) {
            if (this.isFlying()) {
                float speed = Mth.clampedLerp(MIN_SPEED, MAX_SPEED, player.zza);
                float elevationBoost = MathHelper.transform(Math.abs(player.getXRot()), 45.0F, 90.0F, 1.0F, 0.0F);
                float pitch = -MathHelper.toRadians(player.getXRot() - PITCH_OFFSET * elevationBoost);
                float yaw = -MathHelper.toRadians(player.getYRot()) - MathHelper.PI;
                float vxz = -Mth.cos(pitch);
                float vy = Mth.sin(pitch);
                float vz = Mth.cos(yaw);
                float vx = Mth.sin(yaw);
                player.setDeltaMovement(player.getDeltaMovement()
                        .add(vx * vxz * speed,
                                vy * speed + Y_BOOST * (player.getXRot() > 0.0F ? elevationBoost : 1.0D),
                                vz * vxz * speed));
                // similar to swimming where jumping and sneaking help with ascending / descending
                if (player.jumping) {
                    // with the elevation boost this can get quite crazy, so don't add as much as when descending
                    player.setDeltaMovement(player.getDeltaMovement().add(0.0, MANUAL_Y_BOOST / 2.0F, 0.0));
                } else if (player.isDescending()) {
                    player.setDeltaMovement(player.getDeltaMovement().add(0.0, -MANUAL_Y_BOOST, 0.0));
                }
            }
            if (this.canSlowlyDescend(player)) {
                Vec3 deltaMovement = player.getDeltaMovement();
                if (deltaMovement.y() < 0.0D) {
                    player.setDeltaMovement(deltaMovement.multiply(1.0D, FALL_REDUCTION, 1.0D));
                }
                player.fallDistance = 0.0F;
            }
        }
    }

    public void tick(Player player) {
        if (this.holder.isPresent() || !player.isEffectiveAi()) {
            this.onWornUpdate(player);
        } else if (!player.level().isClientSide && this.isFlying()) {
            this.setIsFlying(player, false);
        }
        this.prevTimeFlying = this.timeFlying;
        if (this.isFlying()) {
            if (this.timeFlying < MAX_TIME_FLYING) {
                this.setTimeFlying(this.timeFlying + 1);
            } else if (player.isLocalPlayer() && player.onGround()) {
                this.setIsFlying(player, false, PlayerSet.ofNone());
                MessageSender.broadcast(new ServerboundControlFlyingMessage(false));
            }
        } else if (this.timeFlying > INITIAL_TIME_FLYING) {
            this.setTimeFlying(this.timeFlying - 1);
        }
    }

    public void onFlown(Player player, Vec3 direction) {
        this.holder.ifPresent((Holder<FlightApparatus> holder) -> {
            if (this.isFlying()) {
                holder.value().onFlying(player, direction);
            } else if (this.canSlowlyDescend(player) && player.getDeltaMovement().y() < -0.5) {
                holder.value().onSlowlyDescending(player, direction);
            }
        });
    }

    @Override
    public void write(CompoundTag compoundTag, HolderLookup.Provider registries) {
        compoundTag.putBoolean(KEY_IS_FLYING, this.isFlying);
        compoundTag.putInt(KEY_TIME_FLYING, this.timeFlying);
        compoundTag.storeNullable(KEY_WING_TYPE,
                FlightApparatus.CODEC,
                registries.createSerializationContext(NbtOps.INSTANCE),
                this.holder.orElse(null));
    }

    @Override
    public void read(CompoundTag compoundTag, HolderLookup.Provider registries) {
        this.isFlying = compoundTag.getBooleanOr(KEY_IS_FLYING, false);
        this.timeFlying = compoundTag.getIntOr(KEY_TIME_FLYING, 0);
        this.holder = compoundTag.read(KEY_WING_TYPE,
                FlightApparatus.CODEC,
                registries.createSerializationContext(NbtOps.INSTANCE));
    }
}
