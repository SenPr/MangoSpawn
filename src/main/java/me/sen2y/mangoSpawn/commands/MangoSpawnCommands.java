package me.sen2y.mangoSpawn.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import me.sen2y.mangoSpawn.MangoSpawn;
import me.sen2y.mangoSpawn.utils.Permissions;
import org.bukkit.entity.Player;

public class MangoSpawnCommands {

    private static final LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal("mangospawn")
            .requires(sender -> sender.getSender().hasPermission(Permissions.ADMIN));

    private static final LiteralArgumentBuilder<CommandSourceStack> reload = Commands.literal("reload")
            .executes(ctx -> {

                ctx.getSource().getSender().sendMessage("Reloaded MangoSpawn");
                return Command.SINGLE_SUCCESS;
            });

    private static final LiteralArgumentBuilder<CommandSourceStack> back = Commands.literal("back")
            .then(Commands.argument("target", ArgumentTypes.player()))
            .executes(ctx -> {

                PlayerSelectorArgumentResolver targetResolver = ctx.getArgument("target", PlayerSelectorArgumentResolver.class);
                Player target = targetResolver.resolve(ctx.getSource()).getFirst();

                if (!MangoSpawn.getInstance().getLastLocationManager()
                        .teleportBack(target.getUniqueId())) {
                    ctx.getSource().getSender().sendMessage("Did not send player back, bypassed or no saved location.");
                } else {
                    ctx.getSource().getSender().sendMessage("Sent " + target.getName() + " to last location.");
                }
                return Command.SINGLE_SUCCESS;
            });

    public static LiteralCommandNode<CommandSourceStack> createCommand() {
        return root.then(reload).then(back).build();
    }


}
