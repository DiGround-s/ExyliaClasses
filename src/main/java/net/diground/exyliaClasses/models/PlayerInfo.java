package net.diground.exyliaClasses.models;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class PlayerInfo {
    private final Player player;
    private EClass playerClass;
    private boolean inWarmup;
    private int archerArrowCharge;
    private BukkitRunnable tagTask;
    private Map<PotionEffectType, PotionEffect> previousPotionEffects = new HashMap<>();

    public PlayerInfo(Player player) {
        this.player = player;
        this.inWarmup = false;
    }

    public Player getPlayer() {
        return player;
    }

    public EClass getPlayerClass() {
        return playerClass;
    }

    public void setPlayerClass(EClass playerEClass) {
        this.playerClass = playerEClass;
    }

    public boolean isInWarmup() {
        return inWarmup;
    }

    public void setInWarmup(boolean inWarmup) {
        this.inWarmup = inWarmup;
    }

    public Map<PotionEffectType, PotionEffect> getPreviousPotionEffects() {
        return previousPotionEffects;
    }

    public void setPreviousPotionEffects(Map<PotionEffectType, PotionEffect> previousPotionEffects) {
        this.previousPotionEffects = previousPotionEffects;
    }

    public void savePreviousEffects(Collection<PotionEffect> activeEffects) {
        previousPotionEffects.clear();
        for (PotionEffect effect : activeEffects) {
            previousPotionEffects.put(effect.getType(), effect);
        }
    }

    public void restorePreviousEffects() {
        for (PotionEffect effect : previousPotionEffects.values()) {
            player.addPotionEffect(effect);
        }
        previousPotionEffects.clear();
    }

    public int getArcherArrowCharge() {
        return archerArrowCharge;
    }

    public void setArcherArrowCharge(int archerArrowCharge) {
        this.archerArrowCharge = archerArrowCharge;
    }

    public BukkitRunnable getTagTask() {
        return tagTask;
    }

    public void setTagTask(BukkitRunnable tagTask) {
        this.tagTask = tagTask;
    }
}