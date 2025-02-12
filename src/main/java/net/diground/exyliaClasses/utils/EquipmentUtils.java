package net.diground.exyliaClasses.utils;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Map;

public class EquipmentUtils {

    public static boolean equipmentMatches(Player player, Map<String, Material> equipment) {
        return (equipment.get("helmet") == null || player.getInventory().getHelmet() != null && player.getInventory().getHelmet().getType() == equipment.get("helmet"))
                && (equipment.get("chestplate") == null || player.getInventory().getChestplate() != null && player.getInventory().getChestplate().getType() == equipment.get("chestplate"))
                && (equipment.get("leggings") == null || player.getInventory().getLeggings() != null && player.getInventory().getLeggings().getType() == equipment.get("leggings"))
                && (equipment.get("boots") == null || player.getInventory().getBoots() != null && player.getInventory().getBoots().getType() == equipment.get("boots"));
    }
}
