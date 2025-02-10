package net.diground.exyliaClasses.models;

import org.bukkit.Material;

import java.util.UUID;

public class Cooldown {
    private UUID player;
    private EClass eClass;
    private Material material;
    private long time;

    public Cooldown(UUID player, Material material, long time, EClass eClass) {
        this.player = player;
        this.material = material;
        this.time = time;
        this.eClass = eClass;
    }

    public UUID getPlayer() {
        return player;
    }

    public void setPlayer(UUID player) {
        this.player = player;
    }

    public EClass geteClass() {
        return eClass;
    }

    public void seteClass(EClass eClass) {
        this.eClass = eClass;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
