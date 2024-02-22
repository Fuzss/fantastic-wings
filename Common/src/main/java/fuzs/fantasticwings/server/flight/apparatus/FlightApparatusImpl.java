package fuzs.fantasticwings.server.flight.apparatus;

import fuzs.fantasticwings.FantasticWings;
import fuzs.fantasticwings.init.ModMobEffects;
import fuzs.fantasticwings.server.config.ServerConfig;
import fuzs.puzzleslib.api.init.v3.PotionBrewingRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;

import java.util.function.*;

public enum FlightApparatusImpl implements WingSettingsApparatus, StringRepresentable {
    ANGEL("angel_wings", () -> Items.FEATHER, serverConfig -> serverConfig.angelWings),
    PARROT("parrot_wings", () -> Items.RED_DYE, serverConfig -> serverConfig.parrotWings),
    SLIME("slime_wings", () -> Items.SLIME_BALL, serverConfig -> serverConfig.slimeWings),
    BLUE_BUTTERFLY("blue_butterfly_wings", () -> Items.BLUE_DYE, serverConfig -> serverConfig.blueButterflyWings),
    MONARCH_BUTTERFLY("monarch_butterfly_wings",
            () -> Items.ORANGE_DYE,
            serverConfig -> serverConfig.monarchButterflyWings
    ),
    FIRE("fire_wings", () -> Items.BLAZE_POWDER, serverConfig -> serverConfig.fireWings),
    BAT("bat_wings", () -> Items.LEATHER, serverConfig -> serverConfig.batWings),
    FAIRY("fairy_wings", () -> Items.OXEYE_DAISY, serverConfig -> serverConfig.fairyWings),
    EVIL("evil_wings", () -> Items.BONE, serverConfig -> serverConfig.evilWings),
    DRAGON("dragon_wings", () -> Items.FIRE_CHARGE, serverConfig -> serverConfig.dragonWings);

    private static final FlightApparatusImpl[] VALUES = values();
    private static final IntFunction<FlightApparatusImpl> BY_ID = ByIdMap.continuous(Enum::ordinal,
            values(),
            ByIdMap.OutOfBoundsStrategy.ZERO
    );

    private final String name;
    private final Supplier<Item> ingredient;
    private final Function<ServerConfig, WingSettings> settingsExtractor;

    FlightApparatusImpl(String name, Supplier<Item> ingredient, Function<ServerConfig, WingSettings> settingsExtractor) {
        this.name = name;
        this.ingredient = ingredient;
        this.settingsExtractor = settingsExtractor;
    }

    public static FlightApparatusImpl byId(int id) {
        return BY_ID.apply(id);
    }

    public Holder holder() {
        return Holder.of(this);
    }

    public String id() {
        return this.resourceLocation().toString();
    }

    public ResourceLocation resourceLocation() {
        return FantasticWings.id(this.name);
    }

    public void registerPotion(BiConsumer<String, Supplier<Potion>> consumer) {
        consumer.accept(this.name, () -> {
            return new Potion(new MobEffectInstance(ModMobEffects.GROW_WINGS_MOB_EFFECT.value(), this.ordinal()));
        });
    }

    public void registerBrewingRecipes() {
        PotionBrewingRegistry.INSTANCE.registerPotionRecipe(Potions.SLOW_FALLING,
                this.ingredient.get(),
                this.getPotion()
        );
        PotionBrewingRegistry.INSTANCE.registerPotionRecipe(Potions.LONG_SLOW_FALLING,
                this.ingredient.get(),
                this.getPotion()
        );
    }

    public Potion getPotion() {
        return BuiltInRegistries.POTION.get(this.resourceLocation());
    }

    public static void forEach(Consumer<FlightApparatusImpl> consumer) {
        for (FlightApparatusImpl flightApparatus : VALUES) {
            consumer.accept(flightApparatus);
        }
    }

    @Override
    public WingSettings getWingSettings() {
        return this.settingsExtractor.apply(FantasticWings.CONFIG.get(ServerConfig.class));
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}
