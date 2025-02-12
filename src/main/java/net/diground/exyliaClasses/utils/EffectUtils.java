package net.diground.exyliaClasses.utils;

import net.diground.exyliaClasses.models.ParticleEffect;
import net.diground.exyliaClasses.models.SoundEffect;
import org.bukkit.entity.Player;

public class EffectUtils {
    public static void playSound(Player player, SoundEffect soundEffect) {
        if (!soundEffect.isEnabled()) {
            return;
        }
        player.playSound(player.getLocation(), soundEffect.getSound(), soundEffect.getVolume(), soundEffect.getPitch());
    }
    public static void playParticle(Player player, ParticleEffect particleEffect) {
        if (!particleEffect.isEnabled()) {
            return;
        }

        player.getWorld().spawnParticle(
                particleEffect.getParticle(),
                player.getLocation().add(0,1,0),
                particleEffect.getQuantity(),
                particleEffect.getOffSetX(),
                particleEffect.getOffSetY(),
                particleEffect.getOffSetZ(),
                particleEffect.getSpeed()
        );

    }
}
