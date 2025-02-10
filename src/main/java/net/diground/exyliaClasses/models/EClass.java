package net.diground.exyliaClasses.models;

import org.bukkit.Material;

import java.util.List;
import java.util.Map;

public class EClass {

    private String name;
    private String displayName;
    private List<String> passiveEffects;
    private double warmupTime;
    private List<String> activeEffects;

    public EClass(String name, String displayName, List<String> passiveEffects, double warmupTime, List<String> activeEffects) {
        this.name = name;
        this.displayName = displayName;
        this.passiveEffects = passiveEffects;
        this.warmupTime = warmupTime;
        this.activeEffects = activeEffects;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public List<String> getPassiveEffects() {
        return passiveEffects;
    }

    public void setPassiveEffects(List<String> passiveEffects) {
        this.passiveEffects = passiveEffects;
    }

    public double getWarmupTime() {
        return warmupTime;
    }

    public void setWarmupTime(double warmupTime) {
        this.warmupTime = warmupTime;
    }

    public List<String> getActiveEffects() {
        return activeEffects;
    }

    public void setActiveEffects(List<String> activeEffects) {
        this.activeEffects = activeEffects;
    }
}