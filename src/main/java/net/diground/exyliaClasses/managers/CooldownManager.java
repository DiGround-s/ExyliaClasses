package net.diground.exyliaClasses.managers;

import net.diground.exyliaClasses.models.Cooldown;
import net.diground.exyliaClasses.models.EClass;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.*;

/**
 * Maneja los cooldowns de los ítems para los jugadores.
 * Permite añadir, verificar, eliminar y listar cooldowns activos.
 */
public class CooldownManager {

    // Mapa para almacenar los cooldowns de cada jugador, usando su UUID
    private final Map<UUID, List<Cooldown>> playerCooldowns = new HashMap<>();

    /**
     * Constructor que inicializa las estructuras de datos para los cooldowns.
     */
    public CooldownManager() {
    }

    /**
     * Añade un cooldown para un ítem específico de un jugador.
     *
     * @param player   El jugador al que se le aplica el cooldown.
     * @param material El material del ítem al que se aplica el cooldown.
     * @param cooldown La duración del cooldown en segundos.
     */
    public void addCooldown(Player player, Material material, EClass eClass, double cooldown) {
        long cooldownMillis = (long) (cooldown * 1000); // Convierte el cooldown de segundos a milisegundos
        long expirationTime = System.currentTimeMillis() + cooldownMillis;

        Cooldown newCooldown = new Cooldown(player.getUniqueId(), material, expirationTime, eClass);

        // Añadir el cooldown a la lista de cooldowns del jugador
        playerCooldowns.computeIfAbsent(player.getUniqueId(), k -> new ArrayList<>()).add(newCooldown);

        // Aplicar el cooldown visual al ítem si se especifica un material
        player.setCooldown(material, (int) cooldown * 20); // El cooldown en Minecraft se mide en ticks (20 ticks = 1 segundo)
    }

    /**
     * Verifica si un cooldown está activo para un ítem específico de un jugador.
     *
     * @param player   El jugador a verificar.
     * @param material El material del ítem a verificar.
     * @param eClass   La clase del ítem a verificar.
     * @return true si el cooldown está activo, false si no lo está.
     */
    public boolean isCooldownActive(Player player, Material material, EClass eClass) {
        List<Cooldown> cooldowns = playerCooldowns.get(player.getUniqueId());
        if (cooldowns != null) {
            for (Cooldown cooldown : cooldowns) {
                if (cooldown.getMaterial() == material && cooldown.geteClass() == eClass) {
                    if (System.currentTimeMillis() < cooldown.getTime()) {
                        return true;
                    } else {
                        // El cooldown ha expirado, eliminarlo
                        removeCooldown(player, material, eClass);
                        return false;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Obtiene el tiempo restante de un cooldown en segundos.
     *
     * @param player   El jugador a verificar.
     * @param material El material del ítem a verificar.
     * @param eClass   La clase del ítem a verificar.
     * @return El tiempo restante del cooldown en segundos. Retorna 0 si no hay cooldown activo.
     */
    public double getRemainingCooldown(Player player, Material material, EClass eClass) {
        List<Cooldown> cooldowns = playerCooldowns.get(player.getUniqueId());
        if (cooldowns != null) {
            for (Cooldown cooldown : cooldowns) {
                if (cooldown.getMaterial() == material && cooldown.geteClass() == eClass) {
                    long remainingTime = cooldown.getTime() - System.currentTimeMillis();
                    if (remainingTime > 0) {
                        return remainingTime / 1000.0; // Convertir a segundos
                    } else {
                        // El cooldown ha expirado, eliminarlo
                        removeCooldown(player, material, eClass);
                        return 0;
                    }
                }
            }
        }
        return 0;
    }

    /**
     * Elimina un cooldown específico para un jugador y un ítem.
     *
     * @param player   El jugador del que se eliminará el cooldown.
     * @param material El material del ítem a verificar.
     * @param eClass   La clase del ítem a verificar.
     */
    public void removeCooldown(Player player, Material material, EClass eClass) {
        List<Cooldown> cooldowns = playerCooldowns.get(player.getUniqueId());
        if (cooldowns != null) {
            cooldowns.removeIf(cooldown -> cooldown.getMaterial() == material && cooldown.geteClass() == eClass);
        }
    }

    /**
     * Establece un nuevo cooldown para un ítem de un jugador.
     *
     * @param player   El jugador al que se le aplica el cooldown.
     * @param material El material del ítem a verificar.
     * @param eClass   La clase del ítem a verificar.
     * @param cooldown La duración del cooldown en segundos.
     */
    public void setCooldown(Player player, Material material, EClass eClass, double cooldown) {
        // Eliminar el cooldown existente antes de agregar uno nuevo
        removeCooldown(player, material, eClass);
        addCooldown(player, material, eClass, cooldown);
    }
}
