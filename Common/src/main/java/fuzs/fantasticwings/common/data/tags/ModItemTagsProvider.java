package fuzs.fantasticwings.common.data.tags;

import fuzs.fantasticwings.common.init.ModItems;
import fuzs.fantasticwings.common.init.ModTags;
import fuzs.puzzleslib.common.api.data.v3.core.DataProviderContext;
import fuzs.puzzleslib.common.api.data.v3.tags.AbstractTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.references.ItemIds;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;

public class ModItemTagsProvider extends AbstractTagsProvider<Item> {

    public ModItemTagsProvider(DataProviderContext context) {
        super(Registries.ITEM, context);
    }

    @Override
    public void addTags(HolderLookup.Provider provider) {
        this.tag(ItemTags.BREWING_POTION_INPUTS).add(ModItems.BOTTLED_WINGS_ITEM);
        this.tag(ModTags.Items.WING_OBSTRUCTIONS_ITEM_TAG).add(ItemIds.ELYTRA);
    }
}
