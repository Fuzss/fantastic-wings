package fuzs.fantasticwings.neoforge.data.client;

import fuzs.fantasticwings.common.init.ModRegistry;
import fuzs.puzzleslib.common.api.data.v2.core.DataProviderContext;
import fuzs.puzzleslib.neoforge.api.client.data.v2.AbstractSoundProvider;
import net.neoforged.neoforge.common.data.SoundDefinition;

public class ModSoundProvider extends AbstractSoundProvider {

    public ModSoundProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addSounds() {
        this.add(ModRegistry.ITEM_ARMOR_EQUIP_WINGS.value(),
                "item/armor/equip_leather1",
                "item/armor/equip_leather2",
                "item/armor/equip_leather3",
                "item/armor/equip_leather4",
                "item/armor/equip_leather5",
                "item/armor/equip_leather6");
        SoundDefinition soundDefinition = definition().with(sound("item/elytra/elytra_loop").volume(0.6));
        this.add(ModRegistry.ITEM_WINGS_FLYING.value(), soundDefinition);
        soundDefinition.subtitle(null);
    }
}
