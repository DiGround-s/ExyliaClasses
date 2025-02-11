package net.diground.exyliaClasses.models;

import org.bukkit.Particle;

public class ParticleEffect {
    private boolean enabled;
    private Particle particle;

    public ParticleEffect(boolean enabled, Particle particle) {
        this.enabled = enabled;
        this.particle = particle;
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
}
