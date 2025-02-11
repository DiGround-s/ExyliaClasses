package net.diground.exyliaClasses.models;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

public class MarkWeapon extends Weapon {
    private final int marks;
    private final int markDuration;
    private final List<PotionEffect> rivalEffects;
    private final List<PotionEffect> attackerEffects;
    private final Particle rivalParticles;
    private final Sound rivalSound;
    private final Sound attackerSound;

    public MarkWeapon(Material material, int marks, int markDuration,
                      List<PotionEffect> rivalEffects, List<PotionEffect> attackerEffects,
                      Particle rivalParticles, Sound rivalSound, Sound attackerSound) {
        super(material, WeaponType.MARK);
        this.marks = marks;
        this.markDuration = markDuration;
        this.rivalEffects = rivalEffects;
        this.attackerEffects = attackerEffects;
        this.rivalParticles = rivalParticles;
        this.rivalSound = rivalSound;
        this.attackerSound = attackerSound;
    }

    @Override
    public void applyEffects(Player attacker, Player rival) {
        rivalEffects.forEach(rival::addPotionEffect);
        attackerEffects.forEach(attacker::addPotionEffect);
        rival.getWorld().spawnParticle(rivalParticles, rival.getLocation(), 10);
        rival.getWorld().playSound(rival.getLocation(), rivalSound, 1, 1);
        attacker.getWorld().playSound(attacker.getLocation(), attackerSound, 1, 1);
    }
}
