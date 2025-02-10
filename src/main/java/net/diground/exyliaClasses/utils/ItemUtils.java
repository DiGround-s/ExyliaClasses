package net.diground.exyliaClasses.utils;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ItemUtils {
    public static void consumeItem(Player player, ItemStack item, boolean isMainHand) {
        int newAmount = item.getAmount() - 1;
        if (newAmount <= 0) {
            if (isMainHand) {
                player.getInventory().setItemInMainHand(null); // Eliminar el ítem si no quedan más en la mano principal
            } else {
                player.getInventory().setItemInOffHand(null); // Eliminar el ítem si no quedan más en la mano secundaria
            }
        } else {
            item.setAmount(newAmount); // Actualizar la cantidad del ítem
        }
    }
}
