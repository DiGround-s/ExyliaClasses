package net.diground.exyliaClasses;

import net.diground.exyliaClasses.listeners.classes.ArcherListener;
import net.diground.exyliaClasses.listeners.ArmorListener;
import net.diground.exyliaClasses.listeners.PlayerListener;
import net.diground.exyliaClasses.listeners.classes.MinerListener;
import net.diground.exyliaClasses.managers.ClassManager;
import net.diground.exyliaClasses.managers.CooldownManager;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class ExyliaClasses extends JavaPlugin {

    private ClassManager classManager;
    private CooldownManager cooldownManager;

    @Override
    public void onEnable() {
        loadManagers();
        loadListeners();
        for (Player player : getServer().getOnlinePlayers()) {
            classManager.addPlayerInfo(player);
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void loadManagers() {
        classManager = new ClassManager();
        classManager.loadClasses();
        cooldownManager = new CooldownManager();
    }

    private void loadListeners() {
        getServer().getPluginManager().registerEvents(new ArmorListener(this), this);
        getServer().getPluginManager().registerEvents(new ArcherListener(this), this);
        getServer().getPluginManager().registerEvents(new MinerListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
    }

    public ClassManager getClassManager() {
        return classManager;
    }

    public CooldownManager getCooldownManager() {
        return cooldownManager;
    }
}
