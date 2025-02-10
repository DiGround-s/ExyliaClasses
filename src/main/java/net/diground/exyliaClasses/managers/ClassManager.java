package net.diground.exyliaClasses.managers;

import net.diground.exyliaClasses.models.EClass;
import net.diground.exyliaClasses.models.PlayerInfo;
import net.diground.exyliaClasses.models.classes.Archer;
import net.diground.exyliaClasses.models.classes.Miner;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassManager {
    private Map<Player, PlayerInfo> playerInfoMap = new HashMap<>();
    private final Map<String, EClass> classes = new HashMap<>();

    public void loadClasses() {
        classes.put("Archer", new Archer("Archer", "Arquero", List.of("SPEED:-1:2","DAMAGE_RESISTANCE:-1:1"), 2, List.of("SUGAR:SPEED:6:3:3","IRON_INGOT:DAMAGE_RESISTANCE:6:2:25","FEATHER:JUMP:6:8:3"), 10, 10, 3, 8, false, null, true, true));
        classes.put("Miner", new Miner("Miner", "Minero", List.of("SPEED:-1:0","SATURATION:-1:0","NIGHT_VISION:-1:0","FAST_DIGGING:-1:1"), 2, List.of("IRON_INGOT:FAST_DIGGING:6:2:3")));
    }

    public EClass getClassByName(String name) {
        return classes.get(name);
    }

    public Archer getArcher() {
        return (Archer) classes.get("Archer");
    }
    public Miner getMiner() {
        return (Miner) classes.get("Miner");
    }

    public void addPlayerInfo(Player player) {
        PlayerInfo playerInfo = new PlayerInfo(player);
        playerInfoMap.put(player, playerInfo);
        Bukkit.getLogger().info("Added player info for " + player.getName());
    }

    // Obtiene la información del jugador
    public PlayerInfo getPlayerInfo(Player player) {
        return playerInfoMap.get(player);
    }

    // Elimina la información del jugador cuando sale o ya no se requiere
    public void removePlayerInfo(Player player) {
        playerInfoMap.remove(player);
        Bukkit.getLogger().info("Removed player info for " + player.getName());
    }
}
