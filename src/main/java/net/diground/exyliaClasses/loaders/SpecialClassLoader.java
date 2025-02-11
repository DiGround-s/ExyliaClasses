package net.diground.exyliaClasses.loaders;

import net.diground.exyliaClasses.ExyliaClasses;
import net.diground.exyliaClasses.models.*;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.util.*;

public class SpecialClassLoader {
    private final ExyliaClasses plugin;
    private final Map<String, SpecialClass> classMap = new HashMap<>();

    public Map<String, SpecialClass> getClassMap() {
        return classMap;
    }

    public SpecialClassLoader(ExyliaClasses plugin) {
        this.plugin = plugin;
    }

    public void loadClasses() {
        File itemsFolder = new File(plugin.getDataFolder(), "classes");
        if (!itemsFolder.exists()) {
            itemsFolder.mkdirs();
        }

        for (File file : Objects.requireNonNull(itemsFolder.listFiles())) {
            if (file.isFile() && file.getName().endsWith(".yml")) {
                loadClass(file);
            }
        }
    }

    private void loadClass(File file) {
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        String id = file.getName().replaceFirst("[.][^.]+$", "");
        String displayName = config.getString("display-name");
        String permission = config.getString("permission");
        Map<String, Material> equipment = loadEquipment(config, file);
        List<PotionEffect> passiveEffects = loadPotionEffects(config.getStringList("pasive_effects"), file);

        int warmupTime = config.getInt("warmup.time", 3);

        Warmup warmup = new Warmup(
                warmupTime,
                loadSoundEffect(config, "warmup.sound"),
                loadParticleEffect(config, "warmup.particle"),
                loadSoundEffect(config, "warmup.sound_completed"),
                loadParticleEffect(config, "warmup.particle_completed"),
                loadSoundEffect(config, "warmup.sound_cancel"),
                loadParticleEffect(config, "warmup.particle_cancel")
        );
        Map<String, Ability> abilities = loadAbilities(config, file);

        // cargar armas
//        Map<String, Weapon> weapons = loadWeapons(config, "weapons", file);

        // Debugging
        plugin.getLogger().info("Clase cargada: " + id);
        plugin.getLogger().info("Display Name: " + displayName);
        plugin.getLogger().info("Permission: " + permission);
        plugin.getLogger().info("Equipment: " + equipment);
        plugin.getLogger().info("Pasive Effects: " + passiveEffects);
        plugin.getLogger().info("Warmup cargado: " + warmup);
        plugin.getLogger().info("Abilities cargadas: " + abilities);
        SpecialClass specialClass = new SpecialClass(id, displayName, permission, equipment, passiveEffects, warmup, abilities);
        classMap.put(id, specialClass);

        plugin.getLogger().info("El item " + id + " se ha cargado correctamente.");
    }

    private Map<String, Material> loadEquipment(FileConfiguration config, File file) {
        Map<String, Material> equipment = new HashMap<>();
        ConfigurationSection section = config.getConfigurationSection("equipment");
        if (section != null) {
            for (String key : section.getKeys(false)) {
                Material material = Material.matchMaterial(Objects.requireNonNull(section.getString(key)));
                if (material != null) {
                    equipment.put(key, material);
                } else {
                    plugin.getLogger().warning("Material inválido en " + file.getName() + ": " + section.getString(key));
                }
            }
        }
        return equipment;
    }

    private List<PotionEffect> loadPotionEffects(List<String> effectsList, File file) {
        List<PotionEffect> effects = new ArrayList<>();
        for (String entry : effectsList) {
            String[] parts = entry.split(":");
            if (parts.length >= 2) {
                PotionEffectType effectType = PotionEffectType.getByName(parts[0].toUpperCase());
                int level = parseInt(parts[1], 1) - 1;
                int duration = (parts.length == 3) ? parseInt(parts[2], 1) * 20 : Integer.MAX_VALUE;
                if (effectType != null) {
                    effects.add(new PotionEffect(effectType, duration, level, false, false));
                } else {
                    plugin.getLogger().warning("Efecto inválido en " + file.getName() + ": " + parts[0]);
                }
            } else {
                plugin.getLogger().warning("Formato incorrecto en efecto " + entry + " en " + file.getName());
            }
        }
        return effects;
    }

    private Map<String, Ability> loadAbilities(FileConfiguration config, File file) {
        Map<String, Ability> abilities = new HashMap<>();
        ConfigurationSection section = config.getConfigurationSection("abilities");
        if (section != null) {
            for (String key : section.getKeys(false)) {
                ConfigurationSection abilitySection = section.getConfigurationSection(key);
                if (abilitySection == null) continue;

                Material material = Material.matchMaterial(Objects.requireNonNull(abilitySection.getString("material")));
                if (material == null) {
                    plugin.getLogger().warning("Material inválido en habilidad " + key);
                    continue;
                }

                String type = abilitySection.getString("type", "SELF");
                List<String> actions = abilitySection.getStringList("actions");
                List<PotionEffect> effects = loadPotionEffects(abilitySection.getStringList("effects"), file);
                int cooldown = abilitySection.getInt("cooldown", 30);
                ParticleEffect particles = loadParticleEffect(abilitySection);
                SoundEffect sound = loadSoundEffect(abilitySection);

                abilities.put(key, new Ability(material, type, actions, effects, cooldown, particles, sound));
            }
        }
        return abilities;
    }

    private SoundEffect loadSoundEffect(ConfigurationSection config) {
        if (config == null) return null;
        boolean enabled = config.getBoolean("sound.enabled", false);
        String soundData = config.getString("sound.type", "");
        return parseSoundEffect(enabled, soundData, "sound");
    }

    private SoundEffect loadSoundEffect(FileConfiguration config, String path) {
        boolean enabled = config.getBoolean(path + ".enabled", false);
        String soundData = config.getString(path + ".type", "");
        return parseSoundEffect(enabled, soundData, path);
    }

    private SoundEffect parseSoundEffect(boolean enabled, String soundData, String path) {
        if (soundData.isEmpty()) return null;
        String[] parts = soundData.split(":");
        try {
            Sound sound = Sound.valueOf(parts[0].toUpperCase());
            float volume = (parts.length > 1) ? Float.parseFloat(parts[1]) : 1.0f;
            float pitch = (parts.length > 2) ? Float.parseFloat(parts[2]) : 1.0f;
            return new SoundEffect(enabled, sound, volume, pitch);
        } catch (IllegalArgumentException e) {
            plugin.getLogger().warning("Sonido inválido en " + path + ": " + soundData);
            return null;
        }
    }

    private ParticleEffect loadParticleEffect(ConfigurationSection config) {
        if (config == null) return null;
        boolean enabled = config.getBoolean("particles.enabled", false);
        String type = config.getString("particles.type", "");
        return parseParticleEffect(enabled, type, "particles");
    }

    private ParticleEffect loadParticleEffect(FileConfiguration config, String path) {
        boolean enabled = config.getBoolean(path + ".enabled", false);
        String type = config.getString(path + ".type", "");
        return parseParticleEffect(enabled, type, path);
    }

    private ParticleEffect parseParticleEffect(boolean enabled, String type, String path) {
        try {
            return type.isEmpty() ? null : new ParticleEffect(enabled, Particle.valueOf(type.toUpperCase()));
        } catch (IllegalArgumentException e) {
            plugin.getLogger().warning("Partícula inválida en " + path + ": " + type);
            return null;
        }
    }

    private int parseInt(String value, int defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }




}
