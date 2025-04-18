package fuzs.fantasticwings.world.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;

public class WithDescriptionItem extends Item {

    public WithDescriptionItem(Properties properties) {
        super(properties);
    }

    public Component getDescriptionComponent() {
        return Component.translatable(this.getDescriptionId() + ".description").withStyle(ChatFormatting.GOLD);
    }
}
