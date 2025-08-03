package me.sen2y.mangoSpawn.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import me.sen2y.mangoSpawn.MangoSpawn;
import me.sen2y.mangoSpawn.utils.Permissions;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCommand {
    public static LiteralCommandNode<CommandSourceStack> createCommand() {
        return Commands.literal("spawn")
                .requires(sender -> sender.getSender().hasPermission(Permissions.USE_SPAWN))
                .executes(ctx -> {
                    CommandSender sender = ctx.getSource().getSender();
                    if (!(sender instanceof Player player)) {
                        sender.sendMessage(MangoSpawn.getInstance().getMessageManager().get("not-player"));
                        return Command.SINGLE_SUCCESS;
                    }
                    SpawnCommand.teleportSpawn(player);
                    return Command.SINGLE_SUCCESS;
                })
                .then(Commands.argument("target", ArgumentTypes.player())
                        .requires(sender -> sender.getSender().hasPermission(Permissions.USE_SPAWN_OTHERS))
                        .executes(SpawnCommand::teleportOtherLogic)
                )
                .build();
    }

    private static void teleportSpawn(Player player) {
        if (MangoSpawn.getInstance().getSpawn() == null) {
            player.sendMessage(MangoSpawn.getInstance().getMessageManager().get("spawn-unknown"));
            return;
        }
        player.sendMessage(MangoSpawn.getInstance().getMessageManager().get("teleporting"));
        player.teleport(MangoSpawn.getInstance().getSpawn());
    }

    private static int teleportOtherLogic(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        PlayerSelectorArgumentResolver targetResolver = ctx.getArgument("target", PlayerSelectorArgumentResolver.class);
        Player target = targetResolver.resolve(ctx.getSource()).getFirst();

        CommandSender sender = ctx.getSource().getSender();
        sender.sendMessage(MangoSpawn.getInstance().getMessageManager().get("teleport-other"));
        teleportSpawn(target);
        return Command.SINGLE_SUCCESS;
    }
}
