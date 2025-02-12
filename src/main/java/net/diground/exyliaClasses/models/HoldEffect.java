package net.diground.exyliaClasses.models;

import org.bukkit.Material;
import org.bukkit.potion.PotionEffect;

import java.util.List;

public class HoldEffect {
    private Material material;
    private String type;
    private int radius;
    private List<PotionEffect> effects;

    public HoldEffect(Material material, String type, int radius, List<PotionEffect> effects) {
        this.material = material;
        this.type = type;
        this.radius = radius;
        this.effects = effects;
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

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public List<PotionEffect> getEffects() {
        return effects;
    }

    public void setEffects(List<PotionEffect> effects) {
        this.effects = effects;
    }
}
