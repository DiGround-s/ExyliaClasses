package net.diground.exyliaClasses.models;

import org.bukkit.Material;

public abstract class Weapon {
    private final String material;
    private final WeaponType type;

    public Weapon(String material, WeaponType type) {
        this.material = material;
        this.type = type;
    }

    public String getMaterial() {
        return material;
    }

    public WeaponType getType() {
        return type;
    }
}
