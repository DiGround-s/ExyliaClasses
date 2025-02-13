package net.diground.exyliaClasses.commands;

import net.diground.exyliaClasses.ExyliaClasses;
import net.diground.exyliaClasses.commands.subcommands.Reload;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MainCommand implements CommandExecutor, TabCompleter {
    private final ExyliaClasses plugin;

    public MainCommand(ExyliaClasses plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length == 0) {
            return true;
        }

        String action = args[0].toLowerCase();

        switch (action) {
            case "reload":
                return new Reload(plugin).handleReloadCommand(commandSender, args);
            default:
                return true;
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return List.of("reload");
    }
}
