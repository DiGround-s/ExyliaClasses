package net.diground.exyliaClasses.listeners;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import net.diground.exyliaClasses.ExyliaClasses;
import net.diground.exyliaClasses.models.PlayerInfo;
import net.diground.exyliaClasses.models.SpecialClass;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class ArmorListener implements Listener {
    private final ExyliaClasses plugin;

    public ArmorListener(ExyliaClasses plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    private void onArmorEquip(PlayerArmorChangeEvent event) {
        if (event.getOldItem() != null && event.getNewItem() != null) {
            if (event.getOldItem().getType() == event.getNewItem().getType()) {
                boolean oldHasModel = event.getOldItem().hasItemMeta()
                        && event.getOldItem().getItemMeta() != null
                        && event.getOldItem().getItemMeta().hasCustomModelData();

                boolean newHasModel = event.getNewItem().hasItemMeta()
                        && event.getNewItem().getItemMeta() != null
                        && event.getNewItem().getItemMeta().hasCustomModelData();

                if (oldHasModel == newHasModel) {
                    return;
                }
            }
        }

        Player player = event.getPlayer();
        plugin.getSpecialClassManager().handleClassCheck(player);
    }


    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        PlayerInfo playerInfo = plugin.getPlayerInfoManager().getPlayerInfo(player);

        SpecialClass currentClass = playerInfo.getCurrentClass();
        if (currentClass != null) {
            plugin.getSpecialClassManager().cancelClass(player, currentClass);
            playerInfo.setCurrentClass(null);
        }
    }
}
