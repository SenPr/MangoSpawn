package me.sen2y.mangoSpawn.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import me.sen2y.mangoSpawn.MangoSpawn;
import me.sen2y.mangoSpawn.utils.Permissions;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetSpawnCommand {

    public static LiteralCommandNode<CommandSourceStack> createCommand() {
        return Commands.literal("setspawn")
                .requires(sender -> sender.getSender().hasPermission(Permissions.SET_SPAWN))
                .executes(ctx -> {
                    CommandSender sender = ctx.getSource().getSender();
                    if (!(sender instanceof Player player)) {
                        sender.sendMessage("You are not a player, you cannot use this command");
                        return Command.SINGLE_SUCCESS;
                    }
                    MangoSpawn.getInstance().saveSpawn(player.getLocation());
                    player.sendMessage("Spawn saved!");
                    return Command.SINGLE_SUCCESS;
                })
                .build();
    }

}
