package me.Fupery.PlayerJobs.Listeners;

import me.Fupery.PlayerJobs.JobUI.AbstractMenu;
import me.Fupery.PlayerJobs.JobUI.MenuHandler;
import me.Fupery.PlayerJobs.JobUI.SubMenus.JobMenu;
import me.Fupery.PlayerJobs.JobUI.SubMenus.MenuEmployees;
import me.Fupery.PlayerJobs.JobUI.SubMenus.MenuFilter;
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

        String title = event.getInventory().getTitle().substring(2, 7);

        if (title.equals("[Job]")) {

            Player player = ((Player) event.getPlayer());

            if (plugin.getOpenMenus().containsKey(player)) {
                AbstractMenu menu =
                        plugin.getOpenMenus().get(player).getMenu(event.getInventory());

                final MenuHandler handler = plugin.getOpenMenus().get(player);

                if (menu instanceof JobMenu) {
                    handler.closeRoot();

                } else if (event.getInventory().getTitle().equals(Formatting.inventoryHeading)) {
                    handler.closeInv();
                    Bukkit.getScheduler().runTask(plugin, new Runnable() {
                        @Override
                        public void run() {
                            handler.openRoot();
                        }
                    });

                } else if (menu instanceof MenuEmployees
                        || menu instanceof MenuFilter) {

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
}
