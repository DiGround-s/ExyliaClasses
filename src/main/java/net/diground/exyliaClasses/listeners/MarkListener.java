package net.diground.exyliaClasses.listeners;

import net.diground.exyliaClasses.ExyliaClasses;
import net.diground.exyliaClasses.managers.ConfigManager;
import net.diground.exyliaClasses.models.*;
import net.diground.exyliaClasses.models.weapons.MarkEffect;
import net.diground.exyliaClasses.models.weapons.MarkWeapon;
import net.diground.exyliaClasses.utils.ColorUtils;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static net.diground.exyliaClasses.loaders.WeaponLoader.getMarkWeapons;
import static net.diground.exyliaClasses.utils.EffectUtils.playParticle;
import static net.diground.exyliaClasses.utils.EffectUtils.playSound;

public class MarkListener implements Listener {
    private final ExyliaClasses plugin;
    private final Map<Player, BossBar> markedBossbars = new HashMap<>();
    private final Map<UUID, BukkitTask> markRemovalTasks = new HashMap<>();
    private final Map<UUID, Double> markTimers = new HashMap<>();

    public MarkListener(ExyliaClasses plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onArrowHit(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Arrow arrow)) return;
        if (!(arrow.getShooter() instanceof Player player)) return;
        if (event.isCancelled()) return;
        PlayerInfo playerInfo = plugin.getPlayerInfoManager().getPlayerInfo(player);
        if (playerInfo == null) return;
        SpecialClass currentClass = playerInfo.getCurrentClass();
        if (currentClass == null) return;
        MarkWeapon markWeapon = null;
        for (Weapon weapon : currentClass.getWeapons().values()) {
            if (weapon instanceof MarkWeapon mw && Objects.equals(mw.getMaterial(), "ARROW")) {
                markWeapon = mw;
                break;
            }
        }

        if (markWeapon == null) {
            return;
        }
        Entity hitEntity = event.getEntity();
        if (!(hitEntity instanceof Player target)) return;

        if (!markWeapon.isMarkSameClass()) {
            PlayerInfo targetInfo = plugin.getPlayerInfoManager().getPlayerInfo(target);
            if (targetInfo != null && targetInfo.getCurrentClass() != null) {
                if (playerInfo.getCurrentClass() != null &&
                        Objects.equals(targetInfo.getCurrentClass().getId(), playerInfo.getCurrentClass().getId())) {
                    ColorUtils.sendPlayerMessage(player, plugin.getConfigManager().getMessage(
                            "weapons.mark.same_class",
                            "%prefix% <#a33b53>No puedes marcar a un jugador con tu misma clase."
                    ));
                    playSound(player, markWeapon.getSameClassErrorSound());
                    return;
                }
            }
        }

