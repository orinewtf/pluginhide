package me.orine.pluginhide;

import me.orine.pluginhide.command.CmdMaskCommand;
import me.orine.pluginhide.config.PluginHideConfig;
import me.orine.pluginhide.listener.CommandMaskListener;
import me.orine.pluginhide.service.CommandFilterService;
import me.orine.pluginhide.service.MessageFormatter;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class PluginHidePlugin extends JavaPlugin {

    private PluginHideConfig pluginConfig;
    private MessageFormatter messageFormatter;
    private CommandFilterService filterService;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        filterService = new CommandFilterService(this);
        reloadPluginState();

        Bukkit.getPluginManager().registerEvents(new CommandMaskListener(this, filterService), this);

        PluginCommand cmd = getCommand("cmdmask");
        if (cmd != null) {
            CmdMaskCommand handler = new CmdMaskCommand(this);
            cmd.setExecutor(handler);
            cmd.setTabCompleter(handler);
        }
    }

    public void reloadPluginState() {
        reloadConfig();
        pluginConfig = PluginHideConfig.from(getConfig());
        messageFormatter = new MessageFormatter(pluginConfig.getMessagePrefix());
        filterService.reload(pluginConfig);
    }

    public PluginHideConfig getPluginConfigData() {
        return pluginConfig;
    }

    public MessageFormatter getMessageFormatter() {
        return messageFormatter;
    }
}
