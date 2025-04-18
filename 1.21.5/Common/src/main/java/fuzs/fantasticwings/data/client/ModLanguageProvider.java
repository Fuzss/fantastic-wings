package fuzs.fantasticwings.data.client;

import fuzs.fantasticwings.FantasticWings;
import fuzs.fantasticwings.client.init.ClientModRegistry;
import fuzs.fantasticwings.commands.WingsCommand;
import fuzs.fantasticwings.flight.apparatus.FlightApparatus;
import fuzs.fantasticwings.init.ModRegistry;
import fuzs.fantasticwings.world.item.BottledWingsItem;
import fuzs.fantasticwings.world.item.WithDescriptionItem;
import fuzs.puzzleslib.api.client.data.v2.AbstractLanguageProvider;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import net.minecraft.resources.ResourceKey;

public class ModLanguageProvider extends AbstractLanguageProvider {

    public ModLanguageProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addTranslations(TranslationBuilder translationBuilder) {
        translationBuilder.addCreativeModeTab(FantasticWings.MOD_ID, FantasticWings.MOD_NAME);
        translationBuilder.addKeyCategory(FantasticWings.MOD_ID, FantasticWings.MOD_NAME);
        translationBuilder.add(ClientModRegistry.FLY_KEY_MAPPING, "Toggle Flight");
        translationBuilder.add(WingsCommand.KEY_GIVE_WINGS_SINGLE, "Applied wings to %s");
        translationBuilder.add(WingsCommand.KEY_GIVE_WINGS_MULTIPLE, "Applied wings to %s targets");
        translationBuilder.add(WingsCommand.KEY_TAKE_WINGS_SINGLE, "Removed wings from %s");
        translationBuilder.add(WingsCommand.KEY_TAKE_WINGS_MULTIPLE, "Removed wings from %s targets");
        translationBuilder.add(WingsCommand.COMPONENT_GIVE_WINGS_FAILED, "Unable to apply wings to target");
        translationBuilder.add(WingsCommand.COMPONENT_TAKE_WINGS_FAILED, "Target doesn't have wings to remove");
        translationBuilder.add(ModRegistry.BOTTLED_BAT_BLOOD_ITEM.value(), "Bottled Bat Blood");
        translationBuilder.add(((WithDescriptionItem) ModRegistry.BOTTLED_BAT_BLOOD_ITEM.value()).getDescriptionComponent(),
                "Consume to shed your wings.");
        translationBuilder.add(ModRegistry.BOTTLED_WINGS_ITEM.value(), "Bottled Wings");
        translationBuilder.add(((WithDescriptionItem) ModRegistry.BOTTLED_WINGS_ITEM.value()).getDescriptionComponent(),
                "Consume to grow a set of wings.");
        addWings(ModRegistry.ANGEL_FLIGHT_APPARATUS, "Angel Wings", translationBuilder);
        addWings(ModRegistry.BAT_FLIGHT_APPARATUS, "Bat Wings", translationBuilder);
        addWings(ModRegistry.BLUE_BUTTERFLY_FLIGHT_APPARATUS, "Blue Butterfly Wings", translationBuilder);
        addWings(ModRegistry.DRAGON_FLIGHT_APPARATUS, "Dragon Wings", translationBuilder);
        addWings(ModRegistry.EVIL_FLIGHT_APPARATUS, "Evil Wings", translationBuilder);
        addWings(ModRegistry.FAIRY_FLIGHT_APPARATUS, "Fairy Wings", translationBuilder);
        addWings(ModRegistry.FIRE_FLIGHT_APPARATUS, "Fire Wings", translationBuilder);
        addWings(ModRegistry.MONARCH_BUTTERFLY_FLIGHT_APPARATUS, "Monarch Butterfly Wings", translationBuilder);
        addWings(ModRegistry.PARROT_FLIGHT_APPARATUS, "Parrot Wings", translationBuilder);
        addWings(ModRegistry.SLIME_FLIGHT_APPARATUS, "Slime Wings", translationBuilder);
        addWings(ModRegistry.METALLIC_FLIGHT_APPARATUS, "Metallic Wings", translationBuilder);
        translationBuilder.add(ModRegistry.ITEM_ARMOR_EQUIP_WINGS.value(), "Wings rustle");
    }

    static void addWings(ResourceKey<FlightApparatus> resourceKey, String value, TranslationBuilder translationBuilder) {
        translationBuilder.add(((BottledWingsItem) ModRegistry.BOTTLED_WINGS_ITEM.value()).getWingsComponent(resourceKey),
                "Bottled " + value);
    }
}
