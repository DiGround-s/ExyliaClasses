package net.diground.exyliaClasses.managers;

import net.diground.exyliaClasses.models.Ability;
import org.bukkit.entity.Player;

import java.util.*;

public class CooldownManager {
    private final Map<UUID, Map<String, Long>> cooldowns;

    private final Set<UUID> activeCooldowns;

    public CooldownManager() {
        this.cooldowns = new HashMap<>();
        this.activeCooldowns = new HashSet<>();
    }

    public void addCooldown(Player player, Ability ability, double cooldown) {
        long cooldownMillis = (long) (cooldown * 1000);
        long expirationTime = System.currentTimeMillis() + cooldownMillis;

        cooldowns.computeIfAbsent(player.getUniqueId(), k -> new HashMap<>()).put(ability.getId(), expirationTime);

        if (ability.getMaterial() != null) {
            player.setCooldown(ability.getMaterial(), (int) cooldown * 20);
        }

        activeCooldowns.add(player.getUniqueId());
    }

    public boolean isCooldownActive(Player player, Ability ability) {
        Map<String, Long> playerCooldowns = cooldowns.get(player.getUniqueId());
        if (playerCooldowns == null) {
            return false;
        }

        Long expirationTime = playerCooldowns.get(ability.getId());
        if (expirationTime == null) {
            return false;
        }

        return System.currentTimeMillis() < expirationTime;
    }

    public double getRemainingCooldown(Player player, Ability ability) {
        Map<String, Long> playerCooldowns = cooldowns.get(player.getUniqueId());
        if (playerCooldowns == null) {
            return 0;
        }

        Long expirationTime = playerCooldowns.get(ability.getId());
        if (expirationTime == null) {
            return 0;
        }

        long currentTime = System.currentTimeMillis();
        long remainingMillis = expirationTime - currentTime;

        double remainingSeconds = remainingMillis > 0 ? remainingMillis / 1000.0 : 0;

        return Math.round(remainingSeconds * 10) / 10.0;
    }

    public void removeCooldown(Player player, Ability ability) {
        Map<String, Long> playerCooldowns = cooldowns.get(player.getUniqueId());
        if (playerCooldowns != null) {
            playerCooldowns.remove(ability.getId());
            if (playerCooldowns.isEmpty()) {
                cooldowns.remove(player.getUniqueId());
                activeCooldowns.remove(player.getUniqueId());

                if (ability.getMaterial() != null) {
                    player.setCooldown(ability.getMaterial(), 1);
                }
            }
        }
    }


    public void setCooldown(Player player, Ability ability, double cooldown) {
        long cooldownMillis = (long) (cooldown * 1000);
        long expirationTime = System.currentTimeMillis() + cooldownMillis;
        cooldowns.computeIfAbsent(player.getUniqueId(), k -> new HashMap<>()).put(ability.getId(), expirationTime);

        if (ability.getMaterial() != null) {
            player.setCooldown(ability.getMaterial(), (int) cooldown * 20);
        }
    }

    public List<String> getItemsInCooldown(Player player) {
        List<String> itemsInCooldown = new ArrayList<>();
        Map<String, Long> playerCooldowns = cooldowns.get(player.getUniqueId());

        if (playerCooldowns != null) {
            long currentTime = System.currentTimeMillis();

            for (Map.Entry<String, Long> entry : playerCooldowns.entrySet()) {
                String itemName = entry.getKey();
                Long expirationTime = entry.getValue();

                if (currentTime < expirationTime) {
                    itemsInCooldown.add(itemName);
                }
            }
        }

        return itemsInCooldown;
    }

    public Set<UUID> getActiveCooldowns() {
        return activeCooldowns;
    }
}
