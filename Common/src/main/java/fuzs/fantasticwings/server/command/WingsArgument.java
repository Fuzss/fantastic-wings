package fuzs.fantasticwings.server.command;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import fuzs.fantasticwings.FantasticWings;
import fuzs.fantasticwings.server.flight.apparatus.FlightApparatusImpl;
import fuzs.puzzleslib.api.config.v3.serialization.KeyedValueProvider;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class WingsArgument implements ArgumentType<FlightApparatusImpl> {
    private static final Collection<String> EXAMPLES = Stream.of(FlightApparatusImpl.ANGEL, FlightApparatusImpl.DRAGON)
            .map(FlightApparatusImpl::id)
            .toList();
    private static final DynamicCommandExceptionType ERROR_UNKNOWN_WING = new DynamicCommandExceptionType(component -> Component.translatable(
            "wings.wingsNotFound",
            component
    ));
    private static final KeyedValueProvider<FlightApparatusImpl> VALUE_PROVIDER = KeyedValueProvider.enumConstants(
            FlightApparatusImpl.class,
            FantasticWings.MOD_ID
    );

    private WingsArgument() {
        // NO-OP
    }

    public static WingsArgument wings() {
        return new WingsArgument();
    }

    public static FlightApparatusImpl getWings(CommandContext<CommandSourceStack> ctx, String value) throws CommandSyntaxException {
        return ctx.getArgument(value, FlightApparatusImpl.class);
    }

    @Override
    public FlightApparatusImpl parse(StringReader reader) throws CommandSyntaxException {
        ResourceLocation key = ResourceLocation.read(reader);
        return VALUE_PROVIDER.getValue(key).orElseThrow(() -> ERROR_UNKNOWN_WING.create(key));
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> ctx, SuggestionsBuilder builder) {
        return SharedSuggestionProvider.suggestResource(VALUE_PROVIDER.stream().map(Map.Entry::getKey).toList(),
                builder
        );
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }
}
