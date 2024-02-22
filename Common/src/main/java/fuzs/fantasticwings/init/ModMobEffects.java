package fuzs.fantasticwings.init;

import fuzs.fantasticwings.FantasticWings;
import fuzs.fantasticwings.server.flight.apparatus.FlightApparatusImpl;
import fuzs.fantasticwings.world.effect.WingsMobEffect;
import fuzs.puzzleslib.api.init.v3.registry.RegistryManager;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;

public class ModMobEffects {
    static final RegistryManager REGISTRY = RegistryManager.from(FantasticWings.MOD_ID);
    public static final Holder.Reference<MobEffect> GROW_WINGS_MOB_EFFECT = REGISTRY.registerMobEffect("grow_wings", () -> new WingsMobEffect(
            MobEffectCategory.BENEFICIAL));
    public static final Holder.Reference<MobEffect> SHED_WINGS_MOB_EFFECT = REGISTRY.registerMobEffect("shed_wings", () -> new WingsMobEffect(
            MobEffectCategory.HARMFUL));
    public static final Holder.Reference<Potion> BAT_BLOOD_POTION = REGISTRY.registerPotion("bat_blood", () -> new Potion(new MobEffectInstance(SHED_WINGS_MOB_EFFECT.value(), 1)));

    public static void touch() {
        FlightApparatusImpl.forEach(flightApparatus -> {
            flightApparatus.registerPotion(REGISTRY::registerPotion);
        });
    }
}
