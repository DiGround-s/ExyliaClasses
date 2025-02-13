package net.diground.exyliaClasses.utils;

import net.diground.exyliaClasses.ExyliaClasses;

public class DebugUtils {
    public static void logDebug(String message){
        if (!ExyliaClasses.getInstance().getConfigManager().getConfig().getBoolean("debug")) return;
        ExyliaClasses.getInstance().getLogger().info("[DEBUG] " + message);
    }
    public static void logError(String message){
        ExyliaClasses.getInstance().getLogger().severe("[ERROR] " + message);
    }
    public static void logWarn(String message){
        ExyliaClasses.getInstance().getLogger().warning("[WARN] " + message);
    }
}
