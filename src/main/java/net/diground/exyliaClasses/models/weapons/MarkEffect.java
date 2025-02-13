package net.diground.exyliaClasses.models.weapons;

import net.diground.exyliaClasses.models.utils.ParticleEffect;
import net.diground.exyliaClasses.models.utils.SoundEffect;
import org.bukkit.potion.PotionEffect;

import java.util.List;

public class MarkEffect {
    private final int markNumber;
    private final int extraDamage;
    private final String color;
    private final String icon;
    private final ParticleEffect attackedParticles;
    private final ParticleEffect attackerParticles;
    private final SoundEffect attackedSound;
    private final SoundEffect attackerSound;
    private final List<PotionEffect> attackerEffects;
    private final List<PotionEffect> attackedEffects;
    private final boolean removeInvisibility;

    public MarkEffect(int markNumber, int extraDamage, String color, String icon, ParticleEffect attackedParticles, ParticleEffect attackerParticles, SoundEffect attackedSound, SoundEffect attackerSound, List<PotionEffect> attackerEffects, List<PotionEffect> attackedEffects, boolean removeInvisibility) {
        this.markNumber = markNumber;
        this.extraDamage = extraDamage;
        this.color = color;
        this.icon = icon;
        this.attackedParticles = attackedParticles;
        this.attackerParticles = attackerParticles;
        this.attackedSound = attackedSound;
        this.attackerSound = attackerSound;
        this.attackerEffects = attackerEffects;
        this.attackedEffects = attackedEffects;
        this.removeInvisibility = removeInvisibility;
    }

    public int getMarkNumber() {
        return markNumber;
    }

    public int getExtraDamage() {
        return extraDamage;
    }

    public String getColor() {
        return color;
    }

    public String getIcon() {
        return icon;
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

    public boolean isRemoveInvisibility() {
        return removeInvisibility;
    }
}
