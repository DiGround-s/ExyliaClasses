package net.diground.exyliaClasses.managers;

import net.diground.exyliaClasses.ExyliaClasses;
import net.diground.exyliaClasses.models.PlayerInfo;
import net.diground.exyliaClasses.models.SpecialClass;
import net.diground.exyliaClasses.models.Warmup;
import net.diground.exyliaClasses.utils.ColorUtils;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

import static net.diground.exyliaClasses.utils.EffectUtils.playParticle;
import static net.diground.exyliaClasses.utils.EquipmentUtils.equipmentMatches;
import static net.diground.exyliaClasses.utils.EffectUtils.playSound;

public class WarmupManager {
    private final ExyliaClasses plugin;
    private final Map<Player, BukkitRunnable> classTimers = new HashMap<>();

    public WarmupManager(ExyliaClasses plugin) {
        this.plugin = plugin;
    }

    public void startNewClassWarmup(Player player, SpecialClass newClass) {
        ConfigManager cfg = plugin.getConfigManager();
        cancelNewClassWarmup(player);

        BukkitRunnable task = new BukkitRunnable() {
            private final Warmup warmup = newClass.getWarmup();
            private int countdown = warmup.getTime(); // Ajusta según necesites

            @Override
            public void run() {
                if (countdown > 0) {
                    ColorUtils.sendPlayerMessage(player, cfg.getMessage("warmup.timer", "%prefix% <#ffc58f>Serás %class_display_name%<#ffc58f> en %time%s")
                            .replace("%class_display_name%", newClass.getDisplayName())
                            .replace("%time%", "" + countdown));
                    playSound(player, warmup.getSound());
                    playParticle(player, warmup.getParticle());
                    countdown--;
                } else {
                    PlayerInfo playerInfo = plugin.getPlayerInfoManager().getPlayerInfo(player);
                    if (equipmentMatches(player, newClass.getEquipment())) {
                        playerInfo.setCurrentClass(newClass);
                        ColorUtils.sendPlayerMessage(player, cfg.getMessage("warmup.ready", "%prefix% <#8fffc1>¡Ahora eres %class_display_name%<#8fffc1>!")
                                .replace("%class_display_name%", newClass.getDisplayName()));
                        playSound(player, warmup.getSoundCompleted());
                        playParticle(player, warmup.getParticleCompleted());
                        plugin.getSpecialClassManager().startClass(player, newClass);
                    } else {
                        ColorUtils.sendPlayerMessage(player, cfg.getMessage("warmup.canceled", "%prefix% <#a33b53>¡Tu armadura ha cambiado y se ha cancelado el cambio de clase!"));
                        playSound(player, warmup.getSoundCancel());
                        playParticle(player, warmup.getParticleCancel());
                        plugin.getSpecialClassManager().cancelClass(player, newClass);
                    }
                    classTimers.remove(player);
                    cancel();
                }
            }
        };

        classTimers.put(player, task);
        task.runTaskTimer(plugin, 0L, 20L);
    }

    public void cancelNewClassWarmup(Player player) {
        ConfigManager cfg = plugin.getConfigManager();
        BukkitRunnable task = classTimers.remove(player);
        if (task != null) {
            task.cancel();
            ColorUtils.sendPlayerMessage(player, cfg.getMessage("warmup.canceled", "%prefix% <#a33b53>¡Tu armadura ha cambiado y se ha cancelado el cambio de clase!"));
        }
    }
}
