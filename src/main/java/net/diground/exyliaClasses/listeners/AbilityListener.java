package net.diground.exyliaClasses.listeners;

import net.diground.exyliaClasses.ExyliaClasses;
import net.diground.exyliaClasses.managers.CooldownManager;
import net.diground.exyliaClasses.models.Ability;
import net.diground.exyliaClasses.models.HoldEffect;
import net.diground.exyliaClasses.models.PlayerInfo;
import net.diground.exyliaClasses.models.SpecialClass;
import net.diground.exyliaClasses.utils.ColorUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.List;
import java.util.Map;

import static net.diground.exyliaClasses.utils.DebugUtils.logError;
import static net.diground.exyliaClasses.utils.EffectUtils.playParticle;
import static net.diground.exyliaClasses.utils.EffectUtils.playSound;

public class AbilityListener implements Listener {
    private final ExyliaClasses plugin;

    public AbilityListener(ExyliaClasses plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    private void onItemUse(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        PlayerInfo playerInfo = plugin.getPlayerInfoManager().getPlayerInfo(player);

        if (playerInfo == null) return;

        SpecialClass currentClass = playerInfo.getCurrentClass();
        if (currentClass == null) return;

        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType() == Material.AIR) return;

        Map<String, Ability> abilities = currentClass.getAbilities();
        if (abilities.isEmpty()) return;

        for (Ability ability : abilities.values()) {
            if (ability.getMaterial() != item.getType()) continue;

            Action action = e.getAction();
            List<String> validActions = ability.getActions();
            if (!validActions.contains(action.name())) continue;

            if (!handleNecessary(player, ability)) return;

            ColorUtils.sendPlayerMessage(player, plugin.getConfigManager().getMessage("abilities.used", "%prefix% <#8fffc1>¡Haz utilizado la habilidad %ability_display_name%&r<#8fffc1>!")
                    .replace("%ability_display_name%", ability.getDisplayName()));
            switch (ability.getType()) {
                case "SELF":
                    applyEffects(player, ability);
                    playParticle(player, ability.getParticles());
                    playSound(player, ability.getSound());
                    break;
                case "TO_CLAN":
                    List<Player> clanMembers = plugin.getClanManager().getClanMembersInRadius(player, ability.getRadius());
                    executeEffects(player, clanMembers, ability, false);
                    break;
                case "TO_ALLY":
                    List<Player> allyMembers = plugin.getClanManager().getAllyMembersInRadius(player, ability.getRadius());
                    executeEffects(player, allyMembers, ability, false);
                    break;
                case "TO_ENEMY":
                    List<Player> enemyMembers = plugin.getClanManager().getEnemyMembersInRadius(player, ability.getRadius());
                    executeEffects(player, enemyMembers, ability, true);
                    break;
                case "TO_CLAN_AND_ALLY":
                    List<Player> clanAndAllyMembers = plugin.getClanManager().getClanAndAllyMembersInRadius(player, ability.getRadius());
                    executeEffects(player, clanAndAllyMembers, ability, false);
                    break;
                default:
                    logError("The ability type " + ability.getType() + " in " + ability.getDisplayName());
                    break;
            }
        }
    }

    private void executeEffects(Player executer, List<Player> players, Ability ability, boolean enemy) {
        for (Player p : players) {
            if (enemy && executer.getUniqueId().equals(p.getUniqueId())) {
                continue;
            }

            applyEffects(p, ability);
            playParticle(p, ability.getParticles());
            playSound(p, ability.getSound());

            if (enemy) {
                ColorUtils.sendPlayerMessage(p, plugin.getConfigManager().getMessage("abilities.enemy_informate",
                                "%prefix% <#a33b53>Obtuviste %ability_display_name%&r<#a33b53> por culpa de <#aa76de>%clan_player_name%&r<#a33b53>!")
                        .replace("%ability_display_name%", ability.getDisplayName())
                        .replace("%clan_player_name%", executer.getName()));
            } else if (!executer.getUniqueId().equals(p.getUniqueId())) {
                ColorUtils.sendPlayerMessage(p, plugin.getConfigManager().getMessage("abilities.clan_informate",
                                "%prefix% <#8fffc1>Obtuviste %ability_display_name%&r<#8fffc1> gracias a <#aa76de>%clan_player_name%&r<#8fffc1>!")
                        .replace("%ability_display_name%", ability.getDisplayName())
                        .replace("%clan_player_name%", executer.getName()));
            }
        }
    }



    private boolean handleNecessary(Player player, Ability ability) {
        CooldownManager cooldownManager = plugin.getCooldownManager();
        boolean isActive = cooldownManager.isCooldownActive(player, ability);
        if (isActive) {
            double remainingSeconds = cooldownManager.getRemainingCooldown(player, ability);
            ColorUtils.sendPlayerMessage(player, plugin.getConfigManager().getMessage("abilities.cooldown", "%prefix% <#ffc58f>¡Tu habilidad %ability_display_name%&r<#ffc58f> aún está en enfriamiento, <#aa76de>%time%s restantes<#ffc58f>!")
                    .replace("%ability_display_name%", ability.getDisplayName())
                    .replace("%time%", remainingSeconds + ""));
            return false;
        }

        PlayerInfo playerInfo = plugin.getPlayerInfoManager().getPlayerInfo(player);
        double actualEnergy = playerInfo.getActualEnergy();

        if (actualEnergy >= ability.getEnergyCost()) {
            cooldownManager.addCooldown(player, ability, ability.getCooldown());
            playerInfo.setActualEnergy(playerInfo.getActualEnergy() - ability.getEnergyCost());
            return true;
        } else {
            ColorUtils.sendPlayerMessage(player, plugin.getConfigManager().getMessage("energy.not_enough", "%prefix% <#a33b53>No tienes suficiente energía para usar %ability_display_name%&r<#a33b53>, tienes <#8fffc1>%actual_energy% ⚡ <#a33b53>y necesitas <#ffc58f>%needed_energy% ⚡<#a33b53>.")
                    .replace("%ability_display_name%", ability.getDisplayName())
                    .replace("%actual_energy%", actualEnergy + "")
                    .replace("%needed_energy%", ability.getEnergyCost() + ""));
            return false;
        }
    }



    private void applyEffects(Player player, Ability ability) {
        for (PotionEffect effect : ability.getEffects()) {
            player.addPotionEffect(effect);
        }
    }

}
