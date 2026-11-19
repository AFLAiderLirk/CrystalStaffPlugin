package me.aiderlirk.crystalstaff.command;

import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import me.aiderlirk.crystalstaff.item.CrystalStaffItem;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;

public final class CrystalStaffCommand {

    private static final MiniMessage MINI_MESSAGE =
            MiniMessage.miniMessage();

    private CrystalStaffCommand() {
    }

    public static LiteralCommandNode<CommandSourceStack> create() {
        return Commands.literal("crystalstaff")
                .then(Commands.literal("give")
                        .requires(source -> source.getSender()
                                .hasPermission("crystalstaff.give"))
                        .executes(context -> {
                            if (!(context.getSource().getSender() instanceof Player player)) {
                                context.getSource().getSender().sendMessage(
                                        MINI_MESSAGE.deserialize(
                                                "<red>Эта команда доступна только игроку."
                                        )
                                );
                                return 0;
                            }

                            player.getInventory().addItem(
                                    CrystalStaffItem.create()
                            );

                            player.sendMessage(
                                    MINI_MESSAGE.deserialize(
                                            "<green>Ты получил Кристальный посох!"
                                    )
                            );

                            return 1;
                        }))
                .build();
    }
}
