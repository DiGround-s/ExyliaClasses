package net.diground.exyliaClasses.managers;

import me.ulrich.clans.data.ClanData;
import me.ulrich.clans.interfaces.UClans;
import net.diground.exyliaClasses.ExyliaClasses;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class ClanManager {
    private final ExyliaClasses plugin;

    public ClanManager(ExyliaClasses plugin) {
        this.plugin = plugin;
    }

    public List<Player> getClanAndAllyMembersInRadius(Player p, int radius) {
        List<Player> members = getClanMembersInRadius(p, radius);
        List<Player> allys = getAllyMembersInRadius(p, radius);

        List<Player> combinedList = new ArrayList<>(members);
        combinedList.addAll(allys);

        return combinedList;
    }

    public List<Player> getClanMembersInRadius(Player p, int radius) {
        UClans uClans = plugin.getUClans();
        if (uClans != null) {
            Optional<ClanData> optionalClan = uClans.getPlayerAPI().getPlayerClan(p.getUniqueId());
            if (optionalClan.isPresent()) {
                ClanData clanData = optionalClan.get();
                List<Player> nearbyMembers = new ArrayList<>();

                for (UUID memberUUID : clanData.getOnlineMembers()) {
                    Player member = Bukkit.getPlayer(memberUUID);
                    if (member != null && member.isOnline()) {
                        if (member.getLocation().getWorld().equals(p.getLocation().getWorld())) {
                            if (member.getLocation().distance(p.getLocation()) <= radius) {
                                nearbyMembers.add(member);
                            }
                        }
                    }
                }
                return nearbyMembers;
            }
        }
        return new ArrayList<>();
    }

    public List<Player> getAllyMembersInRadius(Player p, int radius) {
        UClans uClans = plugin.getUClans();
        if (uClans == null) {
            return Collections.emptyList();
        }

        Optional<ClanData> optionalClan = uClans.getPlayerAPI().getPlayerClan(p.getUniqueId());
        if (optionalClan.isEmpty()) {
            return Collections.emptyList();
        }

        ClanData clanData = optionalClan.get();
        List<Player> nearbyMembers = new ArrayList<>();

        for (UUID allyUUID : clanData.getRivalAlly().getAlly()) {
            Optional<ClanData> allyOptionalClan = uClans.getClanAPI().getClan(allyUUID);
            if (allyOptionalClan.isEmpty()) {
                continue;
            }

            ClanData allyClan = allyOptionalClan.get();
            for (UUID memberUUID : allyClan.getOnlineMembers()) {
                Player member = Bukkit.getPlayer(memberUUID);
                if (member != null && member.isOnline()) {
                    if (member.getLocation().getWorld().equals(p.getLocation().getWorld())) {
                        if (member.getLocation().distance(p.getLocation()) <= radius) {
                            nearbyMembers.add(member);
                        }
                    }
                }
            }
        }

        return nearbyMembers;
    }

    public List<Player> getEnemyMembersInRadius(Player p, int radius) {
        UClans uClans = plugin.getUClans();
        if (uClans != null) {
            Optional<ClanData> optionalClan = uClans.getPlayerAPI().getPlayerClan(p.getUniqueId());
            List<UUID> clanMembers = optionalClan.map(ClanData::getMembers).orElse(Collections.emptyList());

            List<Player> enemyPlayers = new ArrayList<>();
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (!clanMembers.contains(onlinePlayer.getUniqueId())) {
                    if (onlinePlayer.getLocation().getWorld().equals(p.getLocation().getWorld())) {
                        if (onlinePlayer.getLocation().distance(p.getLocation()) <= radius) {
                            enemyPlayers.add(onlinePlayer);
                        }
                    }
                }
            }
            return enemyPlayers;
        }
        return new ArrayList<>();
    }
}
