package fuzs.fantasticwings.flight.apparatus;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fuzs.fantasticwings.FantasticWings;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.Locale;
import java.util.function.IntFunction;

public record FlightApparatus(Identifier textureLocation, Model model, WingSettings wingSettings) {
    public static final ResourceKey<Registry<FlightApparatus>> REGISTRY_KEY = ResourceKey.createRegistryKey(
            FantasticWings.id("wings"));
    public static final Codec<FlightApparatus> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    Identifier.CODEC.fieldOf("asset_id").forGetter(FlightApparatus::textureLocation),
                    Model.CODEC.fieldOf("model").forGetter(FlightApparatus::model),
                    WingSettings.CODEC.fieldOf("wing_settings").forGetter(FlightApparatus::wingSettings))
            .apply(instance, FlightApparatus::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, FlightApparatus> DIRECT_STREAM_CODEC = StreamCodec.composite(
            Identifier.STREAM_CODEC,
            FlightApparatus::textureLocation,
            Model.STREAM_CODEC,
            FlightApparatus::model,
            WingSettings.STREAM_CODEC,
            FlightApparatus::wingSettings,
            FlightApparatus::new);
    public static final Codec<Holder<FlightApparatus>> CODEC = RegistryFixedCodec.create(REGISTRY_KEY);
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<FlightApparatus>> STREAM_CODEC = ByteBufCodecs.holderRegistry(
            REGISTRY_KEY);

    public static Identifier transformTextureLocation(Identifier identifier) {
        return identifier.withPath((String string) -> "textures/" + string + ".png");
    }

    public static Identifier getTextureLocation(ResourceKey<FlightApparatus> resourceKey) {
        return resourceKey.identifier().withPath((String string) -> "entity/equipment/wings/" + string);
    }

    public void onFlying(Player player, Vec3 direction) {
        int distance = Math.round((float) direction.length() * 100.0F);
        if (distance > 0) {
            player.causeFoodExhaustion(distance * this.wingSettings().exhaustionFromFlying());
        }
    }

    public void onSlowlyDescending(Player player, Vec3 direction) {
        player.causeFoodExhaustion(this.wingSettings().exhaustionFromSlowlyDescending());
    }

    public boolean isUsableForFlying(Player player) {
        return player.getAbilities().invulnerable || player.getFoodData().getFoodLevel() >= this.wingSettings()
                .requiredFoodLevelForFlying();
    }

    public boolean isUsableForSlowlyDescending(Player player) {
        return player.getAbilities().invulnerable || player.getFoodData().getFoodLevel() >= this.wingSettings()
                .requiredFoodLevelForSlowlyDescending();
    }

    public enum Model implements StringRepresentable {
        AVIAN,
        INSECTOID;

        public static final StringRepresentableCodec<Model> CODEC = StringRepresentable.fromEnum(Model::values);
        public static final IntFunction<Model> BY_ID = ByIdMap.continuous(Enum::ordinal,
                values(),
                ByIdMap.OutOfBoundsStrategy.ZERO);
        public static final StreamCodec<ByteBuf, Model> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Enum::ordinal);

        @Override
        public String getSerializedName() {
            return this.name().toLowerCase(Locale.ROOT);
        }
    }
}
