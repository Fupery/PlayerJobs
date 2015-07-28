package me.Fupery.PlayerJobs.Listeners;

import me.Fupery.PlayerJobs.PlayerJobs;
import me.Fupery.PlayerJobs.Utils.Formatting;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryDragEvent;

public class InventoryDragListener implements Listener {

    PlayerJobs plugin;

    public InventoryDragListener(PlayerJobs plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryDragEvent(InventoryDragEvent event) {
        String title = event.getInventory().getTitle();

        if (title.equals(Formatting.menuHeading)
                || title.equals(Formatting.employeeMenuHeading)
                || title.equals(Formatting.filterMenuHeading)) {

            event.setCancelled(true);
        }

    }
}
