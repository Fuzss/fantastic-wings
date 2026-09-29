package fuzs.fantasticwings.common.init;

import fuzs.fantasticwings.common.world.item.consume_effects.GrantWingsConsumeEffect;
import fuzs.fantasticwings.common.world.item.consume_effects.TakeWingsConsumeEffect;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.consume_effects.ConsumeEffect;

public class ModConsumeEffectTypes {
    public static final Holder.Reference<ConsumeEffect.Type<GrantWingsConsumeEffect>> GRANT_WINGS_CONSUME_EFFECT_TYPE = ModRegistry.REGISTRIES.register(
            Registries.CONSUME_EFFECT_TYPE,
            "grant_wings",
            () -> new ConsumeEffect.Type<>(GrantWingsConsumeEffect.CODEC, GrantWingsConsumeEffect.STREAM_CODEC));
    public static final Holder.Reference<ConsumeEffect.Type<TakeWingsConsumeEffect>> TAKE_WINGS_CONSUME_EFFECT_TYPE = ModRegistry.REGISTRIES.register(
            Registries.CONSUME_EFFECT_TYPE,
            "take_wings",
            () -> new ConsumeEffect.Type<>(TakeWingsConsumeEffect.CODEC, TakeWingsConsumeEffect.STREAM_CODEC));

    public static void bootstrap() {
        // NO-OP
    }
}
