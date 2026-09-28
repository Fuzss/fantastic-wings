package fuzs.fantasticwings.common.data.client;

import fuzs.fantasticwings.common.FantasticWings;
import fuzs.fantasticwings.common.client.init.ClientModRegistry;
import fuzs.fantasticwings.common.commands.WingsCommand;
import fuzs.fantasticwings.common.flight.apparatus.FlightApparatus;
import fuzs.fantasticwings.common.init.FlightApparatuses;
import fuzs.fantasticwings.common.init.ModRegistry;
import fuzs.fantasticwings.common.world.item.BottledWingsItem;
import fuzs.fantasticwings.common.world.item.WithDescriptionItem;
import fuzs.puzzleslib.common.api.client.data.v3.language.AbstractLanguageProvider;
import fuzs.puzzleslib.common.api.client.data.v3.language.TranslationBuilder;
import fuzs.puzzleslib.common.api.data.v3.core.DataProviderContext;
import net.minecraft.resources.ResourceKey;

public class ModLanguageProvider extends AbstractLanguageProvider {

    public ModLanguageProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addTranslations() {
        this.add(ModRegistry.CREATIVE_MODE_TAB.value(), FantasticWings.MOD_NAME);
        this.addKeyCategory(FantasticWings.MOD_ID, FantasticWings.MOD_NAME);
        this.add(ClientModRegistry.FLY_KEY_MAPPING, "Toggle Flight");
        this.add(WingsCommand.KEY_GIVE_WINGS_SINGLE, "Applied wings to %s");
        this.add(WingsCommand.KEY_GIVE_WINGS_MULTIPLE, "Applied wings to %s targets");
        this.add(WingsCommand.KEY_TAKE_WINGS_SINGLE, "Removed wings from %s");
        this.add(WingsCommand.KEY_TAKE_WINGS_MULTIPLE, "Removed wings from %s targets");
        this.add(WingsCommand.COMPONENT_GIVE_WINGS_FAILED, "Unable to apply wings to target");
        this.add(WingsCommand.COMPONENT_TAKE_WINGS_FAILED, "Target doesn't have wings to remove");
        this.add(ModRegistry.BOTTLED_BAT_BLOOD_ITEM.value(), "Bottled Bat Blood");
        this.add(((WithDescriptionItem) ModRegistry.BOTTLED_BAT_BLOOD_ITEM.value()).getDescriptionComponent(),
                "Consume to shed your wings.");
        this.add(ModRegistry.BOTTLED_WINGS_ITEM.value(), "Bottled Wings");
        this.add(((WithDescriptionItem) ModRegistry.BOTTLED_WINGS_ITEM.value()).getDescriptionComponent(),
                "Consume to grow a set of wings.");
        addWings(FlightApparatuses.ANGEL_FLIGHT_APPARATUS, "Angel Wings", this);
        addWings(FlightApparatuses.BAT_FLIGHT_APPARATUS, "Bat Wings", this);
        addWings(FlightApparatuses.BLUE_BUTTERFLY_FLIGHT_APPARATUS, "Blue Butterfly Wings", this);
        addWings(FlightApparatuses.DRAGON_FLIGHT_APPARATUS, "Dragon Wings", this);
        addWings(FlightApparatuses.EVIL_FLIGHT_APPARATUS, "Evil Wings", this);
        addWings(FlightApparatuses.FAIRY_FLIGHT_APPARATUS, "Fairy Wings", this);
        addWings(FlightApparatuses.FIRE_FLIGHT_APPARATUS, "Fire Wings", this);
        addWings(FlightApparatuses.MONARCH_BUTTERFLY_FLIGHT_APPARATUS, "Monarch Butterfly Wings", this);
        addWings(FlightApparatuses.PARROT_FLIGHT_APPARATUS, "Parrot Wings", this);
        addWings(FlightApparatuses.SLIME_FLIGHT_APPARATUS, "Slime Wings", this);
        addWings(FlightApparatuses.METALLIC_FLIGHT_APPARATUS, "Metallic Wings", this);
        this.add(ModRegistry.ITEM_ARMOR_EQUIP_WINGS.value(), "Wings rustle");
    }

    static void addWings(ResourceKey<FlightApparatus> resourceKey, String value, TranslationBuilder translationBuilder) {
        translationBuilder.add(((BottledWingsItem) ModRegistry.BOTTLED_WINGS_ITEM.value()).getWingsComponent(resourceKey),
                "Bottled " + value);
    }
}
