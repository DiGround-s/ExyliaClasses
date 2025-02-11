package net.diground.exyliaSpecials.managers;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ConfigManager {
    private final JavaPlugin plugin;
    private final Map<String, FileConfiguration> configs = new HashMap<>();
    private final Map<String, File> configFiles = new HashMap<>();

    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
        loadConfig("config.yml");
        loadConfig("messages.yml");
    }

    private void loadConfig(String fileName) {
        File file = new File(plugin.getDataFolder(), fileName);
        configFiles.put(fileName, file); // Guardar referencia al archivo
        if (!file.exists()) {
            plugin.saveResource(fileName, false);
        }
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        configs.put(fileName, config);
        Bukkit.getLogger().info("Archivo " + fileName + " cargado correctamente.");
    }

    public FileConfiguration getMessages() {
        return configs.get("messages.yml");
    }

    public FileConfiguration getConfig() {
        return configs.get("config.yml");
    }

    public void saveLocations() {
        File file = configFiles.get("locations.yml");
        FileConfiguration config = configs.get("locations.yml");
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reloadConfig(String fileName) {
        File file = new File(plugin.getDataFolder(), fileName);
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        configs.put(fileName, config);
    }

    public void reloadAllConfigs() {
        for (String fileName : configs.keySet()) {
            reloadConfig(fileName);
        }
    }
}
