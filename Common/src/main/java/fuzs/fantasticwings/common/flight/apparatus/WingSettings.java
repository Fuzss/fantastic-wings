package fuzs.fantasticwings.common.flight.apparatus;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;

public record WingSettings(int requiredFoodLevelForFlying,
                           float exhaustionFromFlying,
                           int requiredFoodLevelForSlowlyDescending,
                           float exhaustionFromSlowlyDescending) {
    public static final WingSettings DEFAULT = new WingSettings(6, 0.0001F, 2, 0.005F);
    public static final Codec<WingSettings> CODEC = RecordCodecBuilder.create(instance -> instance.group(ExtraCodecs.intRange(
                            0,
                            20)
                    .optionalFieldOf("required_food_level_for_flying", DEFAULT.requiredFoodLevelForFlying())
                    .forGetter(WingSettings::requiredFoodLevelForFlying),
            ExtraCodecs.floatRange(0.0F, 10.0F)
                    .optionalFieldOf("exhaustion_from_flying", DEFAULT.exhaustionFromFlying())
                    .forGetter(WingSettings::exhaustionFromFlying),
            ExtraCodecs.intRange(0, 20)
                    .optionalFieldOf("required_food_level_for_slowly_descending",
                            DEFAULT.requiredFoodLevelForSlowlyDescending())
                    .forGetter(WingSettings::requiredFoodLevelForSlowlyDescending),
            ExtraCodecs.floatRange(0.0F, 10.0F)
                    .optionalFieldOf("exhaustion_from_slowly_descending", DEFAULT.exhaustionFromSlowlyDescending())
                    .forGetter(WingSettings::exhaustionFromSlowlyDescending)).apply(instance, WingSettings::new));
    public static final StreamCodec<ByteBuf, WingSettings> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT,
            WingSettings::requiredFoodLevelForFlying,
            ByteBufCodecs.FLOAT,
            WingSettings::exhaustionFromFlying,
            ByteBufCodecs.VAR_INT,
            WingSettings::requiredFoodLevelForSlowlyDescending,
            ByteBufCodecs.FLOAT,
            WingSettings::exhaustionFromSlowlyDescending,
            WingSettings::new);
}
