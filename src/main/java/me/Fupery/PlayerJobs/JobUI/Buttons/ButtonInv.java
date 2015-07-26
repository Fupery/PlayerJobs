package me.Fupery.PlayerJobs.JobUI.Buttons;

import me.Fupery.PlayerJobs.JobUI.AbstractMenu;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class ButtonInv extends AbstractButton {

    private Inventory linkedInventory;

    public ButtonInv(AbstractMenu menu, String[] displayText,
                     Material displayItem, Inventory linkedInventory) {
        super(menu, displayText, displayItem);
        this.linkedInventory = linkedInventory;
    }

    @Override
    public void onInventoryClick(AbstractMenu menu, InventoryClickEvent event) {
        menu.getHandler().openInv(linkedInventory);
    }
}
