package net.diground.exyliaClasses;

//import net.diground.exyliaClasses.listeners.classes.ArcherListener;
//import net.diground.exyliaClasses.listeners.ArmorListener;
//import net.diground.exyliaClasses.listeners.PlayerListener;
//import net.diground.exyliaClasses.listeners.classes.MinerListener;
import net.diground.exyliaClasses.loaders.SpecialClassLoader;
import net.diground.exyliaClasses.managers.CooldownManager;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class ExyliaClasses extends JavaPlugin {

    private SpecialClassLoader specialClassLoader;
    private CooldownManager cooldownManager;

    @Override
    public void onEnable() {
        loadManagers();
        loadListeners();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void loadManagers() {
        specialClassLoader = new SpecialClassLoader(this);
        specialClassLoader.loadClasses();
        cooldownManager = new CooldownManager();
    }

    private void loadListeners() {
//        getServer().getPluginManager().registerEvents(new ArmorListener(this), this);
//        getServer().getPluginManager().registerEvents(new ArcherListener(this), this);
//        getServer().getPluginManager().registerEvents(new MinerListener(this), this);
//        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
    }

    public SpecialClassLoader getSpecialClassLoader() { return specialClassLoader; }

    public CooldownManager getCooldownManager() {
        return cooldownManager;
    }
}
