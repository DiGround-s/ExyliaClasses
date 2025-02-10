package net.diground.exyliaClasses.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class ColorUtils {

    private static final Pattern HEX_PATTERN = Pattern.compile("#[0-9A-Fa-f]{6}");
    private static final Pattern LEGACY_PATTERN = Pattern.compile("&[0-9a-fk-or]");
    private static final Pattern CUSTOM_HEX_PATTERN = Pattern.compile("<#[0-9A-Fa-f]{6}>");
    private static final Map<Character, String> COLOR_MAP;

    static {
        Map<Character, String> map = new HashMap<>();
        map.put('0', "<black>");
        map.put('1', "<dark_blue>");
        map.put('2', "<dark_green>");
        map.put('3', "<dark_aqua>");
        map.put('4', "<dark_red>");
        map.put('5', "<dark_purple>");
        map.put('6', "<gold>");
        map.put('7', "<gray>");
        map.put('8', "<dark_gray>");
        map.put('9', "<blue>");
        map.put('a', "<green>");
        map.put('b', "<aqua>");
        map.put('c', "<red>");
        map.put('d', "<light_purple>");
        map.put('e', "<yellow>");
        map.put('f', "<white>");
        map.put('k', "<obfuscated>");
        map.put('l', "<bold>");
        map.put('m', "<strikethrough>");
        map.put('n', "<underline>");
        map.put('o', "<italic>");
        map.put('r', "<reset>");
        COLOR_MAP = Map.copyOf(map); // Hacer el mapa inmutable
    }

    public static Component translateColors(String message) {
        if (message == null || !message.contains("&")) {
            assert message != null;
            return MiniMessage.miniMessage().deserialize(message).decoration(TextDecoration.ITALIC, false);
        }

        // Reemplazar códigos de color con etiquetas MiniMessage
        StringBuilder builder = new StringBuilder(message.length() + 16);
        int length = message.length();

        for (int i = 0; i < length; i++) {
            char c = message.charAt(i);
            if (c == '&' && i + 1 < length) {
                char next = message.charAt(i + 1);
                String replacement = COLOR_MAP.get(next);
                if (replacement != null) {
                    builder.append(replacement);
                    i++;
                } else {
                    builder.append(c);
                }
            } else {
                builder.append(c);
            }
        }

        // Reemplazar posibles etiquetas de degradado y otras configuraciones
        return MiniMessage.miniMessage().deserialize(builder.toString())
                .decoration(TextDecoration.ITALIC, false);
    }

    public static String translateColorsString(String message) {
        if (message == null || !message.contains("&")) {
            return message;
        }

        // Reemplazar códigos de color con etiquetas MiniMessage
        StringBuilder builder = new StringBuilder(message.length() + 16);
        int length = message.length();

        for (int i = 0; i < length; i++) {
            char c = message.charAt(i);
            if (c == '&' && i + 1 < length) {
                char next = message.charAt(i + 1);
                String replacement = COLOR_MAP.get(next);
                if (replacement != null) {
                    builder.append(replacement);
                    i++;
                } else {
                    builder.append(c);
                }
            } else {
                builder.append(c);
            }
        }

        // Convertir el mensaje formateado con MiniMessage a un Component
        Component component = MiniMessage.miniMessage().deserialize(builder.toString());

        // Convertir el Component a una cadena en formato legacy
        return LegacyComponentSerializer.legacySection().serialize(component);
    }

    // Validar y transformar color
    public static String formatColor(String color) {
        if (HEX_PATTERN.matcher(color).matches()) {
            return "<#" + color.substring(1) + ">";
        } else if (LEGACY_PATTERN.matcher(color).matches()) {
            char code = color.charAt(1);
            return COLOR_MAP.getOrDefault(code, "<reset>");
        } else if (CUSTOM_HEX_PATTERN.matcher(color).matches()) {
            return color;
        } else {
            return "<reset>"; // Retornar un color por defecto si no es válido
        }
    }

    public static boolean isValidColor(String color) {
        return HEX_PATTERN.matcher(color).matches() ||
                LEGACY_PATTERN.matcher(color).matches() ||
                CUSTOM_HEX_PATTERN.matcher(color).matches();
    }

    public static int getColorDecimal(String hexColor) {
        // Eliminar los posibles prefijos y el símbolo '#'
        hexColor = hexColor.replaceAll("<#|>|&#", "").replaceAll("#", "");

        // Convertir el valor hexadecimal a decimal
        try {
            Bukkit.getLogger().info("Color: " + hexColor);
            int colorDecimal = Integer.parseInt(hexColor, 16);
            Bukkit.getLogger().info("Color decimal: " + colorDecimal);
            return colorDecimal;
        } catch (NumberFormatException e) {
            // Manejar error en caso de que el formato no sea válido
            System.err.println("Error: Formato de color hexadecimal inválido.");
            return 0; // Devolver 0 o algún valor predeterminado en caso de error
        }
    }

}
