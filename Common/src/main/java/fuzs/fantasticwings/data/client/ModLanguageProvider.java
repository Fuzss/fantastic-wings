package fuzs.fantasticwings.data.client;

import fuzs.fantasticwings.FantasticWings;
import fuzs.fantasticwings.client.init.ClientModRegistry;
import fuzs.fantasticwings.commands.WingsArgument;
import fuzs.fantasticwings.commands.WingsCommand;
import fuzs.fantasticwings.flight.apparatus.FlightApparatusImpl;
import fuzs.fantasticwings.init.ModRegistry;
import fuzs.puzzleslib.api.client.data.v2.AbstractLanguageProvider;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import net.minecraft.core.Holder;
import net.minecraft.world.item.alchemy.Potion;

import java.util.Objects;
import java.util.Optional;

public class ModLanguageProvider extends AbstractLanguageProvider {

    public ModLanguageProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addTranslations(TranslationBuilder builder) {
        builder.addCreativeModeTab(FantasticWings.MOD_ID, FantasticWings.MOD_NAME);
        builder.addKeyCategory(FantasticWings.MOD_ID, FantasticWings.MOD_NAME);
        builder.add(ClientModRegistry.FLY_KEY_MAPPING, "Toggle Flight");
        builder.add(WingsArgument.KEY_WINGS_NOT_FOUND, "No such wings: %s");
        builder.add(WingsCommand.KEY_GIVE_WINGS_SINGLE, "Applied wings to %s");
        builder.add(WingsCommand.KEY_GIVE_WINGS_MULTIPLE, "Applied wings to %s targets");
        builder.add(WingsCommand.KEY_TAKE_WINGS_SINGLE, "Removed wings from %s");
        builder.add(WingsCommand.KEY_TAKE_WINGS_MULTIPLE, "Removed wings from %s targets");
        builder.add(WingsCommand.COMPONENT_GIVE_WINGS_FAILED, "Unable to apply wings to target");
        builder.add(WingsCommand.COMPONENT_TAKE_WINGS_FAILED, "Target doesn't have wings to remove");
        builder.add(ModRegistry.GROW_WINGS_MOB_EFFECT.value(), "Grow Wings");
        builder.add(ModRegistry.SHED_WINGS_MOB_EFFECT.value(), "Shed Wings");
        addPotion(builder, ModRegistry.BAT_BLOOD_POTION, "Bat Blood");
        addPotion(builder, FlightApparatusImpl.ANGEL.getPotion(), "Angel Wings");
        addPotion(builder, FlightApparatusImpl.BAT.getPotion(), "Bat Wings");
        addPotion(builder, FlightApparatusImpl.BLUE_BUTTERFLY.getPotion(), "Blue Butterfly Wings");
        addPotion(builder, FlightApparatusImpl.DRAGON.getPotion(), "Dragon Wings");
        addPotion(builder, FlightApparatusImpl.EVIL.getPotion(), "Evil Wings");
        addPotion(builder, FlightApparatusImpl.FAIRY.getPotion(), "Fairy Wings");
        addPotion(builder, FlightApparatusImpl.FIRE.getPotion(), "Fire Wings");
        addPotion(builder, FlightApparatusImpl.MONARCH_BUTTERFLY.getPotion(), "Monarch Butterfly Wings");
        addPotion(builder, FlightApparatusImpl.PARROT.getPotion(), "Parrot Wings");
        addPotion(builder, FlightApparatusImpl.SLIME.getPotion(), "Slime Wings");
        addPotion(builder, FlightApparatusImpl.METALLIC.getPotion(), "Metallic Wings");
        builder.add(ModRegistry.ITEM_ARMOR_EQUIP_WINGS.value(), "Wings rustle");
    }

    private static void addPotion(TranslationBuilder builder, Holder<Potion> potion, String value) {
        Objects.requireNonNull(potion, "potion is null");
        String potionName = Potion.getName(Optional.of(potion), "");
        builder.add("item.minecraft.tipped_arrow.effect." + potionName, "Arrow of " + value);
        builder.add("item.minecraft.potion.effect." + potionName, value + " Bottle");
        builder.add("item.minecraft.splash_potion.effect." + potionName, "Splash " + value + " Bottle");
        builder.add("item.minecraft.lingering_potion.effect." + potionName, "Lingering " + value + " Bottle");
    }
}
