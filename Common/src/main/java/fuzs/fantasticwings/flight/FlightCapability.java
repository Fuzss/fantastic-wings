package fuzs.fantasticwings.flight;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fuzs.fantasticwings.flight.apparatus.FlightApparatus;
import fuzs.fantasticwings.init.ModRegistry;
import fuzs.fantasticwings.network.ServerboundControlFlyingMessage;
import fuzs.fantasticwings.util.CubicBezier;
import fuzs.fantasticwings.util.MathHelper;
import fuzs.puzzleslib.api.network.v4.MessageSender;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

public record FlightCapability(Optional<Holder<FlightApparatus>> wings,
                               boolean isFlying,
                               int timeFlying,
                               int prevTimeFlying) {
    public static final Codec<FlightCapability> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    FlightApparatus.CODEC.optionalFieldOf("wing_type").forGetter(FlightCapability::wings),
                    Codec.BOOL.optionalFieldOf("is_flying", false).forGetter(FlightCapability::isFlying),
                    Codec.INT.optionalFieldOf("time_flying", 0).forGetter(FlightCapability::timeFlying))
            .apply(instance, FlightCapability::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, FlightCapability> STREAM_CODEC = StreamCodec.composite(
            FlightApparatus.STREAM_CODEC.apply(ByteBufCodecs::optional),
            FlightCapability::wings,
            ByteBufCodecs.BOOL,
            FlightCapability::isFlying,
            ByteBufCodecs.INT,
            FlightCapability::timeFlying,
            FlightCapability::new);
    public static final FlightCapability VOID = new FlightCapability(Optional.empty(), false, 0);
    private static final CubicBezier FLY_AMOUNT_CURVE = new CubicBezier(0.37F, 0.13F, 0.3F, 1.12F);
    private static final int MAX_TIME_FLYING = 20;
    private static final float MIN_SPEED = 0.03F;
    private static final float MAX_SPEED = 0.0715F;
    private static final float Y_BOOST = 0.05F;
    private static final float MANUAL_Y_BOOST = 0.06F;
    private static final float FALL_REDUCTION = 0.9F;
    private static final float PITCH_OFFSET = 30.0F;

    public FlightCapability(Optional<Holder<FlightApparatus>> wings, boolean isFlying, int timeFlying) {
        this(wings, isFlying, timeFlying, timeFlying);
    }

    public FlightCapability setIsFlying(Player player, boolean isFlying) {
        if (this.isFlying != isFlying) {
            if (isFlying) {
                player.unRide();
            }

            return new FlightCapability(this.wings, isFlying, this.timeFlying, this.prevTimeFlying);
        } else {
            return this;
        }
    }

    public FlightCapability toggleIsFlying(Player player) {
        return this.setIsFlying(player, !this.isFlying);
    }

    public FlightCapability setTimeFlying(int timeFlying) {
        if (this.timeFlying != timeFlying) {
            return new FlightCapability(this.wings, this.isFlying, timeFlying, this.prevTimeFlying);
        } else {
            return this;
        }
    }

    public FlightCapability setPrevTimeFlying(int prevTimeFlying) {
        if (this.prevTimeFlying != prevTimeFlying) {
            return new FlightCapability(this.wings, this.isFlying, this.timeFlying, prevTimeFlying);
        } else {
            return this;
        }
    }

    public FlightCapability setWings(@Nullable Holder<FlightApparatus> wings) {
        if (wings == null && this.wings.isPresent() || wings != null && !this.is(wings)) {
            return new FlightCapability(Optional.ofNullable(wings),
                    this.isFlying,
                    this.timeFlying,
                    this.prevTimeFlying);
        } else {
            return this;
        }
    }

    public boolean isEmpty() {
        return this.wings.isEmpty();
    }

    public boolean is(Holder<FlightApparatus> flightApparatus) {
        Objects.requireNonNull(flightApparatus, "flight apparatus is null");
        return this.wings.filter((Holder<FlightApparatus> holder) -> holder.is(flightApparatus)).isPresent();
    }

    public float getFlyingAmount(float partialTick) {
        return FLY_AMOUNT_CURVE.eval(
                MathHelper.lerp(this.prevTimeFlying, this.timeFlying, partialTick) / MAX_TIME_FLYING);
    }

    public boolean canUseWings(Player player) {
        return !player.getAbilities().flying && !player.getItemBySlot(EquipmentSlot.CHEST)
                .is(ModRegistry.WING_OBSTRUCTIONS);
    }

    public boolean canFly(Player player) {
        return this.canUseWings(player) && this.wings.filter((Holder<FlightApparatus> holder) -> holder.value()
                .isUsableForFlying(player)).isPresent();
    }

    public boolean canSlowlyDescend(Player player) {
        return this.canUseWings(player) && this.wings.filter((Holder<FlightApparatus> holder) -> holder.value()
                .isUsableForSlowlyDescending(player)).isPresent() && (this.isFlying() || !player.isDescending());
    }

    public static FlightCapability tick(FlightCapability flightCapability, Player player) {
        if (flightCapability.wings.isPresent() || !player.isEffectiveAi()) {
            flightCapability = onWornUpdate(flightCapability, player);
        } else if (!player.level().isClientSide && flightCapability.isFlying()) {
            flightCapability = flightCapability.setIsFlying(player, false);
        }
        flightCapability = flightCapability.setPrevTimeFlying(flightCapability.timeFlying);
        if (flightCapability.isFlying()) {
            if (flightCapability.timeFlying < MAX_TIME_FLYING) {
                flightCapability = flightCapability.setTimeFlying(flightCapability.timeFlying + 1);
            } else if (player.isLocalPlayer() && player.onGround()) {
                flightCapability = flightCapability.setIsFlying(player, false);
                MessageSender.broadcast(new ServerboundControlFlyingMessage(false));
            }
        } else if (flightCapability.timeFlying > 0) {
            flightCapability = flightCapability.setTimeFlying(flightCapability.timeFlying - 1);
        }

        return flightCapability;
    }

    private static FlightCapability onWornUpdate(FlightCapability flightCapability, Player player) {
        if (!player.level().isClientSide && flightCapability.isFlying() && !flightCapability.canFly(player)) {
            flightCapability = flightCapability.setIsFlying(player, false);
        }
        if (player.isEffectiveAi()) {
            if (flightCapability.isFlying()) {
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
            if (flightCapability.canSlowlyDescend(player)) {
                Vec3 deltaMovement = player.getDeltaMovement();
                if (deltaMovement.y() < 0.0D) {
                    player.setDeltaMovement(deltaMovement.multiply(1.0D, FALL_REDUCTION, 1.0D));
                }
                player.fallDistance = 0.0F;
            }
        }

        return flightCapability;
    }

    public void onFlown(Player player, Vec3 direction) {
        this.wings.ifPresent((Holder<FlightApparatus> holder) -> {
            if (this.isFlying()) {
                holder.value().onFlying(player, direction);
            } else if (this.canSlowlyDescend(player) && player.getDeltaMovement().y() < -0.5) {
                holder.value().onSlowlyDescending(player, direction);
            }
        });
    }
}
