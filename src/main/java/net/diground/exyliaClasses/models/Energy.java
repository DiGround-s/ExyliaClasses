package net.diground.exyliaClasses.models;

public class Energy {
    private final boolean enabled;
    private final String display;
    private final double maxEnergy;
    private final int regenDelay;
    private final double regenQuantity;

    public Energy(boolean enabled, String display, double maxEnergy, int regenDelay, double regenQuantity) {
        this.enabled = enabled;
        this.display = display;
        this.maxEnergy = maxEnergy;
        this.regenDelay = regenDelay;
        this.regenQuantity = regenQuantity;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public double getMaxEnergy() {
        return maxEnergy;
    }

    public int getRegenDelay() {
        return regenDelay;
    }

    public double getRegenQuantity() {
        return regenQuantity;
    }

    public String getDisplay() {
        return display;
    }
}
