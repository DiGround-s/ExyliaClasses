package net.diground.exyliaClasses.extensions;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.diground.exyliaClasses.ExyliaClasses;
import net.diground.exyliaClasses.models.PlayerInfo;
import net.diground.exyliaClasses.models.weapons.MarkEffect;
import net.diground.exyliaClasses.models.weapons.MarkWeapon;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static net.diground.exyliaClasses.loaders.WeaponLoader.getMarkWeapons;

public class PlaceholderAPI extends PlaceholderExpansion {

    private final ExyliaClasses plugin;

    public PlaceholderAPI(ExyliaClasses plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "classes";
    }

    @Override
    public @NotNull String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String identifier) {
        if (player == null) {
            return "N/A";
        }
        if (identifier.toLowerCase().startsWith("mark_")) {
            PlayerInfo playerInfo = plugin.getPlayerInfoManager().getPlayerInfo(player);
            if (playerInfo == null) return "";
            int currentMarks = playerInfo.getMarks();
            if (currentMarks <= 0) return "";
            MarkEffect markEffect = null;
            for (MarkWeapon weapon : getMarkWeapons()) {
                markEffect = weapon.getMarks().get(currentMarks);
                if (markEffect != null) {
                    break;
                }
            }

            if (markEffect == null) return "";
            return switch (identifier.toLowerCase()) {
                case "mark_color" -> markEffect.getColor();
                case "mark_icon" -> markEffect.getIcon();
                case "mark_number" -> String.valueOf(markEffect.getMarkNumber());
                case "mark_extra_damage" -> String.valueOf(markEffect.getExtraDamage());
                default -> "N/A";
            };
        }
        if (identifier.toLowerCase().startsWith("energy_")) {
            PlayerInfo playerInfo = plugin.getPlayerInfoManager().getPlayerInfo(player);
            if (playerInfo == null) return "";
            if (playerInfo.getCurrentClass() == null) return "";
            if (!playerInfo.getCurrentClass().getEnergy().isEnabled()) return "";

            return switch (identifier.toLowerCase()) {
                case "energy_count" -> String.valueOf(playerInfo.getActualEnergy());
                case "energy_count_display" -> playerInfo.getCurrentClass().getEnergy().getDisplay().replace("%energy%", String.valueOf(playerInfo.getActualEnergy()));
                default -> "N/A";
            };

        }
        return "N/A";
    }
}
