package me.orine.pluginhide.config;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class PluginHideConfig {

    private final CommandMode mode;
    private final String bypassPermission;
    private final List<String> commands;
    private final boolean commandNotFoundEnabled;
    private final String commandNotFoundMessage;
    private final String messagePrefix;
    private final String noPermissionMessage;
    private final String reloadSuccessMessage;
    private final String usageMessage;

    private PluginHideConfig(
            CommandMode mode,
            String bypassPermission,
            List<String> commands,
            boolean commandNotFoundEnabled,
            String commandNotFoundMessage,
            String messagePrefix,
            String noPermissionMessage,
            String reloadSuccessMessage,
            String usageMessage
    ) {
        this.mode = mode;
        this.bypassPermission = bypassPermission;
        this.commands = commands;
        this.commandNotFoundEnabled = commandNotFoundEnabled;
        this.commandNotFoundMessage = commandNotFoundMessage;
        this.messagePrefix = messagePrefix;
        this.noPermissionMessage = noPermissionMessage;
        this.reloadSuccessMessage = reloadSuccessMessage;
        this.usageMessage = usageMessage;
    }

    public static PluginHideConfig from(FileConfiguration cfg) {
        CommandMode mode = CommandMode.fromString(cfg.getString("mode", "BLACKLIST"));
        String bypassPermission = cfg.getString("bypass-permission", "pluginhide.bypass");

        List<String> commands = new ArrayList<String>(cfg.getStringList("commands"));

        boolean commandNotFoundEnabled = cfg.getBoolean("command-not-found.enabled", true);
        String commandNotFoundMessage = cfg.getString("command-not-found.message", "{prefix}&cCommand not found.");

        String messagePrefix = cfg.getString("messages.prefix", "&b&lPluginHide &8>> &7");
        String noPermissionMessage = cfg.getString("messages.no-permission", "{prefix}&cNo permission.");
        String reloadSuccessMessage = cfg.getString("messages.reload-success", "{prefix}&aConfiguration reloaded.");
        String usageMessage = cfg.getString("messages.usage", "{prefix}&7Usage: &f/{label} reload");

        return new PluginHideConfig(
                mode,
                bypassPermission == null ? "" : bypassPermission,
                Collections.unmodifiableList(commands),
                commandNotFoundEnabled,
                commandNotFoundMessage == null ? "" : commandNotFoundMessage,
                messagePrefix == null ? "" : messagePrefix,
                noPermissionMessage == null ? "" : noPermissionMessage,
                reloadSuccessMessage == null ? "" : reloadSuccessMessage,
                usageMessage == null ? "" : usageMessage
        );
    }

    public CommandMode getMode() {
        return mode;
    }

    public String getBypassPermission() {
        return bypassPermission;
    }

    public List<String> getCommands() {
        return commands;
    }

    public boolean isCommandNotFoundEnabled() {
        return commandNotFoundEnabled;
    }

    public String getCommandNotFoundMessage() {
        return commandNotFoundMessage;
    }

    public String getMessagePrefix() {
        return messagePrefix;
    }

    public String getNoPermissionMessage() {
        return noPermissionMessage;
    }

    public String getReloadSuccessMessage() {
        return reloadSuccessMessage;
    }

    public String getUsageMessage() {
        return usageMessage;
    }
}
