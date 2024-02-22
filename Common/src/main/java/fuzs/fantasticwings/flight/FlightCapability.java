package fuzs.fantasticwings.flight;

import fuzs.fantasticwings.FantasticWings;
import fuzs.fantasticwings.flight.apparatus.FlightApparatus;
import fuzs.fantasticwings.init.ModTags;
import fuzs.fantasticwings.network.ServerboundControlFlyingMessage;
import fuzs.fantasticwings.util.CubicBezier;
import fuzs.fantasticwings.util.MathHelper;
import fuzs.puzzleslib.api.capability.v3.data.CapabilityComponent;
import fuzs.puzzleslib.api.network.v3.PlayerSet;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

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
    private static final float FALL_REDUCTION = 0.9F;
    private static final float PITCH_OFFSET = 30.0F;

    private int prevTimeFlying = INITIAL_TIME_FLYING;
    private int timeFlying = INITIAL_TIME_FLYING;
    private boolean isFlying;
    private FlightApparatus.Holder holder = FlightApparatus.Holder.empty();

    public void setIsFlying(boolean isFlying) {
        this.setIsFlying(isFlying, null);
    }

    public void setIsFlying(boolean isFlying, @Nullable PlayerSet playerSet) {
        if (this.isFlying != isFlying) {
            this.isFlying = isFlying;
            if (this.isFlying) {
                this.getHolder().unRide();
            }
            this.setChanged(playerSet);
        }
    }

    public boolean isFlying() {
        return this.isFlying;
    }

    public void toggleIsFlying(PlayerSet playerSet) {
        this.setIsFlying(!this.isFlying, playerSet);
    }

    public void setTimeFlying(int timeFlying) {
        if (this.timeFlying != timeFlying) {
            this.timeFlying = timeFlying;
            this.setChanged();
        }
    }

    public int getTimeFlying() {
        return this.timeFlying;
    }

    public void setWings(FlightApparatus.Holder flightApparatus) {
        if (!this.holder.is(flightApparatus)) {
            this.holder = flightApparatus;
            this.setChanged();
        }
    }

    public FlightApparatus.Holder getWings() {
        return this.holder;
    }

    public float getFlyingAmount(float delta) {
        return FLY_AMOUNT_CURVE.eval(MathHelper.lerp(this.getPrevTimeFlying(),
                this.getTimeFlying(),
                delta
        ) / MAX_TIME_FLYING);
    }

    private void setPrevTimeFlying(int prevTimeFlying) {
        this.prevTimeFlying = prevTimeFlying;
    }

    private int getPrevTimeFlying() {
        return this.prevTimeFlying;
    }

    public boolean canFly() {
        return this.holder.flightApparatus().isUsable(this.getHolder()) && !this.getHolder().getItemBySlot(EquipmentSlot.CHEST).is(
                ModTags.WING_OBSTRUCTIONS);
    }

    public boolean canLand() {
        return this.holder.flightApparatus().isLandable(this.getHolder());
    }

    private void onWornUpdate() {
        Player player = this.getHolder();
        if (!player.level().isClientSide) {
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
                                vz * vxz * speed
                        ));
                if (!this.holder.flightApparatus().isUsable(player)) {
                    this.setIsFlying(false);
                }
            }
            if (this.canLand()) {
                Vec3 deltaMovement = player.getDeltaMovement();
                if (deltaMovement.y() < 0.0D) {
                    player.setDeltaMovement(deltaMovement.multiply(1.0D, FALL_REDUCTION, 1.0D));
                }
                player.fallDistance = 0.0F;
            }
        }
    }

    public void tick() {
        Player player = this.getHolder();
        if (this.canFly() || player.level().isClientSide) {
            this.onWornUpdate();
        } else if (!player.level().isClientSide) {
            this.setWings(FlightApparatus.Holder.empty());
            if (this.isFlying()) {
                this.setIsFlying(false);
            }
        }
        this.setPrevTimeFlying(this.getTimeFlying());
        if (this.isFlying()) {
            if (this.getTimeFlying() < MAX_TIME_FLYING) {
                this.setTimeFlying(this.getTimeFlying() + 1);
            } else if (player.isLocalPlayer() && player.onGround()) {
                this.setIsFlying(false, PlayerSet.ofNone());
                FantasticWings.NETWORK.sendMessage(new ServerboundControlFlyingMessage(false));
            }
        } else {
            if (this.getTimeFlying() > INITIAL_TIME_FLYING) {
                this.setTimeFlying(this.getTimeFlying() - 1);
            }
        }
    }

    public void onFlown(Vec3 direction) {
        Player player = this.getHolder();
        if (this.isFlying()) {
            this.holder.flightApparatus().onFlight(player, direction);
        } else if (player.getDeltaMovement().y() < -0.5) {
            this.holder.flightApparatus().onLanding(player, direction);
        }
    }

    @Override
    public void write(CompoundTag compoundTag) {
        compoundTag.putBoolean(KEY_IS_FLYING, this.isFlying);
        compoundTag.putInt(KEY_TIME_FLYING, this.timeFlying);
        compoundTag.put(KEY_WING_TYPE, this.holder.writeToNBTTag());
    }

    @Override
    public void read(CompoundTag compoundTag) {
        this.isFlying = compoundTag.getBoolean(KEY_IS_FLYING);
        this.setTimeFlying(compoundTag.getInt(KEY_TIME_FLYING));
        this.holder = FlightApparatus.Holder.readFromNBTTag(compoundTag.getCompound(KEY_WING_TYPE));
    }
}
