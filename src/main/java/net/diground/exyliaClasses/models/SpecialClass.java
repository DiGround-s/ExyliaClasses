package net.diground.exyliaClasses.models;

import org.bukkit.Material;
import org.bukkit.potion.PotionEffect;

import java.util.List;
import java.util.Map;

public class SpecialClass {
    private String id; // File name
    private String displayName; // Name to display
    private String permission;
    private Map<String, Material> equipment;
    private List<PotionEffect> passiveEffects;
    private Warmup warmup;
    private Map<String, Ability> abilities;
    private Map<String, Weapon> weapons;
    private Map<String, HoldEffect> holdEffects;
    private Energy energy;

    // Constructor
    public SpecialClass(String id, String displayName, String permission, Map<String, Material> equipment,
                  List<PotionEffect> passiveEffects, Warmup warmup,
                  Map<String, Ability> abilities, Map<String, HoldEffect> holdEffects, Energy energy) { //, Map<String, Weapon> weapons
        this.id = id;
        this.displayName = displayName;
        this.permission = permission;
        this.equipment = equipment;
        this.passiveEffects = passiveEffects;
        this.warmup = warmup;
        this.abilities = abilities;
        this.holdEffects = holdEffects;
        this.energy = energy;
//        this.weapons = weapons;
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

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public Map<String, Material> getEquipment() {
        return equipment;
    }

    public void setEquipment(Map<String, Material> equipment) {
        this.equipment = equipment;
    }

    public List<PotionEffect> getPassiveEffects() {
        return passiveEffects;
    }

    public void setPassiveEffects(List<PotionEffect> passiveEffects) {
        this.passiveEffects = passiveEffects;
    }

    public Warmup getWarmup() {
        return warmup;
    }

    public void setWarmup(Warmup warmup) {
        this.warmup = warmup;
    }

    public Map<String, Ability> getAbilities() {
        return abilities;
    }

    public void setAbilities(Map<String, Ability> abilities) {
        this.abilities = abilities;
    }

    public Map<String, Weapon> getWeapons() {
        return weapons;
    }

    public void setWeapons(Map<String, Weapon> weapons) {
        this.weapons = weapons;
    }

    public Map<String, HoldEffect> getHoldEffects() {
        return holdEffects;
    }

    public void setHoldEffects(Map<String, HoldEffect> holdEffects) {
        this.holdEffects = holdEffects;
    }

    public Energy getEnergy() {
        return energy;
    }

    public void setEnergy(Energy energy) {
        this.energy = energy;
    }
}