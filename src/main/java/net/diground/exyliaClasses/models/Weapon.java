package net.diground.exyliaClasses.models;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Map;

public abstract class Weapon {
    private final Material material;
    private final WeaponType type;

    public Weapon(Material material, WeaponType type) {
        this.material = material;
        this.type = type;
    }

    public Material getMaterial() {
        return material;
    }

    public WeaponType getType() {
        return type;
    }

    public abstract void applyEffects(Player attacker, Player rival);
}

