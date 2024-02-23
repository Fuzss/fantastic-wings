package fuzs.fantasticwings.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import fuzs.fantasticwings.flight.apparatus.FlightApparatusImpl;
import fuzs.fantasticwings.world.effect.WingsMobEffect;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collection;

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
                                .then(Commands.argument("wings", WingsArgument.wings())
                                        .executes(WingsCommand::giveWing))))
                .then(Commands.literal("take")
                        .then(Commands.argument("targets", EntityArgument.players())
                                .then(Commands.argument("wings", WingsArgument.wings())
                                        .executes(WingsCommand::takeSpecificWings))
                                .executes(WingsCommand::takeWings))));
    }

    private static int giveWing(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        Collection<ServerPlayer> targets = EntityArgument.getPlayers(ctx, "targets");
        FlightApparatusImpl wings = WingsArgument.getWings(ctx, "wings");
        int count = 0;
        for (ServerPlayer player : targets) {
            if (WingsMobEffect.giveWings(player, wings.holder())) {
                count++;
            }
        }
        if (count == 0) {
            throw ERROR_GIVE_FAILED.create();
        }
        if (targets.size() == 1) {
            ctx.getSource()
                    .sendSuccess(() -> Component.translatable(KEY_GIVE_WINGS_SINGLE,
                            targets.iterator().next().getDisplayName()
                    ), true);
        } else {
            ctx.getSource().sendSuccess(() -> Component.translatable(KEY_GIVE_WINGS_MULTIPLE, targets.size()), true);
        }
        return count;
    }

    private static int takeWings(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        Collection<ServerPlayer> targets = EntityArgument.getPlayers(ctx, "targets");
        int count = 0;
        for (ServerPlayer player : targets) {
            if (WingsMobEffect.takeWings(player)) {
                count++;
            }
        }
        if (count == 0) {
            throw ERROR_TAKE_FAILED.create();
        }
        if (targets.size() == 1) {
            ctx.getSource()
                    .sendSuccess(() -> Component.translatable(KEY_TAKE_WINGS_SINGLE,
                            targets.iterator().next().getDisplayName()
                    ), true);
        } else {
            ctx.getSource().sendSuccess(() -> Component.translatable(KEY_TAKE_WINGS_MULTIPLE, targets.size()), true);
        }
        return count;
    }

    private static int takeSpecificWings(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        Collection<ServerPlayer> targets = EntityArgument.getPlayers(ctx, "targets");
        FlightApparatusImpl wings = WingsArgument.getWings(ctx, "wings");
        int count = 0;
        for (ServerPlayer player : targets) {
            if (WingsMobEffect.takeWings(player, wings.holder())) {
                count++;
            }
        }
        if (count == 0) {
            throw ERROR_TAKE_FAILED.create();
        }
        if (targets.size() == 1) {
            ctx.getSource()
                    .sendSuccess(() -> Component.translatable(KEY_TAKE_WINGS_SINGLE,
                            targets.iterator().next().getDisplayName()
                    ), true);
        } else {
            ctx.getSource().sendSuccess(() -> Component.translatable(KEY_TAKE_WINGS_MULTIPLE, targets.size()), true);
        }
        return count;
    }
}
