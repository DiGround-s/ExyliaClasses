package net.diground.exyliaClasses.models.weapons;

import net.diground.exyliaClasses.models.Weapon;
import net.diground.exyliaClasses.models.WeaponType;
import net.diground.exyliaClasses.models.utils.SoundEffect;

import java.util.Map;

public class MarkWeapon extends Weapon {
    private final double markDuration;
    private final boolean giveGlow;
    private final boolean markSameClass;
    private final SoundEffect sameClassErrorSound;
    private final Map<Integer, MarkEffect> marks;

    public MarkWeapon(String material, double markDuration, boolean giveGlow, boolean markSameClass, SoundEffect sameClassErrorSound, Map<Integer, MarkEffect> marks) {
        super(material, WeaponType.MARK);
        this.markDuration = markDuration;
        this.giveGlow = giveGlow;
        this.markSameClass = markSameClass;
        this.sameClassErrorSound = sameClassErrorSound;
        this.marks = marks;
    }

    public double getMarkDuration() {
        return markDuration;
    }

    public Map<Integer, MarkEffect> getMarks() {
        return marks;
    }

    public boolean isMarkSameClass() {
        return markSameClass;
    }

    public SoundEffect getSameClassErrorSound() {
        return sameClassErrorSound;
    }

    public boolean isGiveGlow() {
        return giveGlow;
    }
}
