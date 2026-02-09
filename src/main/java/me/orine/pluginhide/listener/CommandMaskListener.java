package me.orine.pluginhide.listener;

import me.orine.pluginhide.PluginHidePlugin;
import me.orine.pluginhide.config.PluginHideConfig;
import me.orine.pluginhide.model.ParsedCommand;
import me.orine.pluginhide.service.CommandFilterService;
import me.orine.pluginhide.service.MessageFormatter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerCommandSendEvent;
import org.bukkit.event.server.TabCompleteEvent;

import java.util.Collection;
import java.util.List;

public final class CommandMaskListener implements Listener {

    private final PluginHidePlugin plugin;
    private final CommandFilterService filterService;

    public CommandMaskListener(PluginHidePlugin plugin, CommandFilterService filterService) {
        this.plugin = plugin;
        this.filterService = filterService;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if (filterService.hasBypass(player)) {
            return;
        }

        ParsedCommand parsed = filterService.parseCommand(event.getMessage());
        if (!filterService.shouldBlock(parsed)) {
            return;
        }

        event.setCancelled(true);

        PluginHideConfig cfg = plugin.getPluginConfigData();
        if (cfg.isCommandNotFoundEnabled()) {
            MessageFormatter formatter = plugin.getMessageFormatter();
            player.sendMessage(formatter.format(cfg.getCommandNotFoundMessage(), parsed.getNormalized(), "cmdmask"));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerCommandSend(PlayerCommandSendEvent event) {
        Player player = event.getPlayer();
        if (filterService.hasBypass(player)) {
            return;
        }

        Collection<String> commands = event.getCommands();
        filterService.filterSuggestions(commands);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onTabComplete(TabCompleteEvent event) {
        if (!(event.getSender() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getSender();
        if (filterService.hasBypass(player)) {
            return;
        }

        if (!filterService.isCommandRootCompletion(event.getBuffer())) {
            return;
        }

        List<String> completions = event.getCompletions();
        if (completions == null || completions.isEmpty()) {
            return;
        }
        filterService.filterSuggestions(completions);
    }
}
