package net.diground.exyliaClasses.models.weapons;

import net.diground.exyliaClasses.models.Weapon;
import net.diground.exyliaClasses.models.WeaponType;
import net.diground.exyliaClasses.models.utils.ParticleEffect;
import net.diground.exyliaClasses.models.utils.SoundEffect;
import org.bukkit.potion.PotionEffect;

import java.util.List;

public class BackstabWeapon extends Weapon {
    private final boolean breakItem;
    private final int extraDamage;
    private final boolean canDead;
    private final int attackCooldownNeed;
    private final ParticleEffect attackedParticles;
    private final ParticleEffect attackerParticles;
    private final SoundEffect attackedSound;
    private final SoundEffect attackerSound;
    private final List<PotionEffect> attackerEffects;
    private final List<PotionEffect> attackedEffects;

    public BackstabWeapon(String material, boolean breakItem, int extraDamage, boolean canDead, int attackCooldownNeed, ParticleEffect attackedParticles, ParticleEffect attackerParticles, SoundEffect attackedSound, SoundEffect attackerSound, List<PotionEffect> attackerEffects, List<PotionEffect> attackedEffects) {
        super(material, WeaponType.BACKSTAB);
        this.breakItem = breakItem;
        this.extraDamage = extraDamage;
        this.canDead = canDead;
        this.attackCooldownNeed = attackCooldownNeed;
        this.attackedParticles = attackedParticles;
        this.attackerParticles = attackerParticles;
        this.attackedSound = attackedSound;
        this.attackerSound = attackerSound;
        this.attackerEffects = attackerEffects;
        this.attackedEffects = attackedEffects;
    }

    public boolean shouldBreakItem() {
        return breakItem;
    }

    public int getExtraDamage() {
        return extraDamage;
    }

    public boolean isCanDead() {
        return canDead;
    }

    public int getAttackCooldownNeed() {
        return attackCooldownNeed;
    }

    public ParticleEffect getAttackedParticles() {
        return attackedParticles;
    }

    public ParticleEffect getAttackerParticles() {
        return attackerParticles;
    }

    public SoundEffect getAttackedSound() {
        return attackedSound;
    }

    public SoundEffect getAttackerSound() {
        return attackerSound;
    }

    public List<PotionEffect> getAttackerEffects() {
        return attackerEffects;
    }

    public List<PotionEffect> getAttackedEffects() {
        return attackedEffects;
    }

    public boolean isBreakItem() {
        return breakItem;
    }
}
