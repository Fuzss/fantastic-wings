package fuzs.fantasticwings.server.effect;

import fuzs.fantasticwings.WingsMod;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public final class WingsEffects {
    private WingsEffects() {
    }

    public static final DeferredRegister<MobEffect> REG = DeferredRegister.create(ForgeRegistries.POTIONS, WingsMod.ID);

    public static final Holder.Reference<MobEffect> WINGS = REG.register("wings", () -> new WingedEffect(0x97cae4));
}
