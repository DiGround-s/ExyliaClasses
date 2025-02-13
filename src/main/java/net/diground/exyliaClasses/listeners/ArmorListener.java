package net.diground.exyliaClasses.listeners;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import net.diground.exyliaClasses.ExyliaClasses;
import net.diground.exyliaClasses.models.PlayerInfo;
import net.diground.exyliaClasses.models.SpecialClass;
import net.diground.exyliaClasses.utils.ColorUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Map;
import java.util.Objects;

import static net.diground.exyliaClasses.utils.EquipmentUtils.equipmentMatches;

public class ArmorListener implements Listener {
    private final ExyliaClasses plugin;

    public ArmorListener(ExyliaClasses plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    private void onArmorEquip(PlayerArmorChangeEvent event) {
        if (event.getOldItem() != null && event.getNewItem() != null) {
            if (event.getOldItem().getType() == event.getNewItem().getType() &&
                    event.getOldItem().getItemMeta().hasCustomModelData() == event.getNewItem().getItemMeta().hasCustomModelData()) {
                return;
            }
        }

        Player player = event.getPlayer();
        PlayerInfo playerInfo = plugin.getPlayerInfoManager().getPlayerInfo(player);

        SpecialClass currentClass = playerInfo.getCurrentClass();

        SpecialClass matchedClass = null;
        for (SpecialClass specialClass : plugin.getSpecialClassManager().getClassMap().values()) {
            Map<String, Material> equipment = specialClass.getEquipment();
            if (equipmentMatches(player, equipment)) {
                matchedClass = specialClass;
                break;
            }
        }

        if (matchedClass != null && matchedClass != currentClass) {
            if (!Objects.equals(matchedClass.getPermission(), "NONE")) {
                if (!player.hasPermission(matchedClass.getPermission())){
                    ColorUtils.sendPlayerMessage(player, plugin.getConfigManager().getMessage("no_permission", "%prefix% <#a33b53>¡No tienes permisos para usar la clase %class_display_name%&r<#a33b53>!")
                            .replace("%class_display_name%", matchedClass.getDisplayName()));
                    return;
                }
            }
            plugin.getWarmupManager().startNewClassWarmup(player, matchedClass);

        } else if (matchedClass == null) {
            if (currentClass != null) {
                plugin.getSpecialClassManager().cancelClass(player, currentClass);
                playerInfo.setCurrentClass(null);
            } else {
                plugin.getWarmupManager().cancelNewClassWarmup(player);
                playerInfo.setCurrentClass(null);
            }
        }
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
