package me.orine.pluginhide.service;

import me.orine.pluginhide.config.CommandMode;
import me.orine.pluginhide.config.PluginHideConfig;
import me.orine.pluginhide.model.ParsedCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class CommandFilterService {

    private final Plugin plugin;
    private final CommandNormalizer normalizer;
    private final Set<String> configuredCommands;
    private final Set<String> effectiveBlacklist;

    private CommandMode mode;
    private String bypassPermission;

    public CommandFilterService(Plugin plugin) {
        this.plugin = plugin;
        this.normalizer = new CommandNormalizer();
        this.configuredCommands = new HashSet<String>();
        this.effectiveBlacklist = new HashSet<String>();
        this.mode = CommandMode.BLACKLIST;
        this.bypassPermission = "pluginhide.bypass";
    }

    public void reload(PluginHideConfig config) {
        this.mode = config.getMode();
        this.bypassPermission = config.getBypassPermission();

        configuredCommands.clear();
        List<String> list = config.getCommands();
        for (int i = 0; i < list.size(); i++) {
            ParsedCommand parsed = normalizer.parse(list.get(i));
            if (!parsed.isEmpty()) {
                configuredCommands.add(parsed.getNormalized());
            }
        }

        rebuildEffectiveBlacklist();
    }

    public boolean hasBypass(Player player) {
        return bypassPermission != null && !bypassPermission.isEmpty() && player.hasPermission(bypassPermission);
    }

    public ParsedCommand parseCommand(String raw) {
        return normalizer.parse(raw);
    }

    public boolean isCommandRootCompletion(String buffer) {
        return normalizer.isCommandRootCompletion(buffer);
    }

    public boolean shouldBlock(ParsedCommand parsed) {
        if (parsed.isEmpty()) {
            return false;
        }
        if (parsed.hadNamespace()) {
            return true;
        }
        if (mode == CommandMode.WHITELIST) {
            return !configuredCommands.contains(parsed.getNormalized());
        }
        return effectiveBlacklist.contains(parsed.getNormalized());
    }

    public void filterSuggestions(Collection<String> suggestions) {
        Iterator<String> iterator = suggestions.iterator();
        while (iterator.hasNext()) {
            ParsedCommand parsed = normalizer.parse(iterator.next());
            if (shouldBlock(parsed)) {
                iterator.remove();
            }
        }
    }

    private void rebuildEffectiveBlacklist() {
        effectiveBlacklist.clear();
        if (mode != CommandMode.BLACKLIST) {
            return;
        }

        effectiveBlacklist.addAll(configuredCommands);
        Map<String, Command> knownCommands = getKnownCommandsMap();
        if (knownCommands == null || knownCommands.isEmpty()) {
            return;
        }

        Set<Command> hiddenCommandObjects = new HashSet<Command>();
        for (Map.Entry<String, Command> entry : knownCommands.entrySet()) {
            ParsedCommand key = normalizer.parse(entry.getKey());
            Command cmd = entry.getValue();
            ParsedCommand name = cmd == null ? ParsedCommand.EMPTY : normalizer.parse(cmd.getName());
            if (configuredCommands.contains(key.getNormalized()) || configuredCommands.contains(name.getNormalized())) {
                hiddenCommandObjects.add(cmd);
            }
        }

        if (hiddenCommandObjects.isEmpty()) {
            return;
        }

        for (Map.Entry<String, Command> entry : knownCommands.entrySet()) {
            Command cmd = entry.getValue();
            if (!hiddenCommandObjects.contains(cmd)) {
                continue;
            }

            ParsedCommand entryLabel = normalizer.parse(entry.getKey());
            if (!entryLabel.isEmpty()) {
                effectiveBlacklist.add(entryLabel.getNormalized());
            }

            if (cmd == null) {
                continue;
            }

            ParsedCommand mainName = normalizer.parse(cmd.getName());
            if (!mainName.isEmpty()) {
                effectiveBlacklist.add(mainName.getNormalized());
            }

            List<String> aliases = cmd.getAliases();
            for (int i = 0; i < aliases.size(); i++) {
                ParsedCommand alias = normalizer.parse(aliases.get(i));
                if (!alias.isEmpty()) {
                    effectiveBlacklist.add(alias.getNormalized());
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private Map<String, Command> getKnownCommandsMap() {
        try {
            Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            Object commandMapObj = commandMapField.get(Bukkit.getServer());
            if (!(commandMapObj instanceof CommandMap)) {
                return null;
            }

            Field knownCommandsField = commandMapObj.getClass().getDeclaredField("knownCommands");
            knownCommandsField.setAccessible(true);
            Object known = knownCommandsField.get(commandMapObj);
            if (!(known instanceof Map)) {
                return null;
            }
            return (Map<String, Command>) known;
        } catch (ReflectiveOperationException ex) {
            plugin.getLogger().warning("Could not read command map for alias expansion: " + ex.getMessage());
            return null;
        }
    }
}
