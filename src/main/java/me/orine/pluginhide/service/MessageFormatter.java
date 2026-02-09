package me.orine.pluginhide.service;

import net.md_5.bungee.api.ChatColor;

public final class MessageFormatter {
    private final String prefix;

    public MessageFormatter(String prefix) {
        this.prefix = prefix == null ? "" : prefix;
    }

    public String format(String template, String command, String label) {
        if (template == null || template.isEmpty()) {
            return "";
        }

        String replaced = template
                .replace("%command%", command)
                .replace("{command}", command)
                .replace("%label%", label)
                .replace("{label}", label)
                .replace("%prefix%", prefix)
                .replace("{prefix}", prefix);

        return ChatColor.translateAlternateColorCodes('&', applyHexColors(replaced));
    }

    private String applyHexColors(String input) {
        StringBuilder out = new StringBuilder(input.length() + 16);
        int i = 0;
        int n = input.length();

        while (i < n) {
            char c = input.charAt(i);
            if (c == '#' && i + 6 < n) {
                int hex = 0;
                boolean valid = true;
                for (int j = 1; j <= 6; j++) {
                    int value = Character.digit(input.charAt(i + j), 16);
                    if (value < 0) {
                        valid = false;
                        break;
                    }
                    hex = (hex << 4) + value;
                }

                if (valid) {
                    out.append(ChatColor.of(String.format("#%06X", hex)));
                    i += 7;
                    continue;
                }
            }

            out.append(c);
            i++;
        }

        return out.toString();
    }
}
