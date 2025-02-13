package net.diground.exyliaClasses.models;

import org.bukkit.entity.Player;

public class PlayerInfo {
    private final Player player;
    private SpecialClass currentClass;
    private double actualEnergy;
    private int marks;
    private double actualMarkDuration;

    public PlayerInfo(Player player) {
        this.player = player;
        this.currentClass = null;
    }

    public Player getPlayer() {
        return player;
    }

    public SpecialClass getCurrentClass() {
        return currentClass;
    }

    public void setCurrentClass(SpecialClass newClass) {
        this.currentClass = newClass;
    }

    public double getActualEnergy() {
        return actualEnergy;
    }

    public void setActualEnergy(double actualEnergy) {
        this.actualEnergy = actualEnergy;
    }

    public int getMarks() {
        return marks;
    }

    public void setMarks(int marks) {
        this.marks = marks;
    }

    public double getActualMarkDuration() {
        return actualMarkDuration;
    }

    public void setActualMarkDuration(double actualMarkDuration) {
        this.actualMarkDuration = actualMarkDuration;
    }
}