        applyMarkEffects(player, target, markWeapon);
    }

    private void applyMarkEffects(Player player, Player target, MarkWeapon markWeapon) {
        Map<Integer, MarkEffect> marks = markWeapon.getMarks();

        int maxMarkNumber = marks.keySet().stream().max(Integer::compareTo).orElse(0);
        int currentMarks = plugin.getPlayerInfoManager().getPlayerInfo(target).getMarks();

        if (currentMarks >= maxMarkNumber) {
            resetMarkTimer(player, target, markWeapon);
            return;
        }

        plugin.getPlayerInfoManager().getPlayerInfo(target).setMarks(currentMarks + 1);
        resetMarkTimer(player, target, markWeapon);
    }

    private void resetMarkTimer(Player player, Player target, MarkWeapon markWeapon) {
        ConfigManager cfgMan = plugin.getConfigManager();
        FileConfiguration cfg = cfgMan.getConfig();
        UUID targetUUID = target.getUniqueId();

        if (markRemovalTasks.containsKey(targetUUID)) {
            markRemovalTasks.get(targetUUID).cancel();
        }

        double time = markWeapon.getMarkDuration();
        if (markWeapon.isGiveGlow()) {
            target.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, (int) time * 20, 1, false, false, false));
        }

        markTimers.put(targetUUID, time);

        if (cfg.getBoolean("weapons.mark.bossbar.enabled")) {
            createMarkedBossbar(cfg, target);
        }

        PlayerInfo targetInfo = plugin.getPlayerInfoManager().getPlayerInfo(target);
        if (targetInfo == null) return;
        int currentMarks = targetInfo.getMarks();
        MarkEffect currentMarkEffect = markWeapon.getMarks().get(currentMarks);

        if (currentMarkEffect == null) return;

        if (currentMarkEffect.isRemoveInvisibility()) {
            target.removePotionEffect(PotionEffectType.INVISIBILITY);
        }
        playSound(target, currentMarkEffect.getAttackedSound());
        playSound(player, currentMarkEffect.getAttackerSound());
        playParticle(target, currentMarkEffect.getAttackedParticles());
        playParticle(player, currentMarkEffect.getAttackerParticles());
        for (PotionEffect potionEffect : currentMarkEffect.getAttackedEffects()) {
            target.addPotionEffect(potionEffect);
        }
        for (PotionEffect potionEffect : currentMarkEffect.getAttackerEffects()) {
            player.addPotionEffect(potionEffect);
        }
        ColorUtils.sendPlayerMessage(player, cfgMan.getMessage("weapons.mark.mark", "")
                .replace("%target_name%", target.getName())
                .replace("%extra_damage%", currentMarkEffect.getExtraDamage()+""));
        ColorUtils.sendPlayerMessage(target, cfgMan.getMessage("weapons.mark.marked", "")
                .replace("%attacker_name%", player.getName())
                .replace("%extra_damage%", currentMarkEffect.getExtraDamage()+""));

        BukkitTask newTask = Bukkit.getScheduler().runTaskTimer(plugin, new Runnable() {
            double timeRemaining = time;

            @Override
            public void run() {
                if (!markTimers.containsKey(targetUUID)) {
                    cancelTask(target);
                    return;
                }

                timeRemaining = markTimers.get(targetUUID);
                BossBar bossbar = markedBossbars.get(target);
                if (bossbar != null) {
                    double maxTime = markWeapon.getMarkDuration();
                    float progress = (float) (timeRemaining / maxTime);
                    bossbar.progress(Math.max(0.0f, Math.min(1.0f, progress)));

                    Component bossbarTitle = ColorUtils.translateColors(Objects.requireNonNull(cfg.getString("weapons.mark.bossbar.title"))
                            .replace("%remaining_time%", String.format("%.1f", timeRemaining))
                            .replace("%mark_color%", currentMarkEffect.getColor())
                            .replace("%mark_icon%", currentMarkEffect.getIcon())
                            .replace("%extra_damage%", currentMarkEffect.getExtraDamage() + ""));

                    bossbar.name(bossbarTitle);
                }

                if (timeRemaining <= 0) {
                    plugin.getPlayerInfoManager().getPlayerInfo(target).setMarks(0);
                    markTimers.remove(targetUUID);
                    ColorUtils.sendPlayerMessage(target, cfgMan.getMessage("weapons.mark.mark_clean", ""));
                    cancelTask(target);
                } else {
                    markTimers.put(targetUUID, timeRemaining - 0.1);
                }
            }
        }, 0L, 2L);

        markRemovalTasks.put(targetUUID, newTask);
    }



    private void cancelTask(Player target) {
        if (markRemovalTasks.containsKey(target.getUniqueId())) {
            markRemovalTasks.get(target.getUniqueId()).cancel();
            markRemovalTasks.remove(target.getUniqueId());
        }
        BossBar bossbar = markedBossbars.remove(target);
        if (bossbar != null) {
            ColorUtils.hidePlayerBossbar(target, bossbar);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player victim)) return;

        PlayerInfo victimInfo = plugin.getPlayerInfoManager().getPlayerInfo(victim);
        if (victimInfo == null) return;

        int currentMarks = victimInfo.getMarks();
        if (currentMarks <= 0) return;

        MarkEffect markEffect = null;
        for (MarkWeapon weapon : getMarkWeapons()) {
            markEffect = weapon.getMarks().get(currentMarks);
            if (markEffect != null) {
                break;
            }
        }

        if (markEffect == null) return;

        double extraDamagePercentage = markEffect.getExtraDamage();
        double damageMultiplier = 1 + (extraDamagePercentage / 100.0);

        double newDamage = event.getDamage() * damageMultiplier;
        event.setDamage(newDamage);
    }

    private void createMarkedBossbar(FileConfiguration cfg, Player p) {
        BossBar markedBossbar = markedBossbars.get(p);

        if (markedBossbar == null) {
            markedBossbar = BossBar.bossBar(
                    ColorUtils.translateColors(""),
                    0.0f,
                    BossBar.Color.valueOf(cfg.getString("weapons.mark.bossbar.color")),
                    BossBar.Overlay.valueOf(cfg.getString("weapons.mark.bossbar.style")));

            markedBossbars.put(p, markedBossbar);
            ColorUtils.showPlayerBossbar(p, markedBossbar);
        }

        Component bossbarTitle = ColorUtils.translateColors("");

        markedBossbar.name(bossbarTitle);
    }

}
