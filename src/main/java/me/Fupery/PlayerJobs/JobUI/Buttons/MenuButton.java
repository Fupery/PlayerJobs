package me.Fupery.PlayerJobs.JobUI.Buttons;

import me.Fupery.PlayerJobs.JobUI.AbstractMenu;
import org.bukkit.event.inventory.InventoryClickEvent;

public interface MenuButton {

    void onInventoryClick(AbstractMenu menu, InventoryClickEvent event);
}
