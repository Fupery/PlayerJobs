package me.Fupery.PlayerJobs.JobUI.Buttons;

import me.Fupery.PlayerJobs.IO.JSONBuilder;
import me.Fupery.PlayerJobs.JobUI.AbstractMenu;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ButtonChat extends AbstractButton {

    public ButtonChat(AbstractMenu menu, String[] displayText, Material displayItem) {
        super(menu, displayText, displayItem);
    }

    @Override
    public void onInventoryClick(AbstractMenu menu, InventoryClickEvent event) {
        menu.passValues(this, returnValue);
    }
}
