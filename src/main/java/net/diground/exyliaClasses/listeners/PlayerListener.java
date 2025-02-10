package net.diground.exyliaClasses.listeners;

import net.diground.exyliaClasses.ExyliaClasses;
import net.diground.exyliaClasses.models.PlayerInfo;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerListener implements Listener {

    private final ExyliaClasses plugin;

    public PlayerListener(ExyliaClasses plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        plugin.getClassManager().addPlayerInfo(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerJoinEvent event) {
        plugin.getClassManager().removePlayerInfo(event.getPlayer());
    }
}
