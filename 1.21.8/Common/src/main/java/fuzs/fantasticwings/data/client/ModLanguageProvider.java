package fuzs.fantasticwings.data.client;

import fuzs.fantasticwings.FantasticWings;
import fuzs.fantasticwings.client.init.ClientModRegistry;
import fuzs.fantasticwings.commands.WingsCommand;
import fuzs.fantasticwings.flight.apparatus.FlightApparatus;
import fuzs.fantasticwings.init.FlightApparatuses;
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
    public void addTranslations(TranslationBuilder builder) {
        builder.add(ModRegistry.CREATIVE_MODE_TAB.value(), FantasticWings.MOD_NAME);
        builder.addKeyCategory(FantasticWings.MOD_ID, FantasticWings.MOD_NAME);
        builder.add(ClientModRegistry.FLY_KEY_MAPPING, "Toggle Flight");
        builder.add(WingsCommand.KEY_GIVE_WINGS_SINGLE, "Applied wings to %s");
        builder.add(WingsCommand.KEY_GIVE_WINGS_MULTIPLE, "Applied wings to %s targets");
        builder.add(WingsCommand.KEY_TAKE_WINGS_SINGLE, "Removed wings from %s");
        builder.add(WingsCommand.KEY_TAKE_WINGS_MULTIPLE, "Removed wings from %s targets");
        builder.add(WingsCommand.COMPONENT_GIVE_WINGS_FAILED, "Unable to apply wings to target");
        builder.add(WingsCommand.COMPONENT_TAKE_WINGS_FAILED, "Target doesn't have wings to remove");
        builder.add(ModRegistry.BOTTLED_BAT_BLOOD_ITEM.value(), "Bottled Bat Blood");
        builder.add(((WithDescriptionItem) ModRegistry.BOTTLED_BAT_BLOOD_ITEM.value()).getDescriptionComponent(),
                "Consume to shed your wings.");
        builder.add(ModRegistry.BOTTLED_WINGS_ITEM.value(), "Bottled Wings");
        builder.add(((WithDescriptionItem) ModRegistry.BOTTLED_WINGS_ITEM.value()).getDescriptionComponent(),
                "Consume to grow a set of wings.");
        addWings(FlightApparatuses.ANGEL_FLIGHT_APPARATUS, "Angel Wings", builder);
        addWings(FlightApparatuses.BAT_FLIGHT_APPARATUS, "Bat Wings", builder);
        addWings(FlightApparatuses.BLUE_BUTTERFLY_FLIGHT_APPARATUS, "Blue Butterfly Wings", builder);
        addWings(FlightApparatuses.DRAGON_FLIGHT_APPARATUS, "Dragon Wings", builder);
        addWings(FlightApparatuses.EVIL_FLIGHT_APPARATUS, "Evil Wings", builder);
        addWings(FlightApparatuses.FAIRY_FLIGHT_APPARATUS, "Fairy Wings", builder);
        addWings(FlightApparatuses.FIRE_FLIGHT_APPARATUS, "Fire Wings", builder);
        addWings(FlightApparatuses.MONARCH_BUTTERFLY_FLIGHT_APPARATUS, "Monarch Butterfly Wings", builder);
        addWings(FlightApparatuses.PARROT_FLIGHT_APPARATUS, "Parrot Wings", builder);
        addWings(FlightApparatuses.SLIME_FLIGHT_APPARATUS, "Slime Wings", builder);
        addWings(FlightApparatuses.METALLIC_FLIGHT_APPARATUS, "Metallic Wings", builder);
        builder.add(ModRegistry.ITEM_ARMOR_EQUIP_WINGS.value(), "Wings rustle");
    }

    static void addWings(ResourceKey<FlightApparatus> resourceKey, String value, TranslationBuilder translationBuilder) {
        translationBuilder.add(((BottledWingsItem) ModRegistry.BOTTLED_WINGS_ITEM.value()).getWingsComponent(resourceKey),
                "Bottled " + value);
    }
}
