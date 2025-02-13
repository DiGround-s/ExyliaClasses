package net.diground.exyliaClasses.loaders;

import net.diground.exyliaClasses.models.Weapon;
import net.diground.exyliaClasses.models.WeaponType;
import net.diground.exyliaClasses.models.weapons.BackstabWeapon;
import net.diground.exyliaClasses.models.weapons.MarkEffect;
import net.diground.exyliaClasses.models.weapons.MarkWeapon;
import net.diground.exyliaClasses.models.utils.ParticleEffect;
import net.diground.exyliaClasses.models.utils.SoundEffect;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

import static net.diground.exyliaClasses.utils.DebugUtils.logDebug;
import static net.diground.exyliaClasses.utils.DebugUtils.logError;

public class WeaponLoader {
    private static final Map<String, MarkWeapon> markWeapons = new HashMap<>();

    public static Map<String, Weapon> loadWeapons(FileConfiguration config) {
        Map<String, Weapon> weapons = new HashMap<>();
        ConfigurationSection weaponsSection = config.getConfigurationSection("weapons");
        if (weaponsSection == null) return new HashMap<>();

        for (String weaponKey : weaponsSection.getKeys(false)) {
            ConfigurationSection weaponData = weaponsSection.getConfigurationSection(weaponKey);
            if (weaponData == null) continue;

            String material = weaponData.getString("material");
            WeaponType type = WeaponType.valueOf(weaponData.getString("type"));

            if (material == null) {
                System.out.println("Material inválido para el arma: " + weaponKey);
                continue;
            }

            switch (type) {
                case MARK:
                    MarkWeapon markWeapon = loadMarkWeapon(weaponData, material);
                    weapons.put(weaponKey, markWeapon);
                    // Agregar MarkWeapon al contenedor
                    markWeapons.put(weaponKey, markWeapon);
                    break;
                case BACKSTAB:
                    weapons.put(weaponKey, loadBackstabWeapon(weaponData, material));
                    break;
            }
        }
        return weapons;
    }

    public static Collection<MarkWeapon> getMarkWeapons() {
        return markWeapons.values();
    }

    private static MarkWeapon loadMarkWeapon(ConfigurationSection section, String material) {
        double markDuration = section.getDouble("mark_duration", 5.0);
        boolean markSameClass = section.getBoolean("can_mark_same_class", false);
        boolean giveGlow = section.getBoolean("give_glow", false);
        SoundEffect sameClassErrorSound = loadSoundEffect(section.getConfigurationSection("same_class_error_sound"));
        logDebug("sameClassErrorSound: " + sameClassErrorSound);
        Map<Integer, MarkEffect> marks = new HashMap<>();

        ConfigurationSection marksSection = section.getConfigurationSection("marks");
        if (marksSection != null) {
            for (String key : marksSection.getKeys(false)) {
                ConfigurationSection markData = marksSection.getConfigurationSection(key);
                if (markData == null) continue;

                MarkEffect markEffect = new MarkEffect(
                        markData.getInt("mark_number"),
                        markData.getInt("extra_damage"),
                        markData.getString("color"),
                        markData.getString("icon"),
                        loadParticleEffect(markData.getConfigurationSection("attacked_particles")),
                        loadParticleEffect(markData.getConfigurationSection("attacker_particles")),
                        loadSoundEffect(markData.getConfigurationSection("attacked_sound")),
                        loadSoundEffect(markData.getConfigurationSection("attacker_sound")),
                        loadPotionEffects(markData.getStringList("attacker_effects")),
                        loadPotionEffects(markData.getStringList("attacked_effects")),
                        markData.getBoolean("remove_invisibility")

                );

                marks.put(markEffect.getMarkNumber(), markEffect);
            }
        }

        return new MarkWeapon(material, markDuration, giveGlow, markSameClass, sameClassErrorSound, marks);
    }

    private static BackstabWeapon loadBackstabWeapon(ConfigurationSection section, String material) {
        boolean breakItem = section.getBoolean("break_item");
        int extraDamage = section.getInt("extra_damage");
        int attackCooldownNeed = section.getInt("attack_cooldown_need");
        boolean canDead = section.getBoolean("can_dead");

        return new BackstabWeapon(
                material,
                breakItem,
                extraDamage,
                canDead,
                attackCooldownNeed,
                loadParticleEffect(section.getConfigurationSection("attacked_particles")),
                loadParticleEffect(section.getConfigurationSection("attacker_particles")),
                loadSoundEffect(section.getConfigurationSection("attacked_sound")),
                loadSoundEffect(section.getConfigurationSection("attacker_sound")),
                loadPotionEffects(section.getStringList("attacker_effects")),
                loadPotionEffects(section.getStringList("attacked_effects"))
        );
    }

    private static ParticleEffect loadParticleEffect(ConfigurationSection config) {
        if (config == null) return null;
        boolean enabled = config.getBoolean("enabled", false);
        String particleConfig = config.getString("type", "");

        if (particleConfig.isEmpty()) return null;

        String[] parts = particleConfig.split("\\|");
        String particleType = parts[0];
        int quantity = parts.length > 1 ? parseInt(parts[1], 1) : 1;
        double offSetX = parts.length > 2 ? Double.parseDouble(parts[2]) : 0.0;
        double offSetY = parts.length > 3 ? Double.parseDouble(parts[3]) : 0.0;
        double offSetZ = parts.length > 4 ? Double.parseDouble(parts[4]) : 0.0;
        double speed = parts.length > 5 ? Double.parseDouble(parts[5]) : 1.0;

        try {
            Particle particle = Particle.valueOf(particleType.toUpperCase());
            return new ParticleEffect(enabled, particle, quantity, offSetX, offSetY, offSetZ, speed);
        } catch (IllegalArgumentException e) {
            logError("Partícula inválida en " + config + ": " + particleType);
            return null;
        }
    }

    private static SoundEffect loadSoundEffect(ConfigurationSection config) {
        if (config == null) return null;
        boolean enabled = config.getBoolean("enabled", false);
        String soundData = config.getString("type", "");
        return parseSoundEffect(enabled, soundData, "sound");
    }

    private static SoundEffect parseSoundEffect(boolean enabled, String soundData, String path) {
        if (soundData.isEmpty()) return null;
        String[] parts = soundData.split("\\|");
        try {
            Sound sound = Sound.valueOf(parts[0].toUpperCase());
            float volume = (parts.length > 1) ? Float.parseFloat(parts[1]) : 1.0f;
            float pitch = (parts.length > 2) ? Float.parseFloat(parts[2]) : 1.0f;
            return new SoundEffect(enabled, sound, volume, pitch);
        } catch (IllegalArgumentException e) {
            logError("Sonido inválido en " + path + ": " + soundData);
            return null;
        }
    }

    private static List<PotionEffect> loadPotionEffects(List<String> effects) {
        List<PotionEffect> potionEffects = new ArrayList<>();
        for (String effect : effects) {
            String[] parts = effect.split("\\|");
            if (parts.length == 3) {
                PotionEffectType type = PotionEffectType.getByName(parts[0]);
                int level = Integer.parseInt(parts[1]);
                int duration = Integer.parseInt(parts[2]);
                if (type != null) {
                    potionEffects.add(new PotionEffect(type, duration * 20, level - 1));
                }
            }
        }
        return potionEffects;
    }

    private static int parseInt(String value, int defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
