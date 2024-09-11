package fuzs.fantasticwings.neoforge.data.client;

import fuzs.fantasticwings.init.ModRegistry;
import fuzs.puzzleslib.neoforge.api.data.v2.client.AbstractSoundDefinitionProvider;
import fuzs.puzzleslib.neoforge.api.data.v2.core.NeoForgeDataProviderContext;
import net.neoforged.neoforge.common.data.SoundDefinition;

public class ModSoundProvider extends AbstractSoundDefinitionProvider {

    public ModSoundProvider(NeoForgeDataProviderContext context) {
        super(context);
    }

    @Override
    public void addSoundDefinitions() {
        this.add(ModRegistry.ITEM_ARMOR_EQUIP_WINGS.value(),
                "item/armor/equip_leather1",
                "item/armor/equip_leather2",
                "item/armor/equip_leather3",
                "item/armor/equip_leather4",
                "item/armor/equip_leather5",
                "item/armor/equip_leather6"
        );
        SoundDefinition soundDefinition = definition().with(sound("item/elytra/elytra_loop").volume(0.6));
        this.add(ModRegistry.ITEM_WINGS_FLYING.value(), soundDefinition);
        soundDefinition.subtitle(null);
    }
}
