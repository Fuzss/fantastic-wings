package fuzs.fantasticwings.commands;

import com.google.common.collect.ImmutableList;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import fuzs.fantasticwings.flight.apparatus.FlightApparatus;
import fuzs.fantasticwings.world.item.consume_effects.GrantWingsConsumeEffect;
import fuzs.fantasticwings.world.item.consume_effects.TakeWingsConsumeEffect;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collection;
import java.util.Optional;

public class WingsCommand {
    public static final String KEY_TAKE_WINGS_SINGLE = "commands.wings.take.success.single";
    public static final String KEY_TAKE_WINGS_MULTIPLE = "commands.wings.take.success.multiple";
    public static final String KEY_GIVE_WINGS_MULTIPLE = "commands.wings.give.success.multiple";
    public static final String KEY_GIVE_WINGS_SINGLE = "commands.wings.give.success.single";
    public static final MutableComponent COMPONENT_GIVE_WINGS_FAILED = Component.translatable(
            "commands.wings.give.failed");
    public static final MutableComponent COMPONENT_TAKE_WINGS_FAILED = Component.translatable(
            "commands.wings.take.failed");
    private static final SimpleCommandExceptionType ERROR_GIVE_FAILED = new SimpleCommandExceptionType(
            COMPONENT_GIVE_WINGS_FAILED);
    private static final SimpleCommandExceptionType ERROR_TAKE_FAILED = new SimpleCommandExceptionType(
            COMPONENT_TAKE_WINGS_FAILED);

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context, Commands.CommandSelection environment) {
        dispatcher.register(Commands.literal("wings")
                .requires(commandSourceStack -> commandSourceStack.hasPermission(2))
                .then(Commands.literal("give")
                        .then(Commands.argument("targets", EntityArgument.players())
                                .then(Commands.argument("wings",
                                                ResourceArgument.resource(context, FlightApparatus.REGISTRY_KEY))
                                        .executes(WingsCommand::giveWing))))
                .then(Commands.literal("take")
                        .executes(ctx -> takeWings(ctx, ImmutableList.of(ctx.getSource().getPlayerOrException())))
                        .then(Commands.argument("targets", EntityArgument.players())
                                .then(Commands.argument("wings",
                                                ResourceArgument.resource(context, FlightApparatus.REGISTRY_KEY))
                                        .executes(WingsCommand::takeSpecificWings))
                                .executes(ctx -> takeWings(ctx, EntityArgument.getPlayers(ctx, "targets"))))));
    }

    private static int giveWing(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Collection<ServerPlayer> targets = EntityArgument.getPlayers(context, "targets");
        Holder.Reference<FlightApparatus> holder = ResourceArgument.getResource(context,
                "wings",
                FlightApparatus.REGISTRY_KEY);
        int count = 0;
        for (ServerPlayer player : targets) {
            if (GrantWingsConsumeEffect.giveWings(player, holder)) {
                count++;
            }
        }
        if (count == 0) {
            throw ERROR_GIVE_FAILED.create();
        }
        if (targets.size() == 1) {
            context.getSource()
                    .sendSuccess(() -> Component.translatable(KEY_GIVE_WINGS_SINGLE,
                            targets.iterator().next().getDisplayName()), true);
        } else {
            context.getSource()
                    .sendSuccess(() -> Component.translatable(KEY_GIVE_WINGS_MULTIPLE, targets.size()), true);
        }
        return count;
    }

    private static int takeWings(CommandContext<CommandSourceStack> context, Collection<ServerPlayer> targets) throws CommandSyntaxException {
        int count = 0;
        for (ServerPlayer player : targets) {
            if (TakeWingsConsumeEffect.takeWings(player, Optional.empty())) {
                count++;
            }
        }
        if (count == 0) {
            throw ERROR_TAKE_FAILED.create();
        }
        if (targets.size() == 1) {
            context.getSource()
                    .sendSuccess(() -> Component.translatable(KEY_TAKE_WINGS_SINGLE,
                            targets.iterator().next().getDisplayName()), true);
        } else {
            context.getSource()
                    .sendSuccess(() -> Component.translatable(KEY_TAKE_WINGS_MULTIPLE, targets.size()), true);
        }
        return count;
    }

    private static int takeSpecificWings(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Collection<ServerPlayer> targets = EntityArgument.getPlayers(context, "targets");
        Holder.Reference<FlightApparatus> holder = ResourceArgument.getResource(context,
                "wings",
                FlightApparatus.REGISTRY_KEY);
        int count = 0;
        for (ServerPlayer player : targets) {
            if (TakeWingsConsumeEffect.takeWings(player, Optional.of(holder))) {
                count++;
            }
        }
        if (count == 0) {
            throw ERROR_TAKE_FAILED.create();
        }
        if (targets.size() == 1) {
            context.getSource()
                    .sendSuccess(() -> Component.translatable(KEY_TAKE_WINGS_SINGLE,
                            targets.iterator().next().getDisplayName()), true);
        } else {
            context.getSource()
                    .sendSuccess(() -> Component.translatable(KEY_TAKE_WINGS_MULTIPLE, targets.size()), true);
        }
        return count;
    }
}
