package com.lifedestroyed.uct.utils;

import org.bukkit.ChatColor;
import java.util.regex.Pattern;

public final class ColorUtils {
    private static final Pattern HEX_PATTERN = Pattern.compile("&#([a-fA-F0-9]{6})");

    public static String format(String text) {
        if (text == null) return "";
        return translateHex(ChatColor.translateAlternateColorCodes('&', text));
    }

    private static String translateHex(String text) {
        var matcher = HEX_PATTERN.matcher(text);
        var buffer = new StringBuffer();

        while (matcher.find()) {
            String hex = matcher.group(1);
            var replacement = new StringBuilder("§x");
            for (char c : hex.toCharArray()) {
                replacement.append("§").append(c);
            }
            matcher.appendReplacement(buffer, replacement.toString());
        }
        return matcher.appendTail(buffer).toString();
    }
}