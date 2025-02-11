package net.diground.exyliaClasses.models;

public class Warmup {
    private int time;
    private SoundEffect sound;
    private ParticleEffect particle;
    private SoundEffect soundCompleted;
    private ParticleEffect particleCompleted;
    private SoundEffect soundCancel;
    private ParticleEffect particleCancel;

    public Warmup(int time, SoundEffect sound, ParticleEffect particle, SoundEffect soundCompleted, ParticleEffect particleCompleted, SoundEffect soundCancel, ParticleEffect particleCancel) {
        this.time = time;
        this.sound = sound;
        this.particle = particle;
        this.soundCompleted = soundCompleted;
        this.particleCompleted = particleCompleted;
        this.soundCancel = soundCancel;
        this.particleCancel = particleCancel;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public SoundEffect getSound() {
        return sound;
    }

    public void setSound(SoundEffect sound) {
        this.sound = sound;
    }

    public ParticleEffect getParticle() {
        return particle;
    }

    public void setParticle(ParticleEffect particle) {
        this.particle = particle;
    }

    public SoundEffect getSoundCompleted() {
        return soundCompleted;
    }

    public void setSoundCompleted(SoundEffect soundCompleted) {
        this.soundCompleted = soundCompleted;
    }

    public ParticleEffect getParticleCompleted() {
        return particleCompleted;
    }

    public void setParticleCompleted(ParticleEffect particleCompleted) {
        this.particleCompleted = particleCompleted;
    }

    public SoundEffect getSoundCancel() {
        return soundCancel;
    }

    public void setSoundCancel(SoundEffect soundCancel) {
        this.soundCancel = soundCancel;
    }

    public ParticleEffect getParticleCancel() {
        return particleCancel;
    }

    public void setParticleCancel(ParticleEffect particleCancel) {
        this.particleCancel = particleCancel;
    }
}
