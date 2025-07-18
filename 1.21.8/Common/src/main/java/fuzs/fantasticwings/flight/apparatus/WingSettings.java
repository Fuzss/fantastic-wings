package fuzs.fantasticwings.flight.apparatus;

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
    public static final Codec<WingSettings> CODEC = RecordCodecBuilder.create(instance -> instance.group(ExtraCodecs.intRange(
                    0,
                    20).fieldOf("required_food_level_for_flying").forGetter(WingSettings::requiredFoodLevelForFlying),
            ExtraCodecs.floatRange(0.0F, 10.0F)
                    .fieldOf("exhaustion_from_flying")
                    .forGetter(WingSettings::exhaustionFromFlying),
            ExtraCodecs.intRange(0, 20)
                    .fieldOf("required_food_level_for_slowly_descending")
                    .forGetter(WingSettings::requiredFoodLevelForSlowlyDescending),
            ExtraCodecs.floatRange(0.0F, 10.0F)
                    .fieldOf("exhaustion_from_slowly_descending")
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
