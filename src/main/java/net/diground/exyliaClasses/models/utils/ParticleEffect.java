package net.diground.exyliaClasses.models.utils;

import org.bukkit.Particle;

public class ParticleEffect {
    private boolean enabled;
    private Particle particle;
    private int quantity;
    private double offSetX;
    private double offSetY;
    private double offSetZ;
    private double speed;

    public ParticleEffect(boolean enabled, Particle particle, int quantity, double offSetX, double offSetY, double offSetZ, double speed) {
        this.enabled = enabled;
        this.particle = particle;
        this.quantity = quantity;
        this.offSetX = offSetX;
        this.offSetY = offSetY;
        this.offSetZ = offSetZ;
        this.speed = speed;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Particle getParticle() {
        return particle;
    }

    public void setParticle(Particle particle) {
        this.particle = particle;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getOffSetX() {
        return offSetX;
    }

    public void setOffSetX(double offSetX) {
        this.offSetX = offSetX;
    }

    public double getOffSetY() {
        return offSetY;
    }

    public void setOffSetY(double offSetY) {
        this.offSetY = offSetY;
    }

    public double getOffSetZ() {
        return offSetZ;
    }

    public void setOffSetZ(double offSetZ) {
        this.offSetZ = offSetZ;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }
}
