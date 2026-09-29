package fuzs.fantasticwings.common.data.recipes;

import fuzs.fantasticwings.common.flight.apparatus.FlightApparatus;
import fuzs.fantasticwings.common.init.FlightApparatuses;
import fuzs.fantasticwings.common.init.ModRegistry;
import fuzs.fantasticwings.common.world.item.BottledWingsItem;
import fuzs.puzzleslib.common.api.data.v3.recipes.AbstractBrewingProvider;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.component.predicates.PotionsPredicate;
import net.minecraft.data.recipes.BrewingRecipeBuilder;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.PotionIngredient;
import net.minecraft.world.item.crafting.Recipe;

public class ModBrewingProvider extends AbstractBrewingProvider {
    private final HolderGetter<FlightApparatus> wings;

    public ModBrewingProvider(BootstrapContext<Recipe<?>> recipeOutput, BootstrapContext<Advancement> advancementOutput) {
        super(recipeOutput, advancementOutput);
        this.wings = recipeOutput.lookup(FlightApparatus.REGISTRY_KEY);
    }

    @Override
    protected void addContainers() {
        this.addContainer(Items.POTION);
    }

    @Override
    protected void addContainerTransformations() {
        // NO-OP
    }

    @Override
    protected void buildMixes() {
        this.buildWingsMix(Items.FEATHER, FlightApparatuses.ANGEL_FLIGHT_APPARATUS);
        this.buildWingsMix(Items.DYE.red(), FlightApparatuses.PARROT_FLIGHT_APPARATUS);
        this.buildWingsMix(Items.SLIME_BALL, FlightApparatuses.SLIME_FLIGHT_APPARATUS);
        this.buildWingsMix(Items.DYE.blue(), FlightApparatuses.BLUE_BUTTERFLY_FLIGHT_APPARATUS);
        this.buildWingsMix(Items.DYE.orange(), FlightApparatuses.MONARCH_BUTTERFLY_FLIGHT_APPARATUS);
        this.buildWingsMix(Items.BLAZE_POWDER, FlightApparatuses.FIRE_FLIGHT_APPARATUS);
        this.buildWingsMix(Items.LEATHER, FlightApparatuses.BAT_FLIGHT_APPARATUS);
        this.buildWingsMix(Items.OXEYE_DAISY, FlightApparatuses.FAIRY_FLIGHT_APPARATUS);
        this.buildWingsMix(Items.BONE, FlightApparatuses.EVIL_FLIGHT_APPARATUS);
        this.buildWingsMix(Items.FIRE_CHARGE, FlightApparatuses.DRAGON_FLIGHT_APPARATUS);
        this.buildWingsMix(Items.IRON_INGOT, FlightApparatuses.METALLIC_FLIGHT_APPARATUS);
    }

    protected void buildWingsMix(Item reagent, ResourceKey<FlightApparatus> output) {
        Holder.Reference<FlightApparatus> outputWings = this.wings.getOrThrow(output);
        this.buildWingsMix(Potions.SLOW_FALLING, reagent, outputWings);
        this.buildWingsMix(Potions.LONG_SLOW_FALLING, reagent, outputWings);
    }

    /**
     * @see AbstractBrewingProvider#buildMix(Holder, Item, Holder)
     */
    protected void buildWingsMix(Holder<Potion> input, Item reagent, Holder<FlightApparatus> output) {
        for (Item container : this.containers) {
            this.save(wingsMix(container, input, reagent, output));
        }
    }

    /**
     * @see net.minecraft.data.recipes.BrewingRecipeBuilder#brewingMix(Item, Holder, Item, Holder)
     */
    public static BrewingRecipeBuilder wingsMix(Item container, Holder<Potion> inputPotion, Item reagentItem, Holder<FlightApparatus> outputWings) {
        PotionIngredient input = potionIngredient(container, inputPotion);
        PotionIngredient reagent = PotionIngredient.of(reagentItem);
        ItemStackTemplate output = wingsOutput(outputWings);
        return new BrewingRecipeBuilder(input, reagent, output);
    }

    /**
     * @see net.minecraft.data.recipes.BrewingRecipeBuilder#potionIngredient(Item, Holder)
     */
    private static PotionIngredient potionIngredient(Item potionContainer, Holder<Potion> potion) {
        return PotionIngredient.of(potionContainer, PotionsPredicate.ofPotion(potion));
    }

    /**
     * @see net.minecraft.data.recipes.BrewingRecipeBuilder#potionOutput(Item, Holder)
     */
    private static ItemStackTemplate wingsOutput(Holder<FlightApparatus> holder) {
        return new ItemStackTemplate(ModRegistry.BOTTLED_WINGS_ITEM,
                1,
                DataComponentPatch.builder()
                        .set(DataComponents.CONSUMABLE, BottledWingsItem.createComponent(holder))
                        .build());
    }
}
