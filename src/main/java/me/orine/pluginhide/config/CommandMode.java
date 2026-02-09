package me.orine.pluginhide.config;

import java.util.Locale;

public enum CommandMode {
    BLACKLIST,
    WHITELIST;

    public static CommandMode fromString(String raw) {
        if (raw == null) {
            return BLACKLIST;
        }
        return "WHITELIST".equals(raw.trim().toUpperCase(Locale.ROOT)) ? WHITELIST : BLACKLIST;
    }
}
