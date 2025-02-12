package net.diground.exyliaClasses.listeners;

import net.diground.exyliaClasses.ExyliaClasses;
import net.diground.exyliaClasses.models.HoldEffect;
import net.diground.exyliaClasses.models.PlayerInfo;
import net.diground.exyliaClasses.models.SpecialClass;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import static net.diground.exyliaClasses.utils.DebugUtils.logError;

public class HoldEffectListener implements Listener {
    private final ExyliaClasses plugin;
    private final Map<Player, Map<Material, BukkitRunnable>> activeTasks = new HashMap<>();

    public HoldEffectListener(ExyliaClasses plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    private void onItemHold(PlayerItemHeldEvent e) {
        Player player = e.getPlayer();

        PlayerInfo playerInfo = plugin.getPlayerInfoManager().getPlayerInfo(player);
        if (playerInfo == null) return;

        SpecialClass currentClass = playerInfo.getCurrentClass();
        if (currentClass == null) return;

        ItemStack item = player.getInventory().getItem(e.getNewSlot());
        if (item == null || item.getType() == Material.AIR) return;

        Map<String, HoldEffect> holdEffects = currentClass.getHoldEffects();
        if (holdEffects.isEmpty()) return;

        for (HoldEffect holdEffect : holdEffects.values()) {
            if (holdEffect.getMaterial() == item.getType()) {
                startHoldEffectTask(player, holdEffect);
                break;
            }
        }
    }

    private void startHoldEffectTask(Player player, HoldEffect holdEffect) {
        if (activeTasks.containsKey(player) && activeTasks.get(player).containsKey(holdEffect.getMaterial())) {
            return;
        }

        BukkitRunnable task = new BukkitRunnable() {
            @Override
            public void run() {
                if (player.getInventory().getItemInMainHand().getType() != holdEffect.getMaterial()) {
                    cancelTask(player, holdEffect.getMaterial());
                    return;
                }

                switch (holdEffect.getType()) {
                    case "SELF":
                        applyEffects(player, holdEffect);
                        break;
                    case "TO_CLAN":
                        List<Player> clanMembers = plugin.getClanManager().getClanMembersInRadius(player, holdEffect.getRadius());
                        executeEffects(player, clanMembers, holdEffect, false);
                        break;
                    case "TO_ALLY":
                        List<Player> allyMembers = plugin.getClanManager().getAllyMembersInRadius(player, holdEffect.getRadius());
                        executeEffects(player, allyMembers, holdEffect, false);
                        break;
                    case "TO_ENEMY":
                        List<Player> enemyMembers = plugin.getClanManager().getEnemyMembersInRadius(player, holdEffect.getRadius());
                        executeEffects(player, enemyMembers, holdEffect, true);
                        break;
                    case "TO_CLAN_AND_ALLY":
                        List<Player> clanAndAllyMembers = plugin.getClanManager().getClanAndAllyMembersInRadius(player, holdEffect.getRadius());
                        executeEffects(player, clanAndAllyMembers, holdEffect, false);
                        break;
                    default:
                        logError("Error in the holdEffect type " + holdEffect.getType());
                        break;
                }
            }
        };

        activeTasks.computeIfAbsent(player, k -> new HashMap<>()).put(holdEffect.getMaterial(), task);

        task.runTaskTimer(plugin, 0L, 20L);
    }

    private void cancelTask(Player player, Material material) {
        if (activeTasks.containsKey(player) && activeTasks.get(player).containsKey(material)) {
            BukkitRunnable task = activeTasks.get(player).remove(material);
            if (task != null) {
                task.cancel();
            }
        }
    }

    private void executeEffects(Player executer, List<Player> players, HoldEffect holdEffect, boolean enemy) {
        for (Player p : players) {
            if (enemy && executer.getUniqueId().equals(p.getUniqueId())) {
                continue;
            }

            applyEffects(p, holdEffect);
        }
    }

    private void applyEffects(Player player, HoldEffect holdEffect) {
        for (PotionEffect effect : holdEffect.getEffects()) {
            player.addPotionEffect(effect);
        }
    }
}
