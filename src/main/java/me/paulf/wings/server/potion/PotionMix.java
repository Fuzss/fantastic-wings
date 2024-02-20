package me.paulf.wings.server.potion;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraftforge.common.brewing.BrewingRecipe;

public class PotionMix extends BrewingRecipe {
    private final Potion from;

    public PotionMix(Potion from, Ingredient ingredient, Potion to) {
        this(from, ingredient, createPotionStack(to));
    }

    public PotionMix(Potion from, Ingredient ingredient, ItemStack result) {
        super(Ingredient.of(createPotionStack(from)), ingredient, result);
        this.from = from;
    }

    @Override
    public boolean isInput(ItemStack stack) {
        return !stack.isEmpty() && PotionUtils.getPotion(stack) == this.from;
    }

    private static ItemStack createPotionStack(Potion potion) {
        return PotionUtils.setPotion(new ItemStack(Items.POTION), potion);
    }
}
