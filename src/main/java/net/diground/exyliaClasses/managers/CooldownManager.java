package net.diground.exyliaClasses.managers;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * Maneja los cooldowns de los ítems para los jugadores.
 * Permite añadir, verificar, eliminar y listar cooldowns activos.
 */
public class CooldownManager {
    // Mapa para almacenar los cooldowns activos de cada jugador, donde el UUID del jugador es la clave
    private final Map<UUID, Map<String, Long>> cooldowns;

    // Conjunto que almacena los UUIDs de los jugadores que tienen cooldowns activos
    private final Set<UUID> activeCooldowns;

    /**
     * Constructor que inicializa las estructuras de datos para los cooldowns.
     */
    public CooldownManager() {
        this.cooldowns = new HashMap<>();
        this.activeCooldowns = new HashSet<>();
    }

    /**
     * Añade un cooldown para un ítem específico de un jugador.
     *
     * @param player El jugador al que se le aplica el cooldown.
     * @param itemName El nombre del ítem en cooldown.
     * @param cooldown La duración del cooldown en segundos.
     * @param material El material relacionado con el ítem para aplicar cooldown visual (opcional).
     */
    public void addCooldown(Player player, String itemName, double cooldown, Material material) {
        long cooldownMillis = (long) (cooldown * 1000); // Convierte el cooldown de segundos a milisegundos
        long expirationTime = System.currentTimeMillis() + cooldownMillis;

        // Añadir o actualizar el cooldown en el mapa
        cooldowns.computeIfAbsent(player.getUniqueId(), k -> new HashMap<>()).put(itemName, expirationTime);

        // Aplicar el cooldown visual al ítem si se especifica un material
        if (material != null) {
            player.setCooldown(material, (int) cooldown * 20); // El cooldown en Minecraft se mide en ticks (20 ticks = 1 segundo)
        }

        // Añadir el jugador al conjunto de cooldowns activos
        activeCooldowns.add(player.getUniqueId());
    }

    /**
     * Verifica si un cooldown está activo para un ítem específico de un jugador.
     *
     * @param player El jugador a verificar.
     * @param itemName El nombre del ítem a verificar.
     * @return true si el cooldown está activo, false si no lo está.
     */
    public boolean isCooldownActive(Player player, String itemName) {
        Map<String, Long> playerCooldowns = cooldowns.get(player.getUniqueId());
        if (playerCooldowns == null) {
            return false; // No hay cooldowns para este jugador
        }

        Long expirationTime = playerCooldowns.get(itemName);
        if (expirationTime == null) {
            return false; // No hay cooldown para este ítem
        }

        // Verificar si el cooldown no ha expirado
        return System.currentTimeMillis() < expirationTime;
    }

    /**
     * Obtiene el tiempo restante de un cooldown en segundos.
     *
     * @param player El jugador a verificar.
     * @param itemName El nombre del ítem a verificar.
     * @return El tiempo restante del cooldown en segundos. Retorna 0 si no hay cooldown activo.
     */
    public double getRemainingCooldown(Player player, String itemName) {
        Map<String, Long> playerCooldowns = cooldowns.get(player.getUniqueId());
        if (playerCooldowns == null) {
            return 0;
        }

        Long expirationTime = playerCooldowns.get(itemName);
        if (expirationTime == null) {
            return 0;
        }

        long currentTime = System.currentTimeMillis();
        long remainingMillis = expirationTime - currentTime;

        // Convertir milisegundos a segundos y redondear a un decimal
        double remainingSeconds = remainingMillis > 0 ? remainingMillis / 1000.0 : 0;

        return Math.round(remainingSeconds * 10) / 10.0;
    }

    /**
     * Elimina un cooldown específico para un jugador y un ítem.
     *
     * @param player El jugador del que se eliminará el cooldown.
     * @param itemName El nombre del ítem cuyo cooldown se eliminará.
     * @param material El material relacionado para reiniciar el cooldown visual (opcional).
     */
    public void removeCooldown(Player player, String itemName, Material material) {
        Map<String, Long> playerCooldowns = cooldowns.get(player.getUniqueId());
        if (playerCooldowns != null) {
            playerCooldowns.remove(itemName);
            if (playerCooldowns.isEmpty()) {
                cooldowns.remove(player.getUniqueId());
                activeCooldowns.remove(player.getUniqueId());

                // Reiniciar el cooldown visual del material
                if (material != null) {
                    player.setCooldown(material, 1);
                }
            }
        }
    }


    /**
     * Establece un nuevo cooldown para un ítem de un jugador.
     *
     * @param player El jugador al que se le aplica el cooldown.
     * @param itemName El nombre del ítem en cooldown.
     * @param cooldown La duración del cooldown en segundos.
     * @param material El material relacionado con el ítem para aplicar cooldown visual (opcional).
     */
    public void setCooldown(Player player, String itemName, double cooldown, Material material) {
        long cooldownMillis = (long) (cooldown * 1000); // Convierte el cooldown de segundos a milisegundos
        long expirationTime = System.currentTimeMillis() + cooldownMillis;
        cooldowns.computeIfAbsent(player.getUniqueId(), k -> new HashMap<>()).put(itemName, expirationTime);

        // Aplicar el cooldown visual al ítem si se especifica un material
        if (material != null) {
            player.setCooldown(material, (int) cooldown * 20); // El cooldown en Minecraft se mide en ticks (20 ticks = 1 segundo)
        }
    }

    /**
     * Obtiene una lista de ítems que están en cooldown para un jugador específico.
     *
     * @param player El jugador a verificar.
     * @return Una lista de nombres de ítems que aún están en cooldown.
     */
    public List<String> getItemsInCooldown(Player player) {
        List<String> itemsInCooldown = new ArrayList<>();
        Map<String, Long> playerCooldowns = cooldowns.get(player.getUniqueId());

        if (playerCooldowns != null) {
            long currentTime = System.currentTimeMillis();

            // Recorrer todos los cooldowns del jugador
            for (Map.Entry<String, Long> entry : playerCooldowns.entrySet()) {
                String itemName = entry.getKey();
                Long expirationTime = entry.getValue();

                // Añadir a la lista si el cooldown aún está activo
                if (currentTime < expirationTime) {
                    itemsInCooldown.add(itemName);
                }
            }
        }

        return itemsInCooldown;
    }

    /**
     * Obtiene el conjunto de jugadores que tienen cooldowns activos.
     *
     * @return Un conjunto de UUIDs de jugadores con cooldowns activos.
     */
    public Set<UUID> getActiveCooldowns() {
        return activeCooldowns;
    }
}
