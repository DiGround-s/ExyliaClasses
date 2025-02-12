//package net.diground.exyliaClasses.listeners.classes;
//
//import net.diground.exyliaClasses.ExyliaClasses;
//import net.diground.exyliaClasses.models.SpecialClass;
//import net.diground.exyliaClasses.models.classes.Archer;
//import org.bukkit.entity.Arrow;
//import org.bukkit.entity.Player;
//import org.bukkit.event.EventHandler;
//import org.bukkit.event.Listener;
//import org.bukkit.event.entity.EntityDamageByEntityEvent;
//import org.bukkit.potion.PotionEffect;
//import org.bukkit.potion.PotionEffectType;
//import org.bukkit.scheduler.BukkitRunnable;
//import org.jetbrains.annotations.NotNull;
//
//import java.util.List;
//
//public class ArcherListener extends AbstractClassListener implements Listener {
//    private final ExyliaClasses plugin;
//
//    private final Archer archer;
//    private final SpecialClass eClass;
//
//    public ArcherListener(ExyliaClasses plugin) {
//        super(plugin, "Archer");
//        this.archer = plugin.getClassManager().getArcher();
//        this.eClass = plugin.getClassManager().getClassByName("Archer");
//        this.plugin = plugin;
//    }
//
//    @Override
//    protected List<String> getActiveEffects() {
//        return archer.getActiveEffects();
//    }
//
//    @EventHandler
//    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
//        if (event.isCancelled() || event.getDamage() <= 0) {
//            return;
//        }
//
//        if (event.getDamager() instanceof Arrow arrow && event.getEntity() instanceof Player damaged) {
//            if (arrow.getShooter() instanceof Player attacker) {
//                PlayerInfo attackerInfo = plugin.getClassManager().getPlayerInfo(attacker);
//                if (attackerInfo != null) {
//                    if (attackerInfo.getPlayerClass() != null && "Archer".equals(attackerInfo.getPlayerClass().getName())) {
//                        PlayerInfo damagedInfo = plugin.getClassManager().getPlayerInfo(damaged);
//                        if (damagedInfo != null && damagedInfo.getPlayerClass() != null) {
//                            if ("Archer".equals(damagedInfo.getPlayerClass().getName())) {
//                                if (archer.isCanTagOthersArchers()) {
//                                    addArrowCharge(attacker, damaged);
//                                } else {
//                                    attacker.sendMessage("¡No puedes atacar a otros arqueros!");
//                                }
//                            } else {
//                                addArrowCharge(attacker, damaged);
//                            }
//                        } else {
//                            addArrowCharge(attacker, damaged);
//                        }
//                    }
//                }
//            }
//        } 1121qwwwwwwwwwwwwwwwwwwwwww WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW11z CXR%TGFY^fa^^^^Q^^^^Fqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq
//    }
//
//    private void addArrowCharge(Player attacker, Player damaged) {
//        PlayerInfo damagedInfo = plugin.getClassManager().getPlayerInfo(damaged);
//        if (damagedInfo == null) {
//            return;
//        }
//
//        if (archer.isApplyGlow()) {
//            damaged.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, (int) (archer.getTagTime() * 20), 0, true, false));
//        }
//
//        if (archer.isRemoveInvisibility()) {
//            damaged.removePotionEffect(PotionEffectType.INVISIBILITY);
//        }
//
//        int currentCharges = damagedInfo.getArcherArrowCharge();
//        if (currentCharges >= archer.getMaxCharges()) {
//            damagedInfo.getTagTask().cancel();
//            BukkitRunnable newTagTask = getTagRunnable(damaged, damagedInfo);
//            newTagTask.runTaskTimer(plugin, 0L, 2L);
//            damagedInfo.setTagTask(newTagTask);
//            return;
//        }
//
//        if (damagedInfo.getTagTask() != null) {
//            damagedInfo.getTagTask().cancel();
//        }
//
//        damagedInfo.setArcherArrowCharge(currentCharges + 1);
//        BukkitRunnable newTagTask = getTagRunnable(damaged, damagedInfo);
//        newTagTask.runTaskTimer(plugin, 0L, 2L);
//        damagedInfo.setTagTask(newTagTask);
//    }
//
//    @EventHandler
//    private void onPlayerAttack(EntityDamageByEntityEvent event) {
//        if (event.getEntity() instanceof Player damaged) {
//            PlayerInfo damagedInfo = plugin.getClassManager().getPlayerInfo(damaged);
//            if (damagedInfo == null || damagedInfo.getArcherArrowCharge() == 0) {
//                return;
//            }
//
//            double damageMultiplier = damagedInfo.getArcherArrowCharge() * archer.getTagExtraDamage();
//            double damage = event.getDamage() * damageMultiplier;
//            damaged.sendMessage("Recibiste un daño adicional desde " + event.getDamage() + " a " + damage + " de daño, tienes " + damagedInfo.getArcherArrowCharge() + " cargas");
//            event.setDamage(damage);
//        }
//    }
//
//    private @NotNull BukkitRunnable getTagRunnable(Player player, PlayerInfo playerInfo) {
//        return new BukkitRunnable() {
//            double timeRemaining = archer.getTagTime();
//
//            @Override
//            public void run() {
//                if (timeRemaining <= 0) {
//                    playerInfo.setArcherArrowCharge(0);
//                    player.sendMessage("¡Has perdido tus cargas!");
//                    player.removePotionEffect(PotionEffectType.GLOWING);
//                    cancel();
//                    return;
//                }
//
//                timeRemaining -= 0.1;
//            }
//        };
//    }
//}