package net.diground.exyliaClasses.managers;

import net.diground.exyliaClasses.ExyliaClasses;
import net.diground.exyliaClasses.models.Energy;
import net.diground.exyliaClasses.models.SpecialClass;
import net.diground.exyliaClasses.utils.ColorUtils;
import net.kyori.adventure.bossbar.BossBar;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static net.diground.exyliaClasses.utils.EffectUtils.playParticle;
import static net.diground.exyliaClasses.utils.EffectUtils.playSound;

public class SpecialClassManager {
    private final ExyliaClasses plugin;

    public SpecialClassManager(ExyliaClasses plugin) {
        this.plugin = plugin;
    }

    private final Map<String, SpecialClass> classMap = new HashMap<>();
    private final Map<Player, BukkitRunnable> energyTasks = new HashMap<>();
    private final Map<Player, BossBar> energyBossbars = new HashMap<>();

    public Map<String, SpecialClass> getClassMap() {
        return classMap;
    }

    public void startClass(Player p, SpecialClass specialClass) {
        for (PotionEffect effect : specialClass.getPassiveEffects()) {
            p.addPotionEffect(effect);
        }

        if (specialClass.getEnergy().isEnabled()) {
            FileConfiguration cfg = plugin.getConfigManager().getConfig();
            startEnergyTask(cfg, p, specialClass);
            if (cfg.getBoolean("energy.bossbar.enabled")) {
                createEnergyBossbar(cfg, p);
            }
        }
    }

    public void cancelClass(Player p, SpecialClass specialClass) {
        ConfigManager cfg = plugin.getConfigManager();
        playSound(p, specialClass.getWarmup().getSoundCancel());
        playParticle(p, specialClass.getWarmup().getParticleCancel());
        ColorUtils.sendPlayerMessage(p, cfg.getMessage("warmup.finish", "%prefix% <#a33b53>¡Tu armadura ha cambiado y ya no eres un %class_display_name%&r<#a33b53>!")
                .replace("%class_display_name%", specialClass.getDisplayName()));

        for (PotionEffect effect : specialClass.getPassiveEffects()) {
            p.removePotionEffect(effect.getType());
        }

        cancelEnergyTask(p);
    }

    private void startEnergyTask(FileConfiguration cfg, Player player, SpecialClass specialClass) {
        Energy energy = specialClass.getEnergy();

        BukkitRunnable task = new BukkitRunnable() {
            @Override
            public void run() {
                if (!player.isOnline()) {
                    cancel();
                    return;
                }

                double currentEnergy = plugin.getPlayerInfoManager().getPlayerInfo(player).getActualEnergy();
                double maxEnergy = energy.getMaxEnergy();

                if (currentEnergy < maxEnergy) {
                    currentEnergy += energy.getRegenQuantity();
                    if (currentEnergy > maxEnergy) {
                        currentEnergy = maxEnergy;
                    }

                    currentEnergy = Math.round(currentEnergy * 10.0) / 10.0;
                    plugin.getPlayerInfoManager().getPlayerInfo(player).setActualEnergy(currentEnergy);

                    // Actualizar bossbar
                    BossBar bossbar = energyBossbars.get(player);
                    if (bossbar != null) {
                        float progress = (float) (currentEnergy / maxEnergy);
                        bossbar.progress(Math.max(0.0f, Math.min(1.0f, progress)));
                        bossbar.name(ColorUtils.translateColors(Objects.requireNonNull(cfg.getString("energy.bossbar.title"))
                                .replace("%energy%", currentEnergy + "")));
                    }
                }
            }
        };

        energyTasks.put(player, task);
        task.runTaskTimer(plugin, 0L, energy.getRegenDelay());
    }

    private void cancelEnergyTask(Player player) {
        BukkitRunnable task = energyTasks.get(player);
        if (task != null) {
            task.cancel();
            energyTasks.remove(player);
        }

        plugin.getPlayerInfoManager().getPlayerInfo(player).setActualEnergy(0);

        BossBar bossbar = energyBossbars.remove(player);
        if (bossbar != null) {
            ColorUtils.hidePlayerBossbar(player, bossbar);
        }
    }

    private void createEnergyBossbar(FileConfiguration cfg, Player p) {
            BossBar energyBossbar = BossBar.bossBar(
                    ColorUtils.translateColors(""),
                    0.0f,
                    BossBar.Color.valueOf(cfg.getString("energy.bossbar.color")),
                    BossBar.Overlay.valueOf(cfg.getString("energy.bossbar.style")));

            energyBossbars.put(p, energyBossbar);
            ColorUtils.showPlayerBossbar(p, energyBossbar);
    }
}
