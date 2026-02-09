package me.orine.pluginhide.command;

import me.orine.pluginhide.PluginHidePlugin;
import me.orine.pluginhide.config.PluginHideConfig;
import me.orine.pluginhide.service.MessageFormatter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public final class CmdMaskCommand implements TabExecutor {

    private static final String RELOAD_PERMISSION = "pluginhide.reload";

    private final PluginHidePlugin plugin;

    public CmdMaskCommand(PluginHidePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        PluginHideConfig cfg = plugin.getPluginConfigData();
        MessageFormatter formatter = plugin.getMessageFormatter();

        if (!sender.hasPermission(RELOAD_PERMISSION)) {
            sender.sendMessage(formatter.format(cfg.getNoPermissionMessage(), "", label));
            return true;
        }

        if (args.length == 1 && "reload".equalsIgnoreCase(args[0])) {
            plugin.reloadPluginState();

            PluginHideConfig reloadedCfg = plugin.getPluginConfigData();
            MessageFormatter reloadedFormatter = plugin.getMessageFormatter();
            sender.sendMessage(reloadedFormatter.format(reloadedCfg.getReloadSuccessMessage(), "", label));
            return true;
        }

        sender.sendMessage(formatter.format(cfg.getUsageMessage(), "", label));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!sender.hasPermission(RELOAD_PERMISSION)) {
            return Collections.emptyList();
        }

        if (args.length == 1) {
            String start = args[0].toLowerCase(Locale.ROOT);
            if ("reload".startsWith(start)) {
                List<String> result = new ArrayList<String>(1);
                result.add("reload");
                return result;
            }
        }

        return Collections.emptyList();
    }
}
