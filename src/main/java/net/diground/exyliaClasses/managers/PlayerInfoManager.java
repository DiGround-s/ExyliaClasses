package net.diground.exyliaClasses.managers;

import net.diground.exyliaClasses.models.PlayerInfo;
import net.diground.exyliaClasses.models.SpecialClass;
import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.Map;

public class PlayerInfoManager {
    private final Map<Player, PlayerInfo> playerInfoMap;

    public PlayerInfoManager() {
        this.playerInfoMap = new HashMap<>();
    }

    public PlayerInfo getPlayerInfo(Player player) {
        return playerInfoMap.computeIfAbsent(player, PlayerInfo::new);
    }

    public void setPlayerClass(Player player, SpecialClass newClass) {
        getPlayerInfo(player).setCurrentClass(newClass);
    }

    public SpecialClass getPlayerClass(Player player) {
        PlayerInfo playerInfo = playerInfoMap.get(player);
        return (playerInfo != null) ? playerInfo.getCurrentClass() : null;
    }

    public void removePlayerInfo(Player player) {
        playerInfoMap.remove(player);
    }
}
