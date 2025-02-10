package net.diground.exyliaClasses.models.classes;

import net.diground.exyliaClasses.models.EClass;
import org.bukkit.Material;

import java.util.List;
import java.util.Map;

public class Archer extends EClass {
    private double tagExtraDamage;
    private double arrowTagExtraDamage;
    private int maxCharges;
    private double tagTime;
    private boolean canTagOthersArchers;
    private Map<Integer, String> tagColors;
    private boolean applyGlow;
    private boolean removeInvisibility;

    public Archer(String name, String displayName, List<String> passiveEffects, double warmupTime, List<String> activeEffects,
                  double tagExtraDamage, double arrowTagExtraDamage, int maxCharges, double tagTime, boolean canTagOthersArchers,
                  Map<Integer, String> tagColors, boolean applyGlow, boolean removeInvisibility) {
        super(name, displayName, passiveEffects, warmupTime, activeEffects);
        this.tagExtraDamage = tagExtraDamage;
        this.arrowTagExtraDamage = arrowTagExtraDamage;
        this.maxCharges = maxCharges;
        this.tagTime = tagTime;
        this.canTagOthersArchers = canTagOthersArchers;
        this.tagColors = tagColors;
        this.applyGlow = applyGlow;
        this.removeInvisibility = removeInvisibility;
    }

    public double getTagExtraDamage() {
        return tagExtraDamage;
    }

    public void setTagExtraDamage(double tagExtraDamage) {
        this.tagExtraDamage = tagExtraDamage;
    }

    public double getArrowTagExtraDamage() {
        return arrowTagExtraDamage;
    }

    public void setArrowTagExtraDamage(double arrowTagExtraDamage) {
        this.arrowTagExtraDamage = arrowTagExtraDamage;
    }

    public int getMaxCharges() {
        return maxCharges;
    }

    public void setMaxCharges(int maxCharges) {
        this.maxCharges = maxCharges;
    }

    public double getTagTime() {
        return tagTime;
    }

    public void setTagTime(double tagTime) {
        this.tagTime = tagTime;
    }

    public boolean isCanTagOthersArchers() {
        return canTagOthersArchers;
    }

    public void setCanTagOthersArchers(boolean canTagOthersArchers) {
        this.canTagOthersArchers = canTagOthersArchers;
    }

    public Map<Integer, String> getTagColors() {
        return tagColors;
    }

    public void setTagColors(Map<Integer, String> tagColors) {
        this.tagColors = tagColors;
    }

    public boolean isApplyGlow() {
        return applyGlow;
    }

    public void setApplyGlow(boolean applyGlow) {
        this.applyGlow = applyGlow;
    }

    public boolean isRemoveInvisibility() {
        return removeInvisibility;
    }

    public void setRemoveInvisibility(boolean removeInvisibility) {
        this.removeInvisibility = removeInvisibility;
    }
}