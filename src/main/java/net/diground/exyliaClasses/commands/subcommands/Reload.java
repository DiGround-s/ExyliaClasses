package net.diground.exyliaClasses.commands.subcommands;

import net.diground.exyliaClasses.ExyliaClasses;
import net.diground.exyliaClasses.models.SpecialClass;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Reload {
    private final ExyliaClasses plugin;

    public Reload(ExyliaClasses plugin) {
        this.plugin = plugin;
    }

    public boolean handleReloadCommand(CommandSender sender, String[] args) {
        for (Player p : plugin.getServer().getOnlinePlayers()) {
            SpecialClass currentClass = plugin.getPlayerInfoManager().getPlayerInfo(p).getCurrentClass();
            if (currentClass != null) {
                plugin.getSpecialClassManager().cancelClass(p, currentClass);
            }
        }

        plugin.getSpecialClassLoader().reloadClasses();
        plugin.getConfigManager().reloadAllConfigs();
        for (Player p : plugin.getServer().getOnlinePlayers()) {
            plugin.getSpecialClassManager().handleClassCheck(p);
        }
        return true;
    }

}
