package net.diground.exyliaClasses.loaders;

import net.diground.exyliaClasses.ExyliaClasses;
import net.diground.exyliaClasses.managers.SpecialClassManager;
import net.diground.exyliaClasses.models.*;
import net.diground.exyliaClasses.models.utils.ParticleEffect;
import net.diground.exyliaClasses.models.utils.SoundEffect;
import net.diground.exyliaClasses.models.weapons.BackstabWeapon;
import net.diground.exyliaClasses.models.weapons.MarkWeapon;
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

import static net.diground.exyliaClasses.loaders.WeaponLoader.loadWeapons;

public class SpecialClassLoader {
    private final ExyliaClasses plugin;
    private final SpecialClassManager specialClassManager;

    public SpecialClassLoader(ExyliaClasses plugin, SpecialClassManager specialClassManager) {
        this.plugin = plugin;
        this.specialClassManager = specialClassManager;
    }

    public void reloadClasses() {
        specialClassManager.clear();
        loadClasses();
        plugin.getLogger().info("Las clases especiales han sido recargadas correctamente.");
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
        Map<String, Ability> abilities = loadAbilities(id, config, file);
        Map<String, HoldEffect> holdEffects = loadHoldEffects(config, file);
        Energy energy = loadEnergy(config);

        // cargar armas
        Map<String, Weapon> weapons = loadWeapons(config);

        // Debugging
        plugin.getLogger().info("Clase cargada: " + id);
        SpecialClass specialClass = new SpecialClass(id, displayName, permission, equipment, passiveEffects, warmup, abilities, holdEffects, energy, weapons);
        specialClassManager.getClassMap().put(id, specialClass);
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
            String[] parts = entry.split("\\|");
            if (parts.length >= 2) {
                PotionEffectType effectType = PotionEffectType.getByName(parts[0].toUpperCase());
                int level = parseInt(parts[1], 1) - 1;
                int duration = (parts.length == 3) ? parseInt(parts[2], 1) * 20 : -1;
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

    private Map<String, Ability> loadAbilities(String classId, FileConfiguration config, File file) {
        Map<String, Ability> abilities = new HashMap<>();
        ConfigurationSection section = config.getConfigurationSection("abilities");
        if (section != null) {
            for (String key : section.getKeys(false)) {
                ConfigurationSection abilitySection = section.getConfigurationSection(key);
                if (abilitySection == null) continue;

                String id = key + "_" + classId;
                String displayName = abilitySection.getString("display_name", "ABILITY");
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

                int radius = abilitySection.getInt("radius", 0);
                int energyCost = abilitySection.getInt("energy_cost", 0);

                abilities.put(key, new Ability(id, displayName, material, type, actions, effects, cooldown, particles, sound, radius, energyCost));
            }
        }
        return abilities;
    }

    private Map<String, HoldEffect> loadHoldEffects(FileConfiguration config, File file) {
        Map<String, HoldEffect> holdEffects = new HashMap<>();
        ConfigurationSection section = config.getConfigurationSection("hold_effects");
        if (section != null) {
            for (String key : section.getKeys(false)) {
                ConfigurationSection effectSection = section.getConfigurationSection(key);
                if (effectSection == null) continue;

                String materialName = effectSection.getString("material", "BLAZE_POWDER");
                Material material = Material.matchMaterial(materialName);
                if (material == null) {
                    plugin.getLogger().warning("Material inválido en efecto de 'hold' " + key + " en " + file.getName() + ": " + materialName);
                    continue;
                }

                String type = effectSection.getString("type", "TO_CLAN_AND_ALLY");
                int radius = effectSection.getInt("radius", 35);
                List<PotionEffect> effects = loadPotionEffects(effectSection.getStringList("effects"), file);

                holdEffects.put(key, new HoldEffect(material, type, radius, effects));
            }
        }
        return holdEffects;
    }

    private Energy loadEnergy(FileConfiguration config) {
        boolean enabled = config.getBoolean("energy.enabled", false);
        double maxEnergy = config.getDouble("energy.max_energy", 100.0);
        int regenDelay = config.getInt("energy.regen_delay", 1);
        double regenQuantity = config.getDouble("energy.regen_quantity", 0.1);

        return new Energy(enabled, maxEnergy, regenDelay, regenQuantity);
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
        String[] parts = soundData.split("\\|");
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
        String particleConfig = config.getString("particles.type", "");

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
            plugin.getLogger().warning("Partícula inválida en " + config + ": " + particleType);
            plugin.getLogger().warning("Error: " + e.getMessage());
            return null;
        }
    }

    private ParticleEffect loadParticleEffect(FileConfiguration config, String path) {
        boolean enabled = config.getBoolean(path + ".enabled", false);
        String particleConfig = config.getString(path + ".type", "");

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
            plugin.getLogger().warning("Partícula inválida en " + path + ": " + particleType);
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
