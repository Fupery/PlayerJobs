package me.Fupery.PlayerJobs.Listeners;

import me.Fupery.PlayerJobs.JobUI.MenuHandler;
import me.Fupery.PlayerJobs.PlayerJobs;
import me.Fupery.PlayerJobs.Utils.Formatting;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class InventoryCloseListener implements Listener {

    private PlayerJobs plugin;

    public InventoryCloseListener(PlayerJobs plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {

        Player player = ((Player) event.getPlayer());

        if (plugin.getOpenMenus().containsKey(player)) {

            String title = event.getInventory().getTitle();
            final MenuHandler handler = plugin.getOpenMenus().get(player);

            if (title.equals(Formatting.menuHeading)) {
                handler.closeRoot();

            } else if (title.equals(Formatting.inventoryHeading)) {
                handler.closeInv();
                Bukkit.getScheduler().runTask(plugin, new Runnable() {
                    @Override
                    public void run() {
                        handler.openRoot();
                    }
                });

            } else if (title.equals(Formatting.employeeMenuHeading)
                    || title.equals(Formatting.logMenuHeading)
                    || title.equals(Formatting.filterMenuHeading)) {

                handler.closeBranch();

                Bukkit.getScheduler().runTask(plugin, new Runnable() {
                    @Override
                    public void run() {
                        handler.openRoot();
                    }
                });
            }
        }
    }
}
