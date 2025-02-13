package net.diground.exyliaClasses.listeners;

import net.diground.exyliaClasses.ExyliaClasses;
import net.diground.exyliaClasses.managers.ConfigManager;
import net.diground.exyliaClasses.models.PlayerInfo;
import net.diground.exyliaClasses.models.SpecialClass;
import net.diground.exyliaClasses.models.Weapon;
import net.diground.exyliaClasses.models.weapons.BackstabWeapon;
import net.diground.exyliaClasses.models.weapons.MarkWeapon;
import net.diground.exyliaClasses.utils.ColorUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;

import java.util.Objects;

import static net.diground.exyliaClasses.utils.DebugUtils.logDebug;
import static net.diground.exyliaClasses.utils.EffectUtils.playParticle;
import static net.diground.exyliaClasses.utils.EffectUtils.playSound;

public class BackstabListener implements Listener {
    private final ExyliaClasses plugin;
    public BackstabListener(ExyliaClasses plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onHit(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player)) return;
        if (event.isCancelled()) return;

        PlayerInfo playerInfo = plugin.getPlayerInfoManager().getPlayerInfo(player);
        if (playerInfo == null) return;

        SpecialClass currentClass = playerInfo.getCurrentClass();
        if (currentClass == null || currentClass.getWeapons() == null) return;

        Material handMaterial = player.getInventory().getItemInMainHand().getType();

        Entity hitEntity = event.getEntity();
        if (!(hitEntity instanceof Player target)) return;

        BackstabWeapon backstabWeapon = null;
        for (Weapon weapon : currentClass.getWeapons().values()) {
            if (weapon instanceof BackstabWeapon bw) {
                Material weaponMaterial = Material.matchMaterial(bw.getMaterial());
                if (handMaterial.equals(weaponMaterial)) {
                    backstabWeapon = bw;
                }
            }
        }
        if (backstabWeapon == null) return;

        if (!isBackstab(player, target)) return;
        double attackCooldown = player.getAttackCooldown() * 100;
        double attackCooldownNeed = backstabWeapon.getAttackCooldownNeed();

        ConfigManager cfg = plugin.getConfigManager();
        if (!(attackCooldown >= attackCooldownNeed)) {
            ColorUtils.sendPlayerMessage(player, cfg.getMessage("weapons.backstab.insufficient_cooldown", "")
                    .replace("%need_cooldown%", (int) attackCooldownNeed + "")
                    .replace("%actual_cooldown%", (int) attackCooldown + ""));
            return;
        }

        applyBackstabEffects(backstabWeapon, player, target, cfg);
    }

    private void applyBackstabEffects(BackstabWeapon backstabWeapon, Player attacker, Player target, ConfigManager cfg) {
        logDebug("Iniciando backstab de " + attacker.getName() + " a " + target.getName());

        if (!backstabWeapon.isCanDead()) {
            double damage = backstabWeapon.getExtraDamage();
            double newHealth = target.getHealth() - damage;

            logDebug("Verificando si el objetivo puede morir. Salud resultante: " + newHealth);

            if (newHealth <= 1) {
                ColorUtils.sendPlayerMessage(attacker, cfg.getMessage("weapons.backstab.cant_dead", ""));
                return;
            }

            target.setHealth(Math.max(1, newHealth));
        } else {
            double damage = backstabWeapon.getExtraDamage();
            double newHealth = Math.max(0, target.getHealth() - damage);
            target.setHealth(newHealth);

            logDebug("Aplicado daño de backstab. Nueva salud del objetivo: " + target.getHealth());
        }

        // Aplicar efectos de poción
        for (PotionEffect potionEffect : backstabWeapon.getAttackedEffects()) {
            target.addPotionEffect(potionEffect);
            logDebug("Aplicado efecto a la víctima: " + potionEffect.getType());
        }
        for (PotionEffect potionEffect : backstabWeapon.getAttackerEffects()) {
            attacker.addPotionEffect(potionEffect);
            logDebug("Aplicado efecto al atacante: " + potionEffect.getType());
        }

        // Rompe el arma si está configurado
        if (backstabWeapon.isBreakItem()) {
            attacker.getInventory().setItemInMainHand(null);
            logDebug("El arma del atacante se ha roto.");
        }

        // Efectos visuales y sonoros
        playSound(target, backstabWeapon.getAttackedSound());
        playSound(attacker, backstabWeapon.getAttackerSound());
        playParticle(target, backstabWeapon.getAttackedParticles());
        playParticle(attacker, backstabWeapon.getAttackerParticles());

        logDebug("Reproducidos efectos visuales y sonoros del backstab.");

        // Mensajes de éxito
        ColorUtils.sendPlayerMessage(attacker, cfg.getMessage("weapons.backstab.success", ""));
        ColorUtils.sendPlayerMessage(target, cfg.getMessage("weapons.backstab.success_target", ""));
    }




    private boolean isBackstab(Player attacker, Player target) {
        Location attackerLoc = attacker.getLocation();
        Location targetLoc = target.getLocation();
        double attackerYaw = normalizeAngle(attackerLoc.getYaw());
        double targetYaw = normalizeAngle(targetLoc.getYaw());
        double deltaYaw = Math.abs(normalizeAngle(targetYaw - attackerYaw));
        double backstabThreshold = 50.0;
        return deltaYaw <= backstabThreshold;
    }
    private double normalizeAngle(double angle) {
        angle %= 360.0;
        if (angle >= 180.0) {
            angle -= 360.0;
        }
        if (angle < -180.0) {
            angle += 360.0;
        }
        return angle;
    }


}
