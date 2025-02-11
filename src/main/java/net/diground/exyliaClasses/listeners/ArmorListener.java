//package net.diground.exyliaClasses.listeners;
//
//import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
//import net.diground.exyliaClasses.ExyliaClasses;
//import net.diground.exyliaClasses.models.SpecialClass;
//import org.bukkit.Material;
//import org.bukkit.entity.Player;
//import org.bukkit.event.EventHandler;
//import org.bukkit.event.Listener;
//import org.bukkit.potion.PotionEffect;
//import org.bukkit.potion.PotionEffectType;
//import org.bukkit.scheduler.BukkitRunnable;
//import org.jetbrains.annotations.NotNull;
//
//import java.util.Optional;
//
//public class ArmorListener implements Listener {
//
//    private final ExyliaClasses plugin;
//
//    public ArmorListener(ExyliaClasses plugin) {
//        this.plugin = plugin;
//    }
//
//    @EventHandler
//    private void onArmorEquip(PlayerArmorChangeEvent event) {
//        Player player = event.getPlayer();
//        PlayerInfo playerInfo = plugin.getClassManager().getPlayerInfo(player);
//
//        if (playerInfo == null) {
//            playerInfo = new PlayerInfo(player);
//            plugin.getClassManager().addPlayerInfo(player);
//        }
//
//        Material helmet = Optional.ofNullable(player.getInventory().getHelmet()).map(item -> item.getType()).orElse(Material.AIR);
//        Material chestplate = Optional.ofNullable(player.getInventory().getChestplate()).map(item -> item.getType()).orElse(Material.AIR);
//        Material leggings = Optional.ofNullable(player.getInventory().getLeggings()).map(item -> item.getType()).orElse(Material.AIR);
//        Material boots = Optional.ofNullable(player.getInventory().getBoots()).map(item -> item.getType()).orElse(Material.AIR);
//
//        if (helmet == Material.LEATHER_HELMET && chestplate == Material.LEATHER_CHESTPLATE && leggings == Material.LEATHER_LEGGINGS && boots == Material.LEATHER_BOOTS) {
//            startClassWarmup(player, plugin.getClassManager().getClassByName("Archer"), playerInfo);
//        } else if (helmet == Material.GOLDEN_HELMET && chestplate == Material.GOLDEN_CHESTPLATE && leggings == Material.GOLDEN_LEGGINGS && boots == Material.GOLDEN_BOOTS) {
//            startClassWarmup(player, plugin.getClassManager().getClassByName("Bard"), playerInfo);
//        } else if (helmet == Material.IRON_HELMET && chestplate == Material.IRON_CHESTPLATE && leggings == Material.IRON_LEGGINGS && boots == Material.IRON_BOOTS) {
//            startClassWarmup(player, plugin.getClassManager().getClassByName("Miner"), playerInfo);
//        } else if (helmet == Material.DIAMOND_HELMET && chestplate == Material.DIAMOND_CHESTPLATE && leggings == Material.DIAMOND_LEGGINGS && boots == Material.DIAMOND_BOOTS) {
//            startClassWarmup(player, plugin.getClassManager().getClassByName("Sorcerer"), playerInfo);
//        } else if (helmet == Material.CHAINMAIL_HELMET && chestplate == Material.CHAINMAIL_CHESTPLATE && leggings == Material.CHAINMAIL_LEGGINGS && boots == Material.CHAINMAIL_BOOTS) {
//            startClassWarmup(player, plugin.getClassManager().getClassByName("Rogue"), playerInfo);
//        } else {
//            cancelEffects(player, playerInfo);
//            playerInfo.setPlayerClass(null);
//        }
//    }
//
//    private void startClassWarmup(Player player, SpecialClass playerEClass, PlayerInfo playerInfo) {
//        if (!playerInfo.isInWarmup()) {
//            playerInfo.setInWarmup(true);
//            BukkitRunnable task = getBukkitRunnable(player, playerEClass, playerInfo);
//            task.runTaskTimer(plugin, 0L, 2L);
//        }
//    }
//
//    private @NotNull BukkitRunnable getBukkitRunnable(Player player, SpecialClass playerEClass, PlayerInfo playerInfo) {
//        return new BukkitRunnable() {
//            double timeRemaining = playerEClass.getWarmupTime();
//
//            @Override
//            public void run() {
//                timeRemaining = Math.round(timeRemaining * 10) / 10.0;
//                player.sendMessage("¡Cambia de clase en " + timeRemaining + " segundos!");
//                if (!isWearingCorrectArmor(player, playerEClass)) {
//                    player.sendMessage("Has cambiado tu armadura. El calentamiento se ha cancelado.");
//                    playerInfo.setInWarmup(false);
//                    cancel();
//                    return;
//                }
//
//                if (timeRemaining <= 0.0) {
//                    playerInfo.setPlayerClass(playerEClass);
//                    playerInfo.setInWarmup(false);
//                    player.sendMessage("¡Has cambiado de clase a " + playerEClass.getDisplayName() + "!");
//                    playerInfo.savePreviousEffects(player.getActivePotionEffects());
//                    startEffects(player, playerInfo);
//                    cancel();
//                }
//
//                timeRemaining -= 0.1;
//            }
//        };
//    }
//
//    private static boolean isWearingCorrectArmor(Player player, SpecialClass playerEClass) {
//        Material helmet = Optional.ofNullable(player.getInventory().getHelmet()).map(item -> item.getType()).orElse(Material.AIR);
//        Material chestplate = Optional.ofNullable(player.getInventory().getChestplate()).map(item -> item.getType()).orElse(Material.AIR);
//        Material leggings = Optional.ofNullable(player.getInventory().getLeggings()).map(item -> item.getType()).orElse(Material.AIR);
//        Material boots = Optional.ofNullable(player.getInventory().getBoots()).map(item -> item.getType()).orElse(Material.AIR);
//
//        return switch (playerEClass.getName()) {
//            case "Archer" ->
//                    helmet == Material.LEATHER_HELMET && chestplate == Material.LEATHER_CHESTPLATE && leggings == Material.LEATHER_LEGGINGS && boots == Material.LEATHER_BOOTS;
//            case "Bard" ->
//                    helmet == Material.GOLDEN_HELMET && chestplate == Material.GOLDEN_CHESTPLATE && leggings == Material.GOLDEN_LEGGINGS && boots == Material.GOLDEN_BOOTS;
//            case "Rogue" ->
//                    helmet == Material.CHAINMAIL_HELMET && chestplate == Material.CHAINMAIL_CHESTPLATE && leggings == Material.CHAINMAIL_LEGGINGS && boots == Material.CHAINMAIL_BOOTS;
//            case "Miner" ->
//                    helmet == Material.IRON_HELMET && chestplate == Material.IRON_CHESTPLATE && leggings == Material.IRON_LEGGINGS && boots == Material.IRON_BOOTS;
//            case "Sorcerer" ->
//                    helmet == Material.DIAMOND_HELMET && chestplate == Material.DIAMOND_CHESTPLATE && leggings == Material.DIAMOND_LEGGINGS && boots == Material.DIAMOND_BOOTS;
//            default -> false;
//        };
//    }
//
//    private void startEffects(Player player, PlayerInfo playerInfo) {
//        for (String potionEffect : playerInfo.getPlayerClass().getPassiveEffects()) {
//            String[] effect = potionEffect.split(":");
//            PotionEffectType effectType = PotionEffectType.getByName(effect[0]);
//            int duration = Integer.parseInt(effect[1]) * 20;
//            int amplifier = Integer.parseInt(effect[2]);
//            if (duration == -20) {
//                duration = -1;
//            }
//            assert effectType != null;
//            player.addPotionEffect(new PotionEffect(effectType, duration, amplifier));
//        }
//    }
//
//    private void cancelEffects(Player player, PlayerInfo playerInfo) {
//        if (playerInfo.getPlayerClass() != null) {
//            for (String potionEffect : playerInfo.getPlayerClass().getPassiveEffects()) {
//                String[] effect = potionEffect.split(":");
//                PotionEffectType effectType = PotionEffectType.getByName(effect[0]);
//                assert effectType != null;
//                player.removePotionEffect(effectType);
//            }
//            playerInfo.restorePreviousEffects();
//        }
//    }
//}