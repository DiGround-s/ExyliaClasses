package net.diground.exyliaClasses.models;

public class Energy {
    private boolean enabled;
    private double maxEnergy;
    private int regenDelay;
    private double regenQuantity;

    public Energy(boolean enabled, double maxEnergy, int regenDelay, double regenQuantity) {
        this.enabled = enabled;
        this.maxEnergy = maxEnergy;
        this.regenDelay = regenDelay;
        this.regenQuantity = regenQuantity;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public double getMaxEnergy() {
        return maxEnergy;
    }

    public void setMaxEnergy(double maxEnergy) {
        this.maxEnergy = maxEnergy;
    }

    public int getRegenDelay() {
        return regenDelay;
    }

    public void setRegenDelay(int regenDelay) {
        this.regenDelay = regenDelay;
    }

    public double getRegenQuantity() {
        return regenQuantity;
    }

    public void setRegenQuantity(double regenQuantity) {
        this.regenQuantity = regenQuantity;
    }
}
