package net.diground.exyliaClasses;

import me.ulrich.clans.interfaces.UClans;
import net.diground.exyliaClasses.listeners.*;
import net.diground.exyliaClasses.loaders.SpecialClassLoader;
import net.diground.exyliaClasses.managers.*;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class ExyliaClasses extends JavaPlugin {

    private static ExyliaClasses instance;


    private ConfigManager configManager;
    private SpecialClassLoader specialClassLoader;
    private CooldownManager cooldownManager;
    private SpecialClassManager specialClassManager;
    private PlayerInfoManager playerInfoManager;
    private WarmupManager warmupManager;
    private ClanManager clanManager;

    private BukkitAudiences adventure;

    private UClans uClans;

    public BukkitAudiences adventure() {
        if(this.adventure == null) {
            throw new IllegalStateException("Tried to access Adventure when the plugin was disabled!");
        }
        return this.adventure;
    }
    @Override
    public void onEnable() {
        this.adventure = BukkitAudiences.create(this);
        instance = this;
        loadManagers();
        loadListeners();
    }

    @Override
    public void onDisable() {
        if(this.adventure != null) {
            this.adventure.close();
            this.adventure = null;
        }
    }

    private void loadManagers() {
        configManager = new ConfigManager(this);
        specialClassManager = new SpecialClassManager(this);
        specialClassLoader = new SpecialClassLoader(this, specialClassManager);
        specialClassLoader.loadClasses();
        warmupManager = new WarmupManager(this);
        playerInfoManager = new PlayerInfoManager();
        cooldownManager = new CooldownManager();
        clanManager = new ClanManager(this);
    }

    private void loadListeners() {
        getServer().getPluginManager().registerEvents(new ArmorListener(this), this);
        getServer().getPluginManager().registerEvents(new AbilityListener(this), this);
        getServer().getPluginManager().registerEvents(new HoldEffectListener(this), this);
        getServer().getPluginManager().registerEvents(new MarkListener(this), this);
        getServer().getPluginManager().registerEvents(new BackstabListener(this), this);
    }

    public SpecialClassLoader getSpecialClassLoader() { return specialClassLoader; }

    public CooldownManager getCooldownManager() {
        return cooldownManager;
    }

    public SpecialClassManager getSpecialClassManager() { return specialClassManager; }

    public PlayerInfoManager getPlayerInfoManager() { return playerInfoManager; }

    public WarmupManager getWarmupManager() { return warmupManager; }

    public ConfigManager getConfigManager() { return configManager; }

    public ClanManager getClanManager() { return clanManager; }

    public BukkitAudiences getAudience() {
        return adventure();
    }

    public static ExyliaClasses getInstance() {
        return instance;
    }


    public UClans getUClans() {
        if (uClans == null) {
            Plugin plugin = Bukkit.getPluginManager().getPlugin("UltimateClans");
            if (plugin instanceof UClans) {
                uClans = (UClans) plugin;
            } else {
                return null;
            }
        }
        return uClans;
    }
}
