package net.diground.exyliaClasses.listeners.classes;

import net.diground.exyliaClasses.ExyliaClasses;
import net.diground.exyliaClasses.models.EClass;
import net.diground.exyliaClasses.models.PlayerInfo;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

import static net.diground.exyliaClasses.utils.ItemUtils.consumeItem;

public abstract class AbstractClassListener implements Listener {

    protected final ExyliaClasses plugin;
    protected final EClass eClass;

    public AbstractClassListener(ExyliaClasses plugin, String className) {
        this.plugin = plugin;
        this.eClass = plugin.getClassManager().getClassByName(className);
    }

    @EventHandler
    private void onRightClick(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
            Player player = event.getPlayer();
            PlayerInfo playerInfo = plugin.getClassManager().getPlayerInfo(player);
            if (playerInfo != null && playerInfo.getPlayerClass() != null && playerInfo.getPlayerClass().getName().equals(eClass.getName())) {
                Material material = event.getMaterial();
                for (String activeEffect : getActiveEffects()) {
                    applyEffect(player, material, activeEffect);
                }
            }
        }
    }

    protected abstract List<String> getActiveEffects();

    private void applyEffect(Player player, Material material, String activeEffect) {
        String[] effect = activeEffect.split(":");
        Material materialType = Material.valueOf(effect[0]);
        PotionEffectType effectType = PotionEffectType.getByName(effect[1]);
        int duration = Integer.parseInt(effect[2]) * 20;
        int amplifier = Integer.parseInt(effect[3]);
        double cooldown = Double.parseDouble(effect[4]);
        if (duration == -20) {
            duration = -1;
        }
        if (materialType == material) {
            if (plugin.getCooldownManager().isCooldownActive(player, material, eClass)) {
                player.sendMessage("Aún no puedes aplicar este efecto, debes esperar " + plugin.getCooldownManager().getRemainingCooldown(player, material, eClass) + " segundos");
                return;
            }

            assert effectType != null;
            player.addPotionEffect(new PotionEffect(effectType, duration, amplifier));
            plugin.getCooldownManager().addCooldown(player, material, eClass, cooldown);
            player.sendMessage("Has aplicado el efecto " + effectType.getName() + " a ti.");

            // Verificar tanto la mano principal como la secundaria
            ItemStack itemInMainHand = player.getInventory().getItemInMainHand();
            ItemStack itemInOffHand = player.getInventory().getItemInOffHand();

            if (itemInMainHand.getType() == material) {
                consumeItem(player, itemInMainHand, true); // Reducir en la mano principal
            } else if (itemInOffHand.getType() == material) {
                consumeItem(player, itemInOffHand, false); // Reducir en la mano secundaria
            }
        }
    }
}
