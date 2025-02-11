package net.diground.exyliaClasses.models;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import java.util.List;

public class BackstabWeapon extends Weapon {
    private final double damage;
    private final List<PotionEffect> rivalEffects;
    private final List<PotionEffect> attackerEffects;
    private final Particle rivalParticles;
    private final Sound rivalSound;
    private final Sound attackerSound;

    public BackstabWeapon(Material material, double damage,
                          List<PotionEffect> rivalEffects, List<PotionEffect> attackerEffects,
                          Particle rivalParticles, Sound rivalSound, Sound attackerSound) {
        super(material, WeaponType.BACKSTAB);
        this.damage = damage;
        this.rivalEffects = rivalEffects;
        this.attackerEffects = attackerEffects;
        this.rivalParticles = rivalParticles;
        this.rivalSound = rivalSound;
        this.attackerSound = attackerSound;
    }

    @Override
    public void applyEffects(Player attacker, Player rival) {
        rival.damage(damage, attacker);
        rivalEffects.forEach(rival::addPotionEffect);
        attackerEffects.forEach(attacker::addPotionEffect);
        rival.getWorld().spawnParticle(rivalParticles, rival.getLocation(), 10);
        rival.getWorld().playSound(rival.getLocation(), rivalSound, 1, 1);
        attacker.getWorld().playSound(attacker.getLocation(), attackerSound, 1, 1);
    }
}
