package net.diground.exyliaClasses.models;

import net.diground.exyliaClasses.models.utils.ParticleEffect;
import net.diground.exyliaClasses.models.utils.SoundEffect;
import org.bukkit.Material;
import org.bukkit.potion.PotionEffect;

import java.util.List;

public class Ability {
    private String id;
    private String displayName;
    private Material material;
    private String type;
    private List<String> actions;
    private List<PotionEffect> effects;
    private int cooldown;
    private ParticleEffect particles;
    private SoundEffect sound;
    private int radius;
    private double energyCost;

    public Ability(String id, String displayName, Material material, String type, List<String> actions, List<PotionEffect> effects, int cooldown, ParticleEffect particles, SoundEffect sound, int radius, double energyCost) {
        this.id = id;
        this.displayName = displayName;
        this.material = material;
        this.type = type;
        this.actions = actions;
        this.effects = effects;
        this.cooldown = cooldown;
        this.particles = particles;
        this.sound = sound;
        this.radius = radius;
        this.energyCost = energyCost;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getActions() {
        return actions;
    }

    public void setActions(List<String> actions) {
        this.actions = actions;
    }

    public List<PotionEffect> getEffects() {
        return effects;
    }

    public void setEffects(List<PotionEffect> effects) {
        this.effects = effects;
    }

    public int getCooldown() {
        return cooldown;
    }

    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }

    public ParticleEffect getParticles() {
        return particles;
    }

    public void setParticles(ParticleEffect particles) {
        this.particles = particles;
    }

    public SoundEffect getSound() {
        return sound;
    }

    public void setSound(SoundEffect sound) {
        this.sound = sound;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public double getEnergyCost() {
        return energyCost;
    }

    public void setEnergyCost(double energyCost) {
        this.energyCost = energyCost;
    }
}
